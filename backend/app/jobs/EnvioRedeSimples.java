package jobs;

import br.ufla.lemaf.beans.Message;
import br.ufla.lemaf.beans.pessoa.ConfirmaRespostaOrgaoRedeSimples;
import models.caracterizacao.ComunicacaoRedeSimples;
import models.caracterizacao.Licenca;
import org.apache.commons.io.IOUtils;
import play.Logger;
import play.jobs.On;
import play.jobs.OnApplicationStart;
import utils.Configuracoes;
import utils.DataUtils;
import utils.WebServiceEntradaUnica;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Job que envia arquivo à Rede Simples".
 */
@On("cron.envioRedeSimples")
@OnApplicationStart
public class EnvioRedeSimples extends GenericJob {

	@Override
	public void executar() {

		Logger.info("EnvioRedeSimples:: Iniciando job");

		List<ComunicacaoRedeSimples> comunicacoesRedeSimples = ComunicacaoRedeSimples.findAll();

		for(ComunicacaoRedeSimples comunicacaoRedeSimples: comunicacoesRedeSimples) {

			processarEnvioRedeSimples(comunicacaoRedeSimples);
		}

		Logger.info("EnvioRedeSimples:: Encerrando job");
	}

	private void processarEnvioRedeSimples(ComunicacaoRedeSimples comunicacaoRedeSimples) {

		if(Configuracoes.SEND_REDE_SIMPLES_ENABLED) {
			try {

				Logger.info("EnvioRedeSimples:: Processando envio id " + comunicacaoRedeSimples.id + " e caracterização id " + comunicacaoRedeSimples.caracterizacao.id);

				StringBuilder message = new StringBuilder();
				String separador = " / ";
				if (comunicacaoRedeSimples.caracterizacao.empreendimento.pessoa.isPessoaJuridica()) {

					if (comunicacaoRedeSimples.caracterizacao.dispensa != null) {
						File file = new File(Configuracoes.ARQUIVOS_DOCUMENTOS_PATH + File.separator + comunicacaoRedeSimples.caracterizacao.dispensa.documento.caminho);
						byte[] byteArrayFile = new byte[0];
						try {
							byteArrayFile = IOUtils.toByteArray(file.toURI());
						} catch (IOException e) {
							throw new Exception(e);
						}
						ConfirmaRespostaOrgaoRedeSimples confirmaRespostaOrgaoRedeSimples = new ConfirmaRespostaOrgaoRedeSimples(
								comunicacaoRedeSimples.caracterizacao.empreendimento.pessoa.getCpfCnpj(),
								"DI",
								comunicacaoRedeSimples.caracterizacao.dispensa.numero,
								DataUtils.formataData(comunicacaoRedeSimples.caracterizacao.dae.dataPagamento, "yyyyMMdd"),
								byteArrayFile
						);
						message.append(WebServiceEntradaUnica.enviarDocumentoRedeSimples(confirmaRespostaOrgaoRedeSimples).getText());
					} else if (comunicacaoRedeSimples.caracterizacao.licencas != null && !comunicacaoRedeSimples.caracterizacao.licencas.isEmpty()) {
						for(Licenca l : comunicacaoRedeSimples.caracterizacao.licencas) {
							File file = new File(Configuracoes.ARQUIVOS_DOCUMENTOS_PATH + File.separator + l.documento.caminho);
							byte[] byteArrayFile = new byte[0];
							try {
								byteArrayFile = IOUtils.toByteArray(file.toURI());
							} catch (IOException e) {
								throw new Exception(e);
							}
							ConfirmaRespostaOrgaoRedeSimples confirmaRespostaOrgaoRedeSimples = new ConfirmaRespostaOrgaoRedeSimples(
									comunicacaoRedeSimples.caracterizacao.empreendimento.pessoa.getCpfCnpj(),
									"LE",
									l.numero,
									DataUtils.formataData(comunicacaoRedeSimples.caracterizacao.dae.dataPagamento, "yyyyMMdd"),
									byteArrayFile
							);
							message.append(WebServiceEntradaUnica.enviarDocumentoRedeSimples(confirmaRespostaOrgaoRedeSimples).getText());
							message.append(separador);
						}
						if (message.length() > 0) {
							message.setLength(message.length() - separador.length());
						}
					}
				} else if (comunicacaoRedeSimples.caracterizacao.empreendimento.pessoa.isPessoaFisica()) {
					message.append("Não enviado pois o empreendimento é uma Pessoa Física.");
				}
				else {
					throw new Exception("O Empreendimento não é Pessoa Júrida ou Pessoa Física.");
				}

				comunicacaoRedeSimples.delete();

				commitTransaction();

				Logger.info("EnvioRedeSimples:: Finalizado envio id " + comunicacaoRedeSimples.id + " e caracterização id " + comunicacaoRedeSimples.caracterizacao.id + " - " + message.toString());

			} catch (Exception e) {

				Logger.error(e.getMessage());

				comunicacaoRedeSimples.tentativas++;
				comunicacaoRedeSimples.save();
				commitTransaction();
			}
		}

	}

}
