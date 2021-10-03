package controllers;

import models.Estado;
import serializers.EstadoSerializer;

public class Estados extends InternalController {

    public static void list() {
    	renderJSON(Estado.findAll());
    }
    
    public static void getEstadoGeometryByCodigo(String codigo) {    	
    	
    	Estado estado = Estado.find("cod_estado", codigo).first();
    	renderJSON(estado, EstadoSerializer.getEstadoGeometryByCodigo);
    }

}
