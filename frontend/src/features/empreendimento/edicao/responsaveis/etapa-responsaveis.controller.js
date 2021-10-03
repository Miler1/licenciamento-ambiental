var EtapaResponsaveisControllerEdicao = function($scope, mensagem, modalSimplesService, uploadService, $injector) {

	// Criando extensão da controller EtapaResponsaveisController
    $injector.invoke(exports.controllers.EtapaResponsaveisController, this,
        {
            $scope: $scope,
			mensagem: mensagem,
			modalSimplesService: modalSimplesService,
			$uploadService: uploadService
        }
    );

	var etapaResponsaveis = this;

	etapaResponsaveis.vincularEmpreendedorComoResponsavelTecnico = vincularEmpreendedorComoResponsavelTecnico;

	function vincularEmpreendedorComoResponsavelTecnico() {

		//Não faz nada na alteração
	}
	
	function initResponsaveis() {

		_.forEach($scope.cadastro.empreendimento.responsaveis, function(responsavel){

			etapaResponsaveis.countResponsaveis[responsavel.tipo] += 1;
		});
	}

	initResponsaveis();
};

exports.controllers.EtapaResponsaveisControllerEdicao = EtapaResponsaveisControllerEdicao;