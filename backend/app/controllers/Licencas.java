package controllers;

import java.util.List;

import models.caracterizacao.Licenca;

public class Licencas extends InternalController {

	public static void gerarPdfLicencas(List<Long> idsLicencas) throws Exception {
		
		Licenca.gerarPdfLicencas(idsLicencas);
		
		ok();
	}

	public static void prorrogar(Long id) throws Exception {

		notFoundIfNull(id);

		Licenca licenca = Licenca.findById(id);

		licenca.prorrogar();

		ok();
	}

	public static void finalizarProrrogacao(List<Long> ids) throws Exception {

		notFoundIfNull(ids);

		Licenca.finalizarProrrogacao(ids);

		ok();
	}
}
