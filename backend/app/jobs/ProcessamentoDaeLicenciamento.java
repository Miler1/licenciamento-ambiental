package jobs;

import models.caracterizacao.DaeLicenciamento;
import play.Logger;
import play.Play;
import play.jobs.On;
import play.jobs.OnApplicationStart;
import sefaz.VO.processamentoDAE.DaePagoVO;
import utils.Configuracoes;
import utils.DataUtils;
import utils.WebServiceSftpSEFAZ;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import static play.db.jpa.JPA.em;

/**
 * Job que verifica o pagamento de DAEs das Licenças".
 */
@On("cron.processamentoDaesLicenciamento")
@OnApplicationStart
public class ProcessamentoDaeLicenciamento extends GenericJob {

	private static final int TERCA_FEIRA = 3;
	private final static String APPLICATION_TEMP_FOLDER =
			Play.applicationPath + File.separator + Play.configuration.getProperty("path.arquivos.tempFolder")+"DAE/";
	private static final String SEFAZ_CODIGO_TAXA_LICENCA_PREVIA = Play.configuration.getProperty("sefaz.codigo.taxa.licenca.previa");
	private static final String SEFAZ_CODIGO_TAXA_LICENCA_INSTALACAO = Play.configuration.getProperty("sefaz.codigo.taxa.licenca.instalacao");
	private static final String SEFAZ_CODIGO_TAXA_LICENCA_OPERACAO = Play.configuration.getProperty("sefaz.codigo.taxa.licenca.operacao");
	private static final String SEFAZ_CODIGO_TAXA_RENOVACAO_LICENCA_INSTALACAO = Play.configuration.getProperty("sefaz.codigo.taxa.renovacao.licenca.instalacao");
	private static final String SEFAZ_CODIGO_TAXA_RENOVACAO_LICENCA_OPERACAO = Play.configuration.getProperty("sefaz.codigo.taxa.renovacao.licenca.operacao");
	private static WebServiceSftpSEFAZ webService = new WebServiceSftpSEFAZ();

