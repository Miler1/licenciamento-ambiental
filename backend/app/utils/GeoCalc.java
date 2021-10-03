package utils;

import beans.CamadaSobreposicaoVO;
import beans.DadosSobreposicaoVO;
import beans.ResultadoSobreposicaoCamadaVO;
import br.ufla.tmsmap.T2;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.index.strtree.STRtree;
import models.caracterizacao.TipoSobreposicao;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geometry.jts.Geometries;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import play.Play;

import java.io.IOException;
import java.io.StringWriter;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.Double.parseDouble;

/**
 * Created by rthoth on 21/05/15.
 * Edited by Julio Cezar on 19/09/17
 */
public class GeoCalc {

	protected static final STRtree index = new STRtree(2);
	protected static CoordinateReferenceSystem CRS_DEFAULT = null;
	protected static SimpleFeatureType featureType;

	static {
		String key, value;
		int indexOf;

		try {
			CRS_DEFAULT = CRS.parseWKT(Play.configuration.getProperty("CRS:DEFAULT"));
		} catch (FactoryException e) {
			throw new RuntimeException(e);
		}

		for (Entry<Object, Object> entry : Play.configuration.entrySet()) {
			key = (String) entry.getKey();
			if (key.startsWith("crs.")) {
				value = (String) entry.getValue();
				indexOf = value.indexOf(' ');
				String[] envelopeStrings = value.substring(0, indexOf).split(",");

				if (envelopeStrings.length == 4) {
					String wkt = value.substring(indexOf);
					double minx, miny, maxx, maxy;
					minx = parseDouble(envelopeStrings[0]);
					miny = parseDouble(envelopeStrings[1]);
					maxx = parseDouble(envelopeStrings[2]);
					maxy = parseDouble(envelopeStrings[3]);

					Envelope envelope = new Envelope(minx, maxx, miny, maxy);
					try {
						addCRS(envelope, wkt);
					} catch (Exception e) {

					}
				}
			}
		}
		
		SimpleFeatureTypeBuilder builderType = new SimpleFeatureTypeBuilder();
		
		builderType.setName("retorno");
		builderType.setCRS(Configuracoes.CRS_DEFAULT);
		builderType.add("geometria", Geometry.class);
		builderType.add("descricao", String.class);
		builderType.setDefaultGeometry("geometria");
		
		featureType = builderType.buildFeatureType();
	}

	private GeoCalc() {

	}

	public static void addCRS(Envelope envelope, String wkt) {
		if (envelope == null)
			throw new IllegalArgumentException("Envelope nulo para " + wkt);

		CoordinateReferenceSystem crs;
		try {
			crs = CRS.parseWKT(wkt);
		} catch (FactoryException e) {
			throw new IllegalArgumentException(wkt, e);
		}

      	//Adding the envelopes and crs to the index tree
		index.insert(envelope, new T2<>(envelope, crs));
	}

	public static double area(Geometry geometry) {
		CoordinateReferenceSystem crs = detectCRS(geometry);
		if (crs == null)
			throw new UnsupportedOperationException();

		return area(geometry, crs);
	}

	public static double area(Geometry geometry, CoordinateReferenceSystem crs) {

		MathTransform mathTransform;
		try {
			mathTransform = CRS.findMathTransform(CRS_DEFAULT, crs, true);
		} catch (FactoryException e) {
			throw new RuntimeException(e);
		}

		try {
			return JTS.transform(geometry, mathTransform).getArea();
		} catch (TransformException e) {
			throw new RuntimeException(e);
		}
	}

