package models.sicar;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import com.vividsolutions.jts.geom.Geometry;

import utils.GeoCalc;

public class SobreposicoesSicar {
	
	public static final String UNIDADE_CONSERVACAO = "UNIDADE_CONSERVACAO";
	public static final String TERRA_INDIGENA = "TERRA_INDIGENA";
	public static final String AREA_MILITAR = "AREA_MILITAR";
	public static final String ZONA_CONSOLIDACAO = "ZONA_CONSOLIDACAO";
	public static final String AREA_QUILOMBOLA = "AREA_QUILOMBOLA";
	public static final String AREA_CONSOLIDADA = "AREA_CONSOLIDADA";
	
	List<SobreposicaoSicar> sobreposicoes;
	
	public boolean possuiSobreposicaoComTema(String tema) {
		
		if(sobreposicoes != null) {
			
			for (SobreposicaoSicar sobreposicaoSicar : sobreposicoes) {
				if(sobreposicaoSicar.nome.equals(tema) &&
					!sobreposicaoSicar.areasSobrepostas.isEmpty()){
					return true;
				}
			}
			
		}
		
		return false;
		
	}
	
	public SobreposicoesSicar processaSobreposicaoAreaConsolidada(Geometry geometria) {
		
		if(possuiSobreposicaoComTema(AREA_CONSOLIDADA)) {
		
			SobreposicaoSicar sobreposicao = this.getByName(AREA_CONSOLIDADA);
			
			if(sobreposicao.areasSobrepostas.equals("Sobreposta")) {
				return this;
			}
			
			String areaSemHa = sobreposicao.areasSobrepostas.replace(",", ".").replace(" ha", "");
			Double areaSobreposicao = Double.parseDouble(areaSemHa);
			Double areaGeometria = GeoCalc.area(geometria)/10000;

			NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
			DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
			decimalFormat.applyPattern("#.0000");
			String areaFormatada = decimalFormat.format(areaGeometria);
			
			areaGeometria = Double.parseDouble(areaFormatada);
			
			if(Double.compare(areaGeometria, areaSobreposicao) != 0)
				this.sobreposicoes.remove(sobreposicao);
		}
		
		return this;
		
	}
	
	public void setSobreposicoes(List<SobreposicaoSicar> sobreposicoes) {
		
		this.sobreposicoes = sobreposicoes;
	}
	
	public SobreposicoesSicar() {
		
	}
	
	
	public class SobreposicaoSicar {
		
		public String nome;
		
		public String areasSobrepostas;

	}
	
	public SobreposicaoSicar getByName(String name) {
		
		for(SobreposicaoSicar sobreposicao : this.sobreposicoes) {
			if(sobreposicao.nome.equals(name)) {
				return sobreposicao;
			}
		}
		
		return null;
		
	}

}
