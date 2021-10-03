var UploadService = function(Upload, config) {

	this.uploadArquivo = function(arquivo) {

		return Upload.upload({
			url: config.BASE_URL() + 'upload/save',
			data: {file: arquivo}
		});

	};
	
};

exports.services.UploadService = UploadService;