	public static CoordinateReferenceSystem detectCRS(Geometry geometry) {
		
		Envelope geometryEnvelope = geometry.getEnvelopeInternal();
		
		List objects = index.query(geometryEnvelope);
		CoordinateReferenceSystem crsFinal = null;
		
		//If the size of objects is equal to 1 there is no need to do anything else, just return the object. 
      	if(objects.size() == 1 || Geometries.get(geometry) == Geometries.POINT || Geometries.get(geometry) == Geometries.LINESTRING)
        	return ((T2<Envelope, CoordinateReferenceSystem>) objects.get(0))._2;
		
		double maxArea = 0.0;

		for (int i = 0; i < objects.size(); i++) {
			
			T2<Envelope, CoordinateReferenceSystem> tuple =  (T2<Envelope, CoordinateReferenceSystem>) objects.get(i);
			
          	//Getting the geometry of an envelope to use later
          	Geometry crsGeometry = geometry.getFactory().toGeometry(tuple._1);
          	
			//Calculate the intersection perimeter between geometry and crsGeometry
          	double envelopeArea = geometry.intersection(crsGeometry).getLength();
			
          	// Check if the envelopeArea is larger than the last maxArea
			if(envelopeArea > maxArea) {
				maxArea = envelopeArea;
				crsFinal = tuple._2;
			}
				
		}

		return crsFinal;
	}
	
	public static String filterCollectionToJson(FeatureCollection featureCollection) throws IOException{
		
		FeatureJSON io = new FeatureJSON();

		StringWriter writer = new StringWriter();
		
		io.writeFeatureCollection(featureCollection, writer);

		return writer.toString();		
	}
	
	public static String getCamadaGeoServerWithIntersection(String urlGeoserver, String nomeCamada, String descricaoCamada, Geometry geometria) throws IOException  {
		
		DefaultFeatureCollection featureCollection = new DefaultFeatureCollection(null, featureType);
		
		WFSGeoserver wfs = new WFSGeoserver(urlGeoserver);

		FeatureCollection<SimpleFeatureType, SimpleFeature> features;

		features =  wfs.intersects(nomeCamada, geometria);

		FeatureIterator<SimpleFeature> interator = features.features();

		while(interator.hasNext()){

			SimpleFeature feature = interator.next();
							
			Geometry featurePolygon = (Geometry) feature.getDefaultGeometry();
			
			Geometry intersection = featurePolygon.intersection(geometria);
			
			SimpleFeatureBuilder builder = new SimpleFeatureBuilder(featureType);

			builder.set("geometria", intersection);
			builder.set("descricao", descricaoCamada);
							
			featureCollection.add(builder.buildFeature(feature.getID()));
		}
		
		return filterCollectionToJson(featureCollection);
	}

	public static List<DadosSobreposicaoVO> getInformacoesSobreposicoes(ResultadoSobreposicaoCamadaVO sobreposicao, Geometry geometria){

		List<DadosSobreposicaoVO> dadosSobreposicao = new ArrayList<>();
		DadosSobreposicaoVO dadoSobreposicao;

		FeatureIterator<SimpleFeature> interator = sobreposicao.featureCollection.features();

		while(interator.hasNext()){

			SimpleFeature feature = interator.next();

			Geometry featurePolygon = (Geometry) feature.getDefaultGeometry();

			Geometry intersection = featurePolygon.intersection(geometria);

			if(!intersection.isEmpty()) {
				dadoSobreposicao = new DadosSobreposicaoVO();
				// Convertendo geometria para projeção 2D.
				for(Coordinate c : intersection.getCoordinates()) {
					c.setCoordinate(new Coordinate(c.x, c.y));
				}

				String colunaNome = (sobreposicao.tipoSobreposicao.colunaNome == null ? "" : sobreposicao.tipoSobreposicao.colunaNome);
				String colunaData = (sobreposicao.tipoSobreposicao.colunaData == null ? "" : sobreposicao.tipoSobreposicao.colunaData);
				String colunaCpfCnpj = (sobreposicao.tipoSobreposicao.colunaCpfCnpj == null ? "" : sobreposicao.tipoSobreposicao.colunaCpfCnpj);

				dadosSobreposicao.add(new DadosSobreposicaoVO(intersection, feature.getAttribute(colunaNome), feature.getAttribute(colunaData), feature.getAttribute(colunaCpfCnpj)));
			}
		}
		return dadosSobreposicao;
	}

