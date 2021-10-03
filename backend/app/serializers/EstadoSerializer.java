package serializers;

import com.vividsolutions.jts.geom.Geometry;

import flexjson.JSONSerializer;
import utils.SerializerUtil;
import utils.flexjson.GeometryTransformer;

public class EstadoSerializer {
	
	public static JSONSerializer getEstadoGeometryByCodigo = SerializerUtil.create(
			"codigo",
			"nome",
			"limite").transform(new GeometryTransformer(), Geometry.class);

}
