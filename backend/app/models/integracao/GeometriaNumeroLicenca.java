package models.integracao;

import com.vividsolutions.jts.geom.Geometry;

public class GeometriaNumeroLicenca {
	
	public String CodigoLar;
	public Geometry geometry;
	
	public GeometriaNumeroLicenca() {
		
	}
	
	public GeometriaNumeroLicenca(String codigoLar, Geometry geometry) {
		
		this.CodigoLar = codigoLar;
		this.geometry = geometry;
	}
}
