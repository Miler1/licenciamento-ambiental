package jobs;

import models.analise.ParecerAnalistaTecnico;
import models.caracterizacao.*;
import play.Logger;
import play.jobs.On;
import utils.WebServiceEntradaUnica;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Job que verifica a isenção dos empreendimento a partir das analises que foram aprovadas".
 */
@On("cron.processamentoAnaliseAprovada")
//@OnApplicationStart
public class ProcessamentoAnaliseAprovada extends GenericJob {

    @Override
    public void executar() {

        Logger.info("ProcessamentoAnaliseAprovada:: Iniciando job");

        List<Caracterizacao> caracterizacoes = Caracterizacao.find("status.id = :idStatus").
                setParameter("idStatus", StatusCaracterizacao.ANALISE_APROVADA).fetch();

        for (Caracterizacao caracterizacao : caracterizacoes) {

            try {

                verificarAlteracaoVigencia(caracterizacao);
                TipoLicenca tipoLicencaAprovada = !caracterizacao.tiposLicencaEmAndamento.isEmpty() ?
                        caracterizacao.tiposLicencaEmAndamento.get(0) : caracterizacao.tipoLicenca;

                caracterizacao.empreendimento.empreendimentoEU =
                        WebServiceEntradaUnica.findEmpreendimentosByCpfCnpj(caracterizacao.empreendimento.cpfCnpj).empreendimentoEU;

                DaeLicenciamento daeLicenciamento = DaeLicenciamento.findByCaracterizacao(caracterizacao);

                if (daeLicenciamento == null) {

                    Caracterizacao carac = caracterizacao.processo.caracterizacoes.stream()
                            .min(Comparator.comparing(caracterizacao1 -> caracterizacao1.id)).get();

                    daeLicenciamento = DaeLicenciamento.findByCaracterizacao(carac);
                    daeLicenciamento.caracterizacao = caracterizacao;
                    caracterizacao.daeLicenciamento = daeLicenciamento;

                }

                if (caracterizacao.empreendimento.isIsento() || tipoLicencaAprovada.sigla.equals("CA") ||
                        daeLicenciamento.valor == 0) {

                    caracterizacao.daeLicenciamento.dataPagamento = new Date();
                    caracterizacao.daeLicenciamento._save();
                    caracterizacao.daeLicenciamento.processamentoDeferimentoDAELicenca();

                    if (caracterizacao.isRenovacao()) {
                        Caracterizacao.ocultaExibicaoListagem(caracterizacao.idCaracterizacaoOrigem);
                    }

                } else {
                    caracterizacao.status =
                            StatusCaracterizacao.findById(StatusCaracterizacao.AGUARDANDO_EMISSAO_TAXA_LICENCIAMENTO);
                }

                caracterizacao._save();

                commitTransaction();

            } catch (Exception e) {

                Logger.error(e, e.getMessage());
                rollbackTransaction();

            }

        }

        Logger.info("ProcessamentoAnaliseAprovada:: Encerrando job");
    }

    private void verificarAlteracaoVigencia(Caracterizacao caracterizacao) {

        int validadePermitida = ParecerAnalistaTecnico.findValidadePermitida(caracterizacao);

        //se validade foi alterada de acordo com o parecer do analista técnico
        if (validadePermitida > 0 && validadePermitida != caracterizacao.vigenciaSolicitada) {
            //primeiro atualizar para nova dataValidade
            Licenca.atualizarDataValidadeLicenca(caracterizacao, validadePermitida);

            //depois atualizar vigencia solicitada
            Caracterizacao.atualizarVigenciaCaracterizacao(caracterizacao, validadePermitida);

            //calcular o novo valor da taxa de licenciamento
            caracterizacao.calcularTaxaLicenciamentoAposAprovadaNoAnalise(validadePermitida);

        }

    }

}