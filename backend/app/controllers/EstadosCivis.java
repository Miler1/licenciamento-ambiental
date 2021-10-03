package controllers;

import models.EstadoCivil;
import serializers.EstadoCivilSerializer;

public class EstadosCivis extends InternalController {

    public static void list() {
    	renderJSON(EstadoCivil.findAll(), EstadoCivilSerializer.findAll);
    }

}
