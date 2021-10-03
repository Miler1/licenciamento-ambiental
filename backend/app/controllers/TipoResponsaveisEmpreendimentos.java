package controllers;

import models.TipoResponsavelEmpreendimento;

public class TipoResponsaveisEmpreendimentos extends InternalController {
	
	public static void list() {
		
		renderJSON(TipoResponsavelEmpreendimento.getAllTiposResponsaveisWithNomes());

	}

}
