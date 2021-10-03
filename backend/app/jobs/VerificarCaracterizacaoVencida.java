package jobs;


import models.caracterizacao.Caracterizacao;
import models.caracterizacao.LicencaValidacaoDataVencimento;
import models.caracterizacao.StatusCaracterizacao;
import play.Logger;
import play.jobs.On;

import java.util.Calendar;
import java.util.List;

/**
 * Job que verifica se a vigencia requerida está vencida".
 */
@On("cron.verificarCaracterizacaoVencida")
public class VerificarCaracterizacaoVencida extends GenericJob{

    @Override
    public void executar() {

        Logger.info("VerificarCaracterizacaoVencida:: Iniciando job");
        verificarCaracterizacaoVencida();
        Logger.info("VerificarCaracterizacaoVencida:: Encerrando job");
    }

    private void verificarCaracterizacaoVencida(){

        List<LicencaValidacaoDataVencimento> licencaValidacaoDataVencimentos =
                LicencaValidacaoDataVencimento.find("caracterizacao.status.id = :idStatus")
                .setParameter("idStatus", StatusCaracterizacao.DEFERIDO).fetch();

        licencaValidacaoDataVencimentos.forEach(this::verificarPrazoVencimento);
    }

    private void verificarPrazoVencimento(LicencaValidacaoDataVencimento licencaValidacaoDataVencimentos){

        Calendar dataValidade = Calendar.getInstance();
        dataValidade.setTime(licencaValidacaoDataVencimentos.dataValidade);

        if (Calendar.getInstance().after(dataValidade)) {
            alterarStatusCaracterizacao(licencaValidacaoDataVencimentos.caracterizacao.id);
            commitTransaction();
        }
    }

    private void alterarStatusCaracterizacao(Long id_caracterizacao){

        Caracterizacao caracterizacao = Caracterizacao.findById(id_caracterizacao);
        caracterizacao.status = StatusCaracterizacao.findById(StatusCaracterizacao.VENCIDO);
        caracterizacao._save();
    }

}
