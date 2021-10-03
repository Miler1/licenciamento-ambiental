package utils;

import com.vividsolutions.jts.geom.Geometry;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.GeoTools;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.spatial.Intersects;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class WFSGeoserver {

	private DataStore data;

	public WFSGeoserver(String urlGetCapabilities) throws IOException {

		Map<String, Object> connectionParameters = new HashMap<String, Object>();

		//parametros de configuração do WFS
		connectionParameters.put("WFSDataStoreFactory:GET_CAPABILITIES_URL", urlGetCapabilities );
		connectionParameters.put("WFSDataStoreFactory:PROTOCOL", true );
		connectionParameters.put("WFSDataStoreFactory:LENIENT", true );
		connectionParameters.put("WFSDataStoreFactory:WFS_STRATEGY", "geoserver" );
		connectionParameters.put("WFSDataStoreFactory:TIMEOUT", 1000000000 );
		connectionParameters.put("WFSDataStoreFactory:TRY_GZIP", false );

		//pegar a conexão do geoserver
		//aqui posso olhar quais as camadas tem o geoserver
		data = DataStoreFinder.getDataStore( connectionParameters );

	}
	
	private Intersects getIntersectsFilter(String nomeLayer, Geometry geometry) throws IOException{

		//pegar a camada com o nome nomeLayer
		SimpleFeatureType schema = data.getSchema( nomeLayer );

		//montar filtro
		FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2( GeoTools.getDefaultHints());
		//uso boundary box para aumentar o desempenho quando consulto o banco
		Intersects filter = ff.intersects(
				ff.property( schema.getGeometryDescriptor().getLocalName() ),
				ff.literal( geometry.getEnvelope() )
		);
		
		return filter;
	}

	public FeatureCollection<SimpleFeatureType, SimpleFeature> intersects(String nomeLayer, Geometry geometry) throws IOException {

		//pegar o nome da geometria default
		FeatureSource<SimpleFeatureType, SimpleFeature> source = data.getFeatureSource( nomeLayer );
		
		DefaultFeatureCollection featureCollectionRetorno = new DefaultFeatureCollection(nomeLayer, source.getSchema());

		Intersects intersectsFilter = getIntersectsFilter(nomeLayer, geometry);
		
		FeatureIterator<SimpleFeature> iterator = source.getFeatures(intersectsFilter).features();

		while (iterator.hasNext()){

			SimpleFeature featureTestada = iterator.next();

			if(geometry.intersects((Geometry) featureTestada.getDefaultGeometry())){

				featureCollectionRetorno.add(featureTestada);

			}

		}

		return featureCollectionRetorno;
	}
	
	public FeatureCollection<SimpleFeatureType, SimpleFeature> contains(String nomeLayer, Geometry geometry) throws IOException {
		
		//pegar o nome da geometria default
		FeatureSource<SimpleFeatureType, SimpleFeature> source = data.getFeatureSource( nomeLayer );
		
		DefaultFeatureCollection featureCollectionRetorno = new DefaultFeatureCollection(nomeLayer, source.getSchema());
		
		Intersects intersectsFilter = getIntersectsFilter(nomeLayer, geometry);
		
		FeatureIterator<SimpleFeature> iterator = source.getFeatures(intersectsFilter).features();

		while (iterator.hasNext()){

			SimpleFeature featureTestada = iterator.next();
			
			Geometry featurePolygon = (Geometry) featureTestada.getDefaultGeometry();
			
			if(featurePolygon.contains(geometry)){

				featureCollectionRetorno.add(featureTestada);
			}
		}

		return featureCollectionRetorno;
	}


	public FeatureCollection<SimpleFeatureType, SimpleFeature> getLayer(String nomeLayer) throws IOException {
		FeatureSource<SimpleFeatureType, SimpleFeature> source = data.getFeatureSource(nomeLayer);
		return source.getFeatures();
	}
}
