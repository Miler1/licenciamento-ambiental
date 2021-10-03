L.Control.GeometryArea = L.Control.extend({

	options: {
		position: 'topright',
		metric: 'Ha'
	},

	onAdd: function (map) {

		this._div = L.DomUtil.create('div', 'geometry-area');
		
		this.label = L.DomUtil.create('label', '', this._div);
		this.label.innerHTML = 'Área da geometria: ';

		this.span = L.DomUtil.create('span', '', this._div);

		if(this.options.geometry && (this.options.geometry.type === "Polygon" || this.options.geometry.type === "LineString")) {
			this.span.innerHTML = this.getArea(this.options.geometry);
		} else {
			this.span.innerHTML = '-';
		}

		return this._div;

	},
	
	update: function (geometry) {
		
		if(geometry && (geometry.type === "Polygon" || geometry.type === "LineString")) {
			this.span.innerHTML = this.getArea(geometry);
		} else {
			this.span.innerHTML = '-';
		}

	},

	getArea: function (geometry) {

		var area;

		if(geometry.type === "Polygon") {

			area = turf.area(geometry);

			if(this.options.metric === Unidades.METROS_QUADRADOS) {

				area = area.toFixed(2) + " " + Unidades.METROS_QUADRADOS;

			} else {

				area = L.Util.formatNum(area / 1e4, 4).toString().replace('.', ',');
				area = area + " " + Unidades.HECTARES;

			}

		} else if(geometry.type === "LineString") {

			area = turf.lineDistance(geometry);

			if(area < 1)
				area = area.toFixed(2) + " " + Unidades.METROS;
			else
				area = area.toFixed(2) + " " + Unidades.KILOMETROS;

		}

		return area;

	}

});

L.Control.geometryArea = function (options) {
	return new L.Control.GeometryArea(options);
};