	@Override
	public void executar() {

		Logger.info("ProcessamentoDaeLicenciamento:: Iniciando job");

		List<DaeLicenciamento> daesParaBuscar = buscaDaesLicenciamentoParaProcessar();

		if(!daesParaBuscar.isEmpty()) {

			processarPagamentoDaeLicenciamentoDiario(daesParaBuscar);

		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int day = cal.get(Calendar.DAY_OF_WEEK);

		if(day == TERCA_FEIRA && !daesParaBuscar.isEmpty()) {

			daesParaBuscar = buscaDaesLicenciamentoParaProcessar();
			processarPagamentoDaeLicenciamentoSemanal(daesParaBuscar);

		}

		//TODO: Descomentar quando for voltar o pagamento para o Gestão de Pagamentos
//		for(DaeLicenciamento daeLicenciamento : daesParaBuscar) {
//
//			processarPagamentoDae(daeLicenciamento);
//		}

		Logger.info("ProcessamentoDaeLicenciamento:: Encerrando job");
	}

	private void processarPagamentoDaeLicenciamentoSemanal(List<DaeLicenciamento> daesParaBuscar) {

		Logger.info("=== PAGAMENTO DAE LICENÇAS EMITIDAS SEMANAL(ÚLTIMOS 6 MÊSES) - VERIFICA SE PAGAMENTO FOI REALIZADO NA SEFAZ === ");

		String dateFormated = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
//		TODO: deixar aqui para teste
//		dateFormated = dateFormated.replace("11", "09");
		Date dateBefore = DataUtils.removeMeses(new Date(), 6);
		String dateBeforeFormated = new SimpleDateFormat("yyyy-MM-dd").format(dateBefore);
//		TODO: deixar aqui para teste
//		dateBeforeFormated = dateBeforeFormated.replace("11", "09");

		String fileName = "Consolidado_" + dateBeforeFormated + "_a_" + dateFormated + ".txt";

		processar(daesParaBuscar, fileName);

	}

	private void processarPagamentoDaeLicenciamentoDiario(List<DaeLicenciamento> daesParaBuscar) {

		Logger.info("=== PAGAMENTO DAE LICENÇAS EMITIDAS DIÁRIO - VERIFICA SE PAGAMENTO FOI REALIZADO NA SEFAZ === ");

		String dateFormated = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
//		TODO: deixar aqui para teste
//		dateFormated = dateFormated.replace("11", "09");
		String fileName = "pagamentoDarAvulso" + dateFormated + ".txt";

		processar(daesParaBuscar, fileName);

	}

	private void processar(List<DaeLicenciamento> daesParaBuscar, String fileName) {

		buscaArquivoSFTP(fileName);

		List<DaePagoVO> daePagoVOList = lerArquivoParaVO(fileName);

		daesParaBuscar.forEach(dae -> {

			try {

				dae.processarPagamentosefaz(daePagoVOList);

				commitTransaction();

				Logger.info("ProcessamentoDaeLicenciamento:: Finalizado DAE id " + dae.id + " - Status DAE: " + dae.status.name() + " - Status caracterização: " + dae.caracterizacao.status.nome);

			} catch (Exception e) {

				Logger.error(e, e.getMessage());
				rollbackTransaction();
			}

		});

	}

	private List<DaePagoVO> lerArquivoParaVO(String fileName) {

		List<DaePagoVO> daePagoVOList = new ArrayList<>();

		Path path = Paths.get(APPLICATION_TEMP_FOLDER + fileName);
		try (Stream<String> lines = Files.lines(path)) {
			lines.forEach(s -> {

				String[] dados = s.split(";");

				if(dados.length > 3) {
					if (checkCodigoLicencaSefaz(dados[4])) {
						daePagoVOList.add(new DaePagoVO(dados));
					}
				}

			});
		} catch (IOException e) {

			Logger.error(e, e.getMessage());
			rollbackTransaction();

		}

		return daePagoVOList;

	}

	private Boolean checkCodigoLicencaSefaz(String codigoLicencaSefaz) {

		if ((codigoLicencaSefaz.equals(SEFAZ_CODIGO_TAXA_LICENCA_PREVIA)) ||
		   (codigoLicencaSefaz.equals(SEFAZ_CODIGO_TAXA_LICENCA_INSTALACAO)) ||
		   (codigoLicencaSefaz.equals(SEFAZ_CODIGO_TAXA_LICENCA_OPERACAO)) ||
		   (codigoLicencaSefaz.equals(SEFAZ_CODIGO_TAXA_RENOVACAO_LICENCA_INSTALACAO)) ||
		   (codigoLicencaSefaz.equals(SEFAZ_CODIGO_TAXA_RENOVACAO_LICENCA_OPERACAO))) {

			return true;

		}

		return false;

	}

	private void buscaArquivoSFTP(String fileName) {

		try {
			webService.whenDownloadFileUsingSshj_thenSuccess(fileName);
		} catch (IOException e) {

			Logger.error(e, e.getMessage());
			rollbackTransaction();
		}

	}

	private List<DaeLicenciamento> buscaDaesLicenciamentoParaProcessar() {

		List<DaeLicenciamento> daesPendente = DaeLicenciamento.findByStatus(DaeLicenciamento.Status.EMITIDO);
		List<DaeLicenciamento> daesVencidoAguardandoPagamento = DaeLicenciamento.findByStatus(DaeLicenciamento.Status.VENCIDO_AGUARDANDO_PAGAMENTO);

		List<DaeLicenciamento> daesParaBuscar = new ArrayList<>();
		daesParaBuscar.addAll(daesPendente);
		daesParaBuscar.addAll(daesVencidoAguardandoPagamento);

		return daesParaBuscar;

	}

	private void processarPagamentoDae(DaeLicenciamento daeLicenciamento) {

		try {

			Logger.info("ProcessamentoDaeLicenciamento:: Processando DAE id " + daeLicenciamento.id +
					" - Status DAE: " + daeLicenciamento.status.name() + " - Status caracterização: " +
					daeLicenciamento.caracterizacao.status.nome + " id caracterizacao :" + daeLicenciamento.caracterizacao.id);

			if(Configuracoes.VERIFICAR_PAGAMENTO_GESTAO_PAGAMENTO) {
				Logger.info("=== PAGAMENTO DAE EMITIDAS - VERIFICA SE PAGAMENTO FOI REALIZADO NO GESTAO PAGAMENTOS === ");
				daeLicenciamento.processarPagamento();
			} else {
				Logger.info("=== PAGAMENTO DAE EMITIDAS - SEM VERIFICACAO NO GESTAO PAGAMENTOS === ");
				daeLicenciamento.processaPagamentoSemVerificarGestaoPagamentos();
			}

			commitTransaction();

			Logger.info("ProcessamentoDaeLicenciamento:: Finalizado DAE id " + daeLicenciamento.id + " - Status DAE: " + daeLicenciamento.status.name() + " - Status caracterização: " + daeLicenciamento.caracterizacao.status.nome);

		} catch (Exception e) {

			Logger.error(e, e.getMessage());
			rollbackTransaction();
		}

	}

}
