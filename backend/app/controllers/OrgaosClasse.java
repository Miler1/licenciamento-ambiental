package controllers;

import models.OrgaoClasse;

public class OrgaosClasse extends InternalController {
	
	public static void list() {
		
		renderJSON(OrgaoClasse.findAll());
		
	}

}
