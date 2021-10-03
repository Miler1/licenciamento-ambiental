package controllers;

import java.util.List;

import com.vividsolutions.jts.geom.Geometry;
import models.Municipio;
import serializers.MunicipiosSerializer;

public class Municipios extends InternalController {

	public static void listByEstado(String uf) {
		
		List<Municipio> municipios = Municipio.findByEstado(uf);
		renderJSON(municipios, MunicipiosSerializer.findByEstado);
	}
	
	public static void getMunicipioGeometryById(Long id) {
		
		Municipio municipio = Municipio.findById(id);

		renderJSON(municipio, MunicipiosSerializer.getMunicipioGeometryById);
		
	}
}
