package controllers;

import models.Esfera;

public class Esferas extends InternalController {

    public static void list() {
    	renderJSON(Esfera.values());
    }

}
