package controllers;

import models.analise.DlaCancelada;

public class DlasCanceladas extends InternalController {

	public static void cancelar(DlaCancelada dla) {

		dla.cancelarDla(dla.usuario);

		ok();
	}

}
