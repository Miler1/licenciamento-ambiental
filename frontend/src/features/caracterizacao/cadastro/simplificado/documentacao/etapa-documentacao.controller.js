var CaracterizacaoLASDocumentacaoController = function($scope, mensagem, solicitacaoDocumentoCaracterizacaoService,
                                                       solicitacaoGrupoDocumentoService, caracterizacaoService, config) {

	var etapaDocumentacao = this;

	etapaDocumentacao.passoValido = passoValido;
    etapaDocumentacao.proximo = proximo;
	etapaDocumentacao.selecionarArquivo = selecionarArquivo;
	etapaDocumentacao.baixarDocumento = baixarDocumento;
	etapaDocumentacao.removerDocumento = removerDocumento;
	etapaDocumentacao.podeRemoverDocumento = podeRemoverDocumento;

	etapaDocumentacao.MODELO_URL = config.MODELO_URL;

	$scope.cadastro.etapas.DOCUMENTACAO.passoValido = passoValido;
	$scope.cadastro.etapas.DOCUMENTACAO.beforeEscolherEtapa = undefined;

	etapaDocumentacao.hideVoltar = $scope.cadastro.continuacao || false;
    etapaDocumentacao.tipoLicenca = ($scope.cadastro.caracterizacao.tipoLicenca && $scope.cadastro.caracterizacao.tipoLicenca != null) ? $scope.cadastro.caracterizacao.tipoLicenca : $scope.cadastro.caracterizacao.tiposLicencaEmAndamento[0];

	function passoValido() {

		if(!$scope.formDocumentacao || !$scope.formDocumentacao.$valid || !$scope.cadastro.caracterizacao) {

			return false;
		}

		var isValid = true;
		var isValidCaracterizacao = true;
		var isValidAtividade = true;

        _.each($scope.cadastro.caracterizacao.solicitacoesDocumentoCaracterizacao, function(solicitacaoDocumento) {

            if(solicitacaoDocumento.obrigatorio === true && !solicitacaoDocumento.documento) {

                isValidCaracterizacao = false;
            }
        });

        _.each($scope.cadastro.caracterizacao.solicitacoesGruposDocumentos, function(solicitacaoDocumento) {

            if(solicitacaoDocumento.obrigatorio === true && !solicitacaoDocumento.documento) {

                isValidCaracterizacao = false;
            }
        });

		isValid = isValidAtividade === true && isValidCaracterizacao === true;

        return isValid;
	}

	function proximo() {

		// Força validação de erros no formulário
		$scope.formDocumentacao.$setSubmitted();

		if(etapaDocumentacao.passoValido()){

			if ($scope.cadastro.modo !== 'Visualizar') {

				caracterizacaoService.updateEtapaDocumentacao($scope.cadastro.caracterizacao.id, $scope.cadastro.caracterizacao)
					.then(function(){

						$scope.cadastro.proximo();
					})
					.catch(function(response){

						mensagem.warning(response.data.texto);
						return;
					});

			} else {

				$scope.cadastro.proximo();
			}

		} else {

			mensagem.warning('Preencha os campos destacados em vermelho para prosseguir com o cadastro.');
		}
    }

   	function selecionarArquivo(files, file, solicitacaoDocumento, tipoDocumentacao) {

		// 2465792 = 2 * 1024 * 1024 = 2MB
		if(file && file.size > 25000000) {

			mensagem.warning('Os arquivos selecionados não devem exceder 25MB.', {ttl: 10000});

			return;
		}

		uploadArquivo(file, solicitacaoDocumento, tipoDocumentacao);
	}

	function uploadArquivo(arquivo, solicitacaoDocumento, tipoDocumentacao) {

		if (!arquivo){

			return;
		}

        if(tipoDocumentacao === 'CARACTERIZACAO') {
            solicitacaoDocumentoCaracterizacaoService.uploadDocumento(solicitacaoDocumento.id, arquivo)
                .then(function(response){

                    solicitacaoDocumento.documento = response.data;
                    solicitacaoDocumento.novo = true;
                    mensagem.success('Documento adicionado com sucesso.', {dontScroll: true});
                })
                .catch(function(response){

                    mensagem.warning(response.data.texto);
                    return;
            });
        } else if(tipoDocumentacao === 'GRUPO') {
            solicitacaoGrupoDocumentoService.uploadDocumento(solicitacaoDocumento.id, arquivo)
                .then(function (response) {

                    solicitacaoDocumento.documento = response.data;
                    solicitacaoDocumento.novo = true;
                    mensagem.success('Documento adicionado com sucesso.', {dontScroll: true});
                })
                .catch(function (response) {

                    mensagem.warning(response.data.texto);
                    return;
                });
        }
	}

	function baixarDocumento(idSolicitacaoDocumento, tipoDocumentacao) {
        if(tipoDocumentacao === 'CARACTERIZACAO') {
		    location.href = solicitacaoDocumentoCaracterizacaoService.getRotaDownloadDocumento(idSolicitacaoDocumento);
        } else if(tipoDocumentacao === 'GRUPO') {
            location.href = solicitacaoGrupoDocumentoService.getRotaDownloadDocumento(idSolicitacaoDocumento);
        }
	}

    function removerDocumento(solicitacaoDocumento, tipoDocumentacao) {

        if(tipoDocumentacao === 'CARACTERIZACAO') {
            if ($scope.cadastro.modo !== 'Editar') {

                solicitacaoDocumentoCaracterizacaoService.deleteDocumento(solicitacaoDocumento.id)
                    .then(function(response){

                        solicitacaoDocumento.documento = null;
                        mensagem.success(response.data.texto);
                    })
                    .catch(function(response){

                        mensagem.warning(response.data.texto);
                        return;
                    });

            } else {

                solicitacaoDocumentoCaracterizacaoService.desvincularDocumento(solicitacaoDocumento.id)
                .then(function(response){

                    solicitacaoDocumento.documento = null;
                    mensagem.success(response.data.texto);
                })
                .catch(function(response){

                    mensagem.warning(response.data.texto);
                    return;
                });
            }

        } else if(tipoDocumentacao === 'GRUPO') {
            if ($scope.cadastro.modo !== 'Editar') {

                solicitacaoGrupoDocumentoService.deleteDocumento(solicitacaoDocumento.id)
                    .then(function(response){

                        solicitacaoDocumento.documento = null;
                        mensagem.success(response.data.texto);
                    })
                    .catch(function(response){

                        mensagem.warning(response.data.texto);
                        return;
                    });

            } else {
                solicitacaoGrupoDocumentoService.desvincularDocumento(solicitacaoDocumento.id)
                    .then(function(response){

                        solicitacaoDocumento.documento = null;
                        mensagem.success(response.data.texto);
                    })
                    .catch(function(response){

                        mensagem.warning(response.data.texto);
                        return;
                    });
            }

        }
    }

    function podeRemoverDocumento() {
        return $scope.cadastro.caracterizacao.status.id !== app.STATUS_CARACTERIZACAO.NOTIFICADO.id &&
            $scope.cadastro.caracterizacao.status.id !== app.STATUS_CARACTERIZACAO.NOTIFICADO_EM_ANDAMENTO.id;
    }

};

exports.controllers.CaracterizacaoLASDocumentacaoController = CaracterizacaoLASDocumentacaoController;
