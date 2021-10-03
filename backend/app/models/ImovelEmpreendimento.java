package models;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.codec.binary.Base64;
import org.geotools.referencing.CRS;
import org.hibernate.annotations.Type;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

import br.ufla.tmsmap.Format;
import br.ufla.tmsmap.JTSLayer;
import br.ufla.tmsmap.PolygonStyle;
import br.ufla.tmsmap.Style;
import br.ufla.tmsmap.TMSLayer;
import br.ufla.tmsmap.TMSMap;
import models.sicar.ImovelSicar;
import models.sicar.SicarWebService;
import play.Play;
import play.data.validation.Required;
import play.db.jpa.GenericModel;
import utils.GeoJsonUtils;

@Entity
@Table(schema = "licenciamento", name = "imovel_empreendimento")
public class ImovelEmpreendimento extends GenericModel {
	
	private static final String SEQ = "licenciamento.imovel_empreendimento_id_seq";
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
	@SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
	public Long id;
	
	@Required
	@Column(name = "codigo_imovel")
	public String codigo;
	
	@Column(name = "id_imovel_car")
	public Long idCar;
	
	public String nome;
	
	@Column(name = "the_geom", columnDefinition="Geometry")
	public Geometry limite;
	
	@ManyToOne
	@JoinColumn(name="id_municipio", referencedColumnName="id_municipio")
	public Municipio municipio;
	
	@OneToOne
	@JoinColumn(name = "id_empreendimento", referencedColumnName = "id", nullable = false)
	public Empreendimento empreendimento;
	
	@Column(name = "data_cadastro")
	public Date dataCadastro;
	
	@Column(name = "area_total")
	public Double areaTotal;
	
	@Column(name = "area_app")
	public Double areaAPP;
	
	@Column(name = "area_reserva_legal")
	public Double areaReservaLegal;
	
	@Column(name = "area_uso_alternativo_solo")
	public Double areaUsoAlternativoSolo;
	
	@Transient
	public String imagemMapa;
	
	public ImovelEmpreendimento() {
		
	}
	
	public ImovelEmpreendimento(ImovelSicar imovelSicar) {
		
		this.idCar = imovelSicar.id;
		this.codigo = imovelSicar.codigo;
		this.nome = imovelSicar.nome;
		this.municipio = imovelSicar.municipio;
		
		if (imovelSicar.geo != null)
			this.limite = GeoJsonUtils.toGeometry(imovelSicar.geo);
		
		try {
			this.dataCadastro = new SimpleDateFormat("dd/MM/yyyy").parse(imovelSicar.dataCriacao);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		this.areaTotal = imovelSicar.areaImovel;
		this.areaAPP = imovelSicar.areaAPP;
		this.areaReservaLegal = imovelSicar.areaReservaLegal;
		this.areaUsoAlternativoSolo = imovelSicar.areaUsoAlternativoSolo;
	}
	
	private class TemaVO {
		public Object geo;
		public Object tema;
	}

	public static ImovelEmpreendimento getImovelBy(String codigo){

		return ImovelEmpreendimento.find("codigo", codigo).first();

	}
	
	private Geometry getTemaGeometry(String tema) {
		
		List<String> temas = new ArrayList<>();
		temas.add(tema);
		
		JsonElement jsonElement = new SicarWebService().getTemasByImovel(this.codigo, temas);
		JsonArray jsonArray = jsonElement.getAsJsonArray();		
		
		List<TemaVO> temasVO = new ArrayList<>();
		
		for(JsonElement json : jsonArray) {
			temasVO.add(new Gson().fromJson(json, TemaVO.class));
		}
		
		Geometry geometry = null;
		
		for(TemaVO temaVo : temasVO) {
			
			if(geometry == null)
				geometry = GeoJsonUtils.toGeometry(temaVo.geo.toString());
			else
				geometry = geometry.union(GeoJsonUtils.toGeometry(temaVo.geo.toString()));
			
		}
		
		return geometry;
		
	}
	
	public String getMapaBase64() throws MalformedURLException, UnsupportedEncodingException {

		int width = Integer.valueOf(Play.configuration.getProperty("sistema.recibo.mosaico.width", "709")),
				height= Integer.valueOf(Play.configuration.getProperty("sistema.recibo.mosaico.height", "354"));
//		String path = Play.configuration.getProperty("sistemas.recibo.mosaico", "http://www.car.gov.br/mosaicos/{z}/{x}/{y}.jpg");

		TMSMap map = new TMSMap(0);

		map.addLayer(TMSLayer.from(new URL("http://www.car.gov.br/mosaicos/{z}/{x}/{y}.jpg")));		
		//map.addLayer(TMSLayer.from(new File(path)));

		float[] colorAreaImovel = Color.RGBtoHSB(255, 99, 71, null);
		
		Color corAI = Color.getHSBColor(colorAreaImovel[0], colorAreaImovel[1], colorAreaImovel[2]);
		
		Style areaImovelStyle = new PolygonStyle()
				.fillColor(corAI)
				.fillOpacity(0.2f)
				.color(corAI)
				.width(2)
				.opacity(1f);
		
		float[] fillColorAPP = Color.RGBtoHSB(255, 251, 0, null);
		
		Style APPStyle = new PolygonStyle()
				.fillColor(Color.getHSBColor(fillColorAPP[0], fillColorAPP[1], fillColorAPP[2]))
				.fillOpacity(0.5f)
				.opacity(1f);
		
		float[] fillColorRL = Color.RGBtoHSB(51, 159, 49, null);
		float[] colorRL = Color.RGBtoHSB(168, 96, 21, null);
		
		Style RLStyle = new PolygonStyle()
				.fillColor(Color.getHSBColor(fillColorRL[0], fillColorRL[1], fillColorRL[2]))
				.fillOpacity(0.5f)
				.color(Color.getHSBColor(colorRL[0], colorRL[1], colorRL[2]))
				.width(2)
				.opacity(1f);
		
		CoordinateReferenceSystem crsDefault = null;
		
		try {
			crsDefault = CRS.parseWKT(Play.configuration.getProperty("CRS:DEFAULT"));
		} catch (FactoryException e) {
			throw new RuntimeException(e);
		}

		map.addLayer(JTSLayer.from(crsDefault, areaImovelStyle, this.limite));
		map.addLayer(JTSLayer.from(crsDefault, APPStyle, getTemaGeometry("APP_TOTAL")));
		map.addLayer(JTSLayer.from(crsDefault, RLStyle, getTemaGeometry("ARL_TOTAL")));

		Envelope envelope = this.limite.getEnvelopeInternal();

		map.zoomTo(envelope, width, height, 0, 14, 256, 256);

		map.padding(map.getZoom(), map.getZoom());

		ByteArrayOutputStream out = new ByteArrayOutputStream();

		map.render(width, height, Format.PNG, out);

		return "data:image/png;base64," + Base64.encodeBase64String(out.toByteArray());
	}
}
