var UploadLayer = function(map, verificarPonto, verificarPoligono, verificarLinha, typeGeometryAllowed, mensagem, growlMessages, atividadeID) {

	this.uploadedFile = undefined;

	this.verificarPonto = verificarPonto;
	this.verificarPoligono = verificarPoligono;
	this.verificarLinha = verificarLinha;
	this.typeGeometryAllowed = typeGeometryAllowed;
	this.mensagem = mensagem;
	this.growlMessages = growlMessages;
	this.atividadeID = atividadeID;

	this.addUploadLayerControl(map);

};

UploadLayer.prototype.addUploadLayerControl = function(map) {

	this.uploadControl = L.easyButton('fa-upload', function(){

		$('<input type="file" id="layerFile" accept=".zip, .gpx, .kml">')
			.click()
			.change(function(e){

				this.uploadedFile = e.target.files[0];
				this.importFile(this.uploadedFile);

			}.bind(this));

	}.bind(this), 'Importar shapefile (.ZIP, .GPX, .KML)',{

		position: 'topleft'

	});

	map.addControl(this.uploadControl);

};

UploadLayer.prototype.removeControl = function(map) {

	map.removeControl(this.uploadControl);

};

UploadLayer.prototype.mensagemGeometriaNaoPermitida = function(tipoGeometriaDesenhada) {

	if(this.atividadeID) {
		this.mensagem.error('A atividade selecionada não permite geometria do tipo ' + tipoGeometriaDesenhada + '.');
	}
	else {
		this.mensagem.error('Não é permitida geometria do tipo ' + tipoGeometriaDesenhada + '.');
	}
};

UploadLayer.prototype.importFile = function() {

	this.growlMessages.destroyAllMessages();

	var fileType = ['zip', 'gpx', 'kml'],
		fileExtension = this.getFileExtension(this.uploadedFile.name),
		fileReader = new FileReader(),
		extensionIsValid = false;

	for(var i = 0; i < fileType.length; i++) {

		if (fileType[i] === fileExtension) {

			extensionIsValid = true;
		}
	}

	if (!extensionIsValid) {

		this.mensagem.error('Esta funcionalidade suporta apenas arquivos do tipo .ZIP, .KML e .GPX');

		delete this.uploadedFileData;
		$('#layerFile').val('');
		return;

	}

	if(fileExtension === 'zip') {

		fileReader.readAsArrayBuffer(this.uploadedFile);

	} else {

		fileReader.readAsText(this.uploadedFile);
	}

	fileReader.onload = function() {

		switch(fileExtension) {

			case 'kml':
				
				this.uploadedFileData = app.utils.toGeoJSON.kml($.parseXML(fileReader.result));
				this.addGeometry(this.uploadedFileData);
				break;

			case 'gpx':

				this.uploadedFileData = app.utils.toGeoJSON.gpx($.parseXML(fileReader.result));
				this.addGeometry(this.uploadedFileData);
				break;

			case 'zip':

				shp(fileReader.result).then(function(data) {
					
					if(data instanceof Array) {
						return;
					}
					
					this.addGeometry(data);

				}.bind(this));
				break;
		}

	}.bind(this);

};

UploadLayer.prototype.addGeometry = function(geojson) {

	this.growlMessages.destroyAllMessages();

	if (!geojson || geojson.features.length === 0) {

		this.mensagem.error('O arquivo selecionado é inválido ou não foi selecionado nenhum arquivo.');

		return;
	}

	L.geoJSON(geojson, {
		
		onEachFeature: function(feature, layer){
			
			switch (layer.feature.geometry.type) {
				
				case 'Polygon':

					if(!this.typeGeometryAllowed.polygon) {
						this.mensagemGeometriaNaoPermitida('polígono');
						return;
					}

				
					layer.setStyle({
						color: '#f06eaa',
						fill: true,
						fillOpacity: 0.2,
						opacity: 0.5,
						stroke: true,
						weight: 3
					});
				
					this.verificarPoligono(layer, this.atividadeID);

					break;
				
				case 'LineString':

					if(!this.typeGeometryAllowed.polyline) {
						this.mensagemGeometriaNaoPermitida('linha');
						return;
					}
					
					layer.setStyle({
						color: '#f06eaa',
						fill:false,
						opacity: 0.5,
						stroke: true,
						weight: 4
					});

					this.verificarLinha(layer, this.atividadeID);

					break;

				case 'Point':

					if(!this.typeGeometryAllowed.marker) {
						this.mensagemGeometriaNaoPermitida('ponto');
						return;
					}

					this.verificarPonto(layer, this.atividadeID);
					break;
			}

		}.bind(this)
	});
};

UploadLayer.prototype.getFileExtension = function(fileName) {

	var ext = fileName.split('.').pop();
	
	if(ext === fileName) {
		
		return '';
	}
	
	return ext;
};

exports.utils.UploadLayer = UploadLayer;