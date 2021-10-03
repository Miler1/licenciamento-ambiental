package jobs;

import java.util.List;

import models.caracterizacao.Caracterizacao;
import models.caracterizacao.DispensaLicenciamento;
import models.caracterizacao.Licenca;
import models.caracterizacao.StatusCaracterizacao;
import models.caracterizacao.TipoLicenca;
import play.Logger;
import play.jobs.Job;

/*
 * Este JOB tem o intuito de tratar erros que podem vir a ocorrer, sempre que necessário, esse job deve ser
 * incrementado para abranger outros erros que não estão sendo tratados aqui.
 */

public class ReprocessarLicencasJob extends Job {

	@Override
	public void doJob() throws Exception {

		reprocessarDLAs();
		reprocessarLicencas();

	}

	private void reprocessarDLAs() {

		Logger.info("::CorrigirDLAs:: Iniciando job");

		List<DispensaLicenciamento> dlasSemDocumento = DispensaLicenciamento.find("byDocumentoIsNullAndNumeroIsNull").fetch();

		for(DispensaLicenciamento dla : dlasSemDocumento) {

			Logger.info("::CorrigirDLAs:: Processando DLA: " + dla.id);

			dla.reprocessarDLA();
		}

		Logger.info("::CorrigirDLAs:: Job finalizado");

	}

	private void reprocessarLicencas() {

		Logger.info("::ReprocessarLicencasNaoEmitidas:: Iniciando JOB");

		StatusCaracterizacao status = StatusCaracterizacao.findById(StatusCaracterizacao.DEFERIDO);
		TipoLicenca tipo = TipoLicenca.findById(TipoLicenca.DISPENSA_LICENCA_AMBIENTAL);

		List<Caracterizacao> caracterizacoesDeferidasSemLicenca = Caracterizacao.find("byStatusAndTipoLicencaNotEqual", status, tipo).fetch();

		for(Caracterizacao caracterizacao : caracterizacoesDeferidasSemLicenca) {

			if(caracterizacao.licencas.isEmpty()) {

				Logger.info("::ReprocessarLicencasNaoEmitidas:: Processo número - " + caracterizacao.numero);

				Licenca licenca = new Licenca(caracterizacao);
				licenca.gerar();

			}

		}


		Logger.info("::ReprocessarLicencasNaoEmitidas:: JOB Finalizado");

	}
}