	public static boolean containsGeometry(String urlGeoserver, String nomeCamada, Geometry geometria) throws IOException {
		
		WFSGeoserver wfs = new WFSGeoserver(urlGeoserver);
		
		FeatureCollection<SimpleFeatureType, SimpleFeature> featureCollection = wfs.contains(nomeCamada, geometria);
		
		return featureCollection.size() > 0;
	}
	
	public static FeatureCollection<SimpleFeatureType, SimpleFeature> getCamadaGeoServerContains(String urlGeoserver, String nomeCamada, Geometry geometria) throws IOException {
		
		WFSGeoserver wfs = new WFSGeoserver(urlGeoserver);
		
		return wfs.contains(nomeCamada, geometria);
	}

	public static FeatureCollection<SimpleFeatureType, SimpleFeature> getCamadaGeoServerIntersects(String urlGeoserver, String nomeCamada, Geometry geometria) throws IOException {

		WFSGeoserver wfs = new WFSGeoserver(urlGeoserver);

		return wfs.intersects(nomeCamada, geometria);
	}

	public static List<ResultadoSobreposicaoCamadaVO> getSobreposicoesGeoserver(Geometry geometria, ArrayList<CamadaSobreposicaoVO> camadas) {

		List<ResultadoSobreposicaoCamadaVO> resultadosSobreposicao = new ArrayList<>();
		for(CamadaSobreposicaoVO camada : camadas) {
			FeatureCollection featureCollectionAtual = null;
			try {
				featureCollectionAtual = GeoCalc.getCamadaGeoServerIntersects(Configuracoes.GEOSERVER_GETCAPABILITIES, camada.camada, geometria);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(featureCollectionAtual.size() > 0) {
				resultadosSobreposicao.add(new ResultadoSobreposicaoCamadaVO(featureCollectionAtual, camada.tipoSobreposicao));
			}
		}

		return resultadosSobreposicao;
	}

	public static Geometry transform(Geometry geometry, CoordinateReferenceSystem crs) {

		MathTransform mathTransform;
		try {
			mathTransform = CRS.findMathTransform(CRS_DEFAULT, crs, true);
		} catch(FactoryException e) {
			throw new RuntimeException(e);
		}

		try {
			return JTS.transform(geometry, mathTransform);
		} catch(TransformException e) {
			throw new RuntimeException(e);
		}
	}

	public static double distance(Geometry g1, Geometry g2) {
		return transform(g1, detectCRS(g1)).distance(transform(g2, detectCRS(g1)));
	}

	public static Double getDistanceGeoserver(String nomeLayer, Geometry geometry) {
		try {
			WFSGeoserver wfs = new WFSGeoserver(Configuracoes.GEOSERVER_GETCAPABILITIES);

			FeatureCollection featureCollectionAtual = wfs.getLayer(nomeLayer);

			return dist(featureCollectionAtual,geometry);

		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	public static Double dist(FeatureCollection features, Geometry geometria){

		List<Double> dists = new ArrayList<>();
		FeatureIterator<SimpleFeature> interator = features.features();

		while(interator.hasNext()){
			dists.add(distance(geometria,(Geometry) interator.next().getDefaultGeometry()));
		}
		return dists.stream().min(Double::compare).orElse(0.0);
	}

	public static List<Geometry> getGeometries(Geometry geometry){
		if(geometry.getGeometryType().equals("GeometryCollection")) {
			return IntStream.range(0, geometry.getNumGeometries()).mapToObj(geometry::getGeometryN)
					.peek(g -> g.setSRID(Configuracoes.SRID)).collect(Collectors.toList());
		}
		geometry.setSRID(Configuracoes.SRID);
		return Collections.singletonList(geometry);
	}
}
