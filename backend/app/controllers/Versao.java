package controllers;

import play.Play;

public class Versao extends InternalController {

	public static void versao() {
		Play.configuration.setProperty("versao.play", Play.frameworkPath.getName());
		render();
	}
}
