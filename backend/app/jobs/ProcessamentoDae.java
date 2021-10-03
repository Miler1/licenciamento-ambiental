package jobs;

import models.caracterizacao.Dae;
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

/**
 * Job que verifica o pagamento de DAEs".
 */
@On("cron.processamentoDaes")
@OnApplicationStart
public class ProcessamentoDae extends GenericJob {

	private static final int TERCA_FEIRA = 3;
	private final static String APPLICATION_TEMP_FOLDER =
			Play.applicationPath + File.separator + Play.configuration.getProperty("path.arquivos.tempFolder")+"DAE/";
	private static final String SEFAZ_CODIGO_TAXA_EXPEDIENTE = Play.configuration.getProperty("sefaz.codigo.taxa.expediente");
	private static final String SEFAZ_CODIGO_TAXA_EXPEDIENTE_DISPENSA = Play.configuration.getProperty("sefaz.codigo.taxa.expediente.dispensa");
	private static final String SEFAZ_CODIGO_TAXA_EXPEDIENTE_LICENCA = Play.configuration.getProperty("sefaz.codigo.taxa.expediente.licenca");
	private static WebServiceSftpSEFAZ webService = new WebServiceSftpSEFAZ();

	@Override
	public void executar() {

		Logger.info("ProcessamentoDae:: Iniciando job");

		List<Dae> daesParaBuscar = buscaDaesParaProcessar();

		if(!daesParaBuscar.isEmpty()) {

			processarPagamentoDaeDiario(daesParaBuscar);

		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int day = cal.get(Calendar.DAY_OF_WEEK);

		if(day == TERCA_FEIRA && !daesParaBuscar.isEmpty()) {

			daesParaBuscar = buscaDaesParaProcessar();
			processarPagamentoDaeSemanal(daesParaBuscar);

		}

		//TODO: Descomentar quando for voltar o pagamento para o Gestão de Pagamentos
//		for(Dae dae : daesParaBuscar) {
//
//			daesParaBuscar = buscaDaesParaProcessar();
//			processarPagamentoDae(dae);
//
//		}

		Logger.info("ProcessamentoDae:: Encerrando job");
	}

	private List<Dae> buscaDaesParaProcessar() {

		List<Dae> daesPendente = Dae.findByStatus(Dae.Status.EMITIDO);
		List<Dae> daesVencidoAguardandoPagamento = Dae.findByStatus(Dae.Status.VENCIDO_AGUARDANDO_PAGAMENTO);

		List<Dae> daesParaBuscar = new ArrayList<>();
		daesParaBuscar.addAll(daesPendente);
		daesParaBuscar.addAll(daesVencidoAguardandoPagamento);

		return daesParaBuscar;

	}

	private void processarPagamentoDaeDiario(List<Dae> daesParaBuscar) {

		Logger.info("=== PAGAMENTO DAE EMITIDAS DIÁRIO - VERIFICA SE PAGAMENTO FOI REALIZADO NA SEFAZ === ");

		String dateFormated = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
//		TODO: deixar aqui para teste
//		dateFormated = dateFormated.replace("11", "09");
		String fileName = "pagamentoDarAvulso" + dateFormated + ".txt";

		processar(daesParaBuscar, fileName);

	}


	private void processarPagamentoDaeSemanal(List<Dae> daesParaBuscar) {

		Logger.info("=== PAGAMENTO DAE EMITIDAS SEMANAL(ÚLTIMOS 6 MÊSES) - VERIFICA SE PAGAMENTO FOI REALIZADO NA SEFAZ === ");

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

	private void processar(List<Dae> daesParaBuscar, String fileName) {

		buscaArquivoSFTP(fileName);

		List<DaePagoVO> daePagoVOList = lerArquivoParaVO(fileName);

		daesParaBuscar.forEach(dae -> {

			try {

				dae.processarPagamentosefaz(daePagoVOList);

				commitTransaction();

				Logger.info("ProcessamentoDae:: Finalizado DAE id " + dae.id + " - Status DAE: " + dae.status.name() + " - Status caracterização: " + dae.caracterizacao.status.nome);

			} catch (Exception e) {

				Logger.error(e, e.getMessage());
				rollbackTransaction();
			}

		});
	}

	private void buscaArquivoSFTP(String fileName) {

		try {
			webService.whenDownloadFileUsingSshj_thenSuccess(fileName);
		} catch (IOException e) {

			Logger.error(e, e.getMessage());
			rollbackTransaction();
		}

	}

	private List<DaePagoVO> lerArquivoParaVO(String fileName) {

		List<DaePagoVO> daePagoVOList = new ArrayList<>();

		Path path = Paths.get(APPLICATION_TEMP_FOLDER + fileName);
		try (Stream<String> lines = Files.lines(path)) {
			lines.forEach(s -> {

				String[] dados = s.split(";");

				if(dados.length > 3) {
					if (dados[4].equals(SEFAZ_CODIGO_TAXA_EXPEDIENTE_DISPENSA) || dados[4].equals(SEFAZ_CODIGO_TAXA_EXPEDIENTE_LICENCA)) {
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

	private void processarPagamentoDae(Dae dae) {

		try {

			Logger.info("ProcessamentoDae:: Processando DAE id " + dae.id + " - Status DAE: " + dae.status.name() + " - Status caracterização: " + dae.caracterizacao.status.nome);

			if(Configuracoes.VERIFICAR_PAGAMENTO_GESTAO_PAGAMENTO) {
				Logger.info("=== PAGAMENTO DAE EMITIDAS - VERIFICA SE PAGAMENTO FOI REALIZADO NO GESTAO PAGAMENTOS === ");
				dae.processarPagamento();
			} else {
				Logger.info("=== PAGAMENTO DAE EMITIDAS - SEM VERIFICACAO NO GESTAO PAGAMENTOS === ");
				dae.processaPagamentoSemVerificarGestaoPagamentos();
			}

			commitTransaction();

			Logger.info("ProcessamentoDae:: Finalizado DAE id " + dae.id + " - Status DAE: " + dae.status.name() + " - Status caracterização: " + dae.caracterizacao.status.nome);

		} catch (Exception e) {

			Logger.error(e, e.getMessage());
			rollbackTransaction();
		}

	}

}
