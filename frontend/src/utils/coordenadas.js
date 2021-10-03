;(function(proj4){

	//SAD 69 - UTM
	proj4.defs('EPSG:29187', '+proj=utm +zone=17 +south +ellps=aust_SA +units=m +no_defs');
	proj4.defs('EPSG:29188', '+proj=utm +zone=18 +south +ellps=aust_SA +units=m +no_defs');
	proj4.defs('EPSG:29189', '+proj=utm +zone=19 +south +ellps=aust_SA +units=m +no_defs');
	proj4.defs('EPSG:29190', '+proj=utm +zone=20 +south +ellps=aust_SA +units=m +no_defs');
	proj4.defs('EPSG:29191', '+proj=utm +zone=21 +south +ellps=aust_SA +units=m +no_defs');
	proj4.defs('EPSG:29192', '+proj=utm +zone=22 +south +ellps=aust_SA +units=m +no_defs');
	proj4.defs('EPSG:29193', '+proj=utm +zone=23 +south +ellps=aust_SA +units=m +no_defs');
	proj4.defs('EPSG:29194', '+proj=utm +zone=24 +south +ellps=aust_SA +units=m +no_defs');
	proj4.defs('EPSG:29195', '+proj=utm +zone=25 +south +ellps=aust_SA +units=m +no_defs');

	//SAD 69 - UTM Norte
	proj4.defs('EPSG:29169', '+proj=utm +zone=19 +ellps=aust_SA +units=m +no_defs');
	proj4.defs('EPSG:29170', '+proj=utm +zone=20 +ellps=aust_SA +units=m +no_defs');
	proj4.defs('EPSG:29171', '+proj=utm +zone=21 +ellps=aust_SA +units=m +no_defs');
	proj4.defs('EPSG:29172', '+proj=utm +zone=22 +ellps=aust_SA +units=m +no_defs');
	proj4.defs('EPSG:29173', '+proj=utm +zone=23 +ellps=aust_SA +units=m +no_defs');


	//SAD 69 - GCS
	proj4.defs('EPSG:4618', '+proj=longlat +ellps=aust_SA +no_defs');
	//////////////////////



	//SIRGAS 2000 - UTM
	proj4.defs('EPSG:31977', '+proj=utm +zone=17 +south +ellps=GRS80 +towgs84=0,0,0,0,0,0,0 +units=m +no_defs');
	proj4.defs('EPSG:31978', '+proj=utm +zone=18 +south +ellps=GRS80 +towgs84=0,0,0,0,0,0,0 +units=m +no_defs');
	proj4.defs('EPSG:31979', '+proj=utm +zone=19 +south +ellps=GRS80 +towgs84=0,0,0,0,0,0,0 +units=m +no_defs');
	proj4.defs('EPSG:31980', '+proj=utm +zone=20 +south +ellps=GRS80 +towgs84=0,0,0,0,0,0,0 +units=m +no_defs');
	proj4.defs('EPSG:31981', '+proj=utm +zone=21 +south +ellps=GRS80 +towgs84=0,0,0,0,0,0,0 +units=m +no_defs');
	proj4.defs('EPSG:31982', '+proj=utm +zone=22 +south +ellps=GRS80 +towgs84=0,0,0,0,0,0,0 +units=m +no_defs');
	proj4.defs('EPSG:31983', '+proj=utm +zone=23 +south +ellps=GRS80 +towgs84=0,0,0,0,0,0,0 +units=m +no_defs');
	proj4.defs('EPSG:31984', '+proj=utm +zone=24 +south +ellps=GRS80 +towgs84=0,0,0,0,0,0,0 +units=m +no_defs');
	proj4.defs('EPSG:31985', '+proj=utm +zone=25 +south +ellps=GRS80 +towgs84=0,0,0,0,0,0,0 +units=m +no_defs');

	//SIRGAS 2000 - UTM Norte
	proj4.defs('EPSG:31973', '+proj=utm +zone=19 +ellps=GRS80 +towgs84=0,0,0,0,0,0,0 +units=m +no_defs');
	proj4.defs('EPSG:31974', '+proj=utm +zone=20 +ellps=GRS80 +towgs84=0,0,0,0,0,0,0 +units=m +no_defs');
	proj4.defs('EPSG:31975', '+proj=utm +zone=21 +ellps=GRS80 +towgs84=0,0,0,0,0,0,0 +units=m +no_defs');
	proj4.defs('EPSG:31976', '+proj=utm +zone=22 +ellps=GRS80 +towgs84=0,0,0,0,0,0,0 +units=m +no_defs');
	proj4.defs('EPSG:31977', '+proj=utm +zone=23 +ellps=GRS80 +towgs84=0,0,0,0,0,0,0 +units=m +no_defs');

	//SIRGAS 2000 - GCS
	proj4.defs('EPSG:4674', '+proj=longlat +ellps=GRS80 +towgs84=0,0,0,0,0,0,0 +no_defs');
	//////////////////////



	//WGS84 - UTM
	proj4.defs('EPSG:32717', '+proj=utm +zone=17 +south +ellps=WGS84 +datum=WGS84 +units=m +no_defs');
	proj4.defs('EPSG:32718', '+proj=utm +zone=18 +south +ellps=WGS84 +datum=WGS84 +units=m +no_defs');
	proj4.defs('EPSG:32719', '+proj=utm +zone=19 +south +ellps=WGS84 +datum=WGS84 +units=m +no_defs');
	proj4.defs('EPSG:32720', '+proj=utm +zone=20 +south +ellps=WGS84 +datum=WGS84 +units=m +no_defs');
	proj4.defs('EPSG:32721', '+proj=utm +zone=21 +south +ellps=WGS84 +datum=WGS84 +units=m +no_defs');
	proj4.defs('EPSG:32722', '+proj=utm +zone=22 +south +ellps=WGS84 +datum=WGS84 +units=m +no_defs');
	proj4.defs('EPSG:32723', '+proj=utm +zone=23 +south +ellps=WGS84 +datum=WGS84 +units=m +no_defs');
	proj4.defs('EPSG:32724', '+proj=utm +zone=24 +south +ellps=WGS84 +datum=WGS84 +units=m +no_defs');
	proj4.defs('EPSG:32725', '+proj=utm +zone=25 +south +ellps=WGS84 +datum=WGS84 +units=m +no_defs');

	//WGS84 - UTM Norte
	proj4.defs('EPSG:32619', '+proj=utm +zone=19 +ellps=WGS84 +datum=WGS84 +units=m +no_defs');
	proj4.defs('EPSG:32620', '+proj=utm +zone=20 +ellps=WGS84 +datum=WGS84 +units=m +no_defs');
	proj4.defs('EPSG:32621', '+proj=utm +zone=21 +ellps=WGS84 +datum=WGS84 +units=m +no_defs');
	proj4.defs('EPSG:32622', '+proj=utm +zone=22 +ellps=WGS84 +datum=WGS84 +units=m +no_defs');
	proj4.defs('EPSG:32623', '+proj=utm +zone=23 +ellps=WGS84 +datum=WGS84 +units=m +no_defs');

	//WGS84 - GCS
	proj4.defs('EPSG:4326', '+proj=longlat +ellps=WGS84 +datum=WGS84 +no_defs');
	//////////////////////

	var converter = function(crsOrigem, crsDestino, point){

		// return proj4.transform(crsOrigem, crsDestino, point);
		return proj4(crsOrigem, crsDestino, point);

	};

	var subBase = 1000000;
	/*  Definição para amenizar erros de arredondamento com ponto flutuante
	 Por exemplo:
	 - a subtração 49.51334 - 49 deveria retornar 0.51334, mas retorna 0.5133399999999995
	 - com safeSubtract(49.51334, 49), o valor é 0.51334.
	 Caso seja necessário mais precisão, talvez seja preciso aumentar o parâmetro subBase
	 */
	var safeSubtract = function(a, b){
		return (a * subBase - b * subBase) / subBase;
	};

	var toMaskedString = function(float) {

		return float.toString().replace('.', ',');

	};

	function lat2gms(lat) {

		if(!lat || lat === ''){
			return '';
		}

		dec2gms(lat);

		return (lat < 0 ? 'S' : 'N') + d + '°' + m + '\'' + toMaskedString(s) + '"';

	}

	function lng2gms(lng) {

		if(!lng || lng === ''){
			return '';
		}

		dec2gms(lng);

		return (lng < 0 ? 'W' : 'E') + d + '°' + m + '\'' + toMaskedString(s) + '"';

	}

	Number.prototype.lat2gms = function(){

		return lat2gms(this);
	};

	Number.prototype.lng2gms = function(){

		return lng2gms(this);
	};

	var d,m,s, diff;
	function dec2gms(dec) {
		dec = Math.abs(dec);
		d = Math.floor(dec);
		diff = safeSubtract(dec, d);
		m = Math.floor((diff) * 60);
		s = parseFloat(((diff - m / 60) * 3600).toPrecision(6));

		d = (d < 10) ? '0' + d : d.toString();
		m = (m < 10) ? '0' + m : m.toString();
		s = ((s < 10) ? '0' + s : s.toString()).substr(0, 7);
	}


	exports.Coordenadas = {
							converter: converter,
							lat2gms: lat2gms,
							lng2gms: lng2gms
						};

})(window.proj4);