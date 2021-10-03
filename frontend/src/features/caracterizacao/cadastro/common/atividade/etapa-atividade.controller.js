var CaracterizacaoDLAAtividadeController = function ($scope, $location, $rootScope, mensagem, caracterizacaoService, $q, imovelService,
                                                     $uibModal, modalSimplesService, $routeParams, ID_TIPOLOGIA_AGROSSILVIPASTORIL, enderecoService, empreendimentoService, tiposLicencaService) {

    var etapaAtividade = this;

    etapaAtividade.passoValido = passoValido;
    etapaAtividade.proximo = proximo;
    etapaAtividade.anterior = anterior;
    etapaAtividade.removerAtividade = removerAtividade;
    etapaAtividade.getAtividadesPorTipologia = getAtividadesPorTipologia;
    etapaAtividade.alterarTipologia = alterarTipologia;
    etapaAtividade.verificaTipologiaSelecionada = verificaTipologiaSelecionada;
    etapaAtividade.abrirModalSelecionarCnae = abrirModalSelecionarCnae;
    etapaAtividade.trocarTipoCaracterizacao = trocarTipoCaracterizacao;
    etapaAtividade.filtrarAtividadesDaCnae = filtrarAtividadesDaCnae;
    etapaAtividade.limparDados = limparDados;
    etapaAtividade.cadastroSelecionadoDI = cadastroSelecionadoDI;
    etapaAtividade.cadastroSelecionadoSimplificado = cadastroSelecionadoSimplificado;
    etapaAtividade.configCampoVigenciaRequerida = configCampoVigenciaRequerida;
    // Funções para validação para trocar de passo
    etapaAtividade.validaTiposLicencaEscolhidas = validaTiposLicencaEscolhidas;
    etapaAtividade.validarLicencaEscolhidaNaEtapaCorreta = validarLicencaEscolhidaNaEtapaCorreta;
    // Funções utilizadas na lógica de tiposLicença
    etapaAtividade.listaLicencasAbreModal = listaLicencasAbreModal;
    etapaAtividade.listaTiposLicenca = listaTiposLicenca;
    etapaAtividade.selecionaItem = selecionaItem;
    etapaAtividade.quantidadeCasasDecimais = quantidadeCasasDecimais;
    etapaAtividade.listaLicencasAtualizacaoSelecionadas = [];
    etapaAtividade.hideVoltar = true;
    etapaAtividade.tipologiasCodigo = app.TIPOLOGIAS;
    etapaAtividade.visualizaDescricao = false;
    etapaAtividade.zonaLocalizacao = app.LOCALIZACOES_EMPREENDIMENTO;

    $scope.cadastro.etapas.ATIVIDADE.passoValido = passoValido;
    $scope.cadastro.etapas.ATIVIDADE.beforeEscolherEtapa = beforeEscolherEtapa;
    $scope.limitesValidos = null;

    var imovelValidoParaLicenciamento = false;

    $scope.cadastro.caracterizacao.dispensa = {};
    $scope.cadastro.caracterizacao.atividadesCaracterizacao = [];
    $scope.cadastro.caracterizacao.descricaoAtividade = null;
    $scope.cadastro.caracterizacao.vigenciaSolicitada = null;
    $scope.cadastro.atividadesCnaesEmpreendimento = [];
    $scope.cadastro.caracterizacao.atividadeSelecionada = {};
    etapaAtividade.tipologiaAnterior = undefined;
    etapaAtividade.tipoCaracterizacaoAnterior = undefined;

    function init() {

        empreendimentoService.getEmpreendimentoPorId($scope.cadastro.idEmpreendimento)
            .then(function (response) {

                etapaAtividade.empreendimento = response.data;
                $rootScope.empreendimento = etapaAtividade.empreendimento;

                verificaImovelRuralValidoLicenciamento();
                getTipologias();
            });

    }



    function resetCaracterizacao() {
        etapaAtividade.caracterizacao = {
            empreendimento: {
                id: etapaAtividade.idEmpreendimento
            },
            atividadesCaracterizacao: []
        };

        for (var index in etapaAtividade.etapas) {
            etapaAtividade.etapas[index].beforeEscolherEtapa = undefined;
        }

    }

    function limparDados() {

        etapaAtividade.finalidadeItemAtivo = null;

        if (etapaAtividade.tipoCaracterizacao === 'dispensa') {
            etapaAtividade.tipologia = null;
            etapaAtividade.caracterizacao.atividadesCaracterizacao.forEach(function (ac) {
                ac.atividade = null;
            });
            etapaAtividade.caracterizacao.descricaoAtividade = null;
            etapaAtividade.cadastro.caracterizacao.vigenciaSolicitada = null;

            etapaAtividade.limparDados();
        } else {
            resetCaracterizacao();
            $scope.cadastro.caracterizacao.atividadeSelecionada = {};
            etapaAtividade.tipologia = null;
        }
    }

    function removerAtividade(atividadeCaracterizacao) {
        var configModal = {
            titulo: 'Confirmar remoção da atividade',
            conteudo: 'Tem certeza que deseja remover a atividade?'
        };

        var instanciaModal = modalSimplesService.abrirModal(configModal);

        instanciaModal.result.then(() => {
            var indexAC = $scope.cadastro.caracterizacao.atividadesCaracterizacao.findIndex(ac => ac === atividadeCaracterizacao);
            $scope.cadastro.caracterizacao.atividadesCaracterizacao[indexAC].atividade.parametros.forEach(function (p) {
                p.valorParametro = null;
            });

            $scope.cadastro.caracterizacao.atividadesCaracterizacao.splice(indexAC, 1);
            etapaAtividade.atividades = etapaAtividade.atividadesBckp;

        }, () => {
        });
    }

    function podeEmitirDla() {

        if ($scope.cadastro.tipologia && $scope.cadastro.tipologia.id === ID_TIPOLOGIA_AGROSSILVIPASTORIL) {
            return $scope.cadastro.empreendimento.isPessoaFisica;
        }
        return true;
    }

    function passoValido() {

        if ($scope.cadastro.dispensa) {
            return true;
        }

        $scope.cadastro.caracterizacao.atividadesCaracterizacao.forEach(function (ac) {
            ac.atividadeCaracterizacaoParametros = [];
            if (ac.atividade) {
                ac.atividade.parametros.forEach(function (p) {
                    ac.atividadeCaracterizacaoParametros.push({
                        parametroAtividade: p,
                        valorParametro: p.valorParametro
                    });
                });
            }
        });

        return !!(validarCamposFormulario() &&
            todasAtividadesComCnaesSelecionados() &&
            podeEmitirDla() && tipoAtividadePermitido() &&
            possuiDescricaoValida() &&
            possuiVigenciaValida() &&
            validarLicencaEscolhidaNaEtapaCorreta() &&
            validarParametrosPreenchidos() &&
            validarComplexo() &&
            imovelValidoParaLicenciamento);
    }

    function todasAtividadesComCnaesSelecionados() {
        var todosOk = true;
        $scope.cadastro.caracterizacao.atividadesCaracterizacao.forEach(function (ac) {
            if (_.isEmpty(ac.atividadesCnae)) {
                todosOk = false;
            }
        });
        return todosOk;
    }

    function tipoAtividadePermitido() {

        return $scope.cadastro.empreendimento;
    }

    function validaLimiteParametro(){

        $scope.limitesValidos = [];
        $scope.cadastro.caracterizacao.atividadesCaracterizacao.forEach(function (ac) {
            ac.atividadeCaracterizacaoParametros.forEach(function (acp, index) {
                if(ac.atividade.limites !== null && ac.atividade.limites !== undefined){
                    if (acp.parametroAtividade.codigo === ac.atividade.limites.parametroAtividade.codigo){
                        if(ac.atividade.limites.limiteInferior){
                            if(acp.valorParametro <= ac.atividade.limites.limiteInferior){
                                $scope.limitesValidos[index] = false;
                            }

                        }
                        else if(ac.atividade.limites.limiteSuperior){
                            if(acp.valorParametro > ac.atividade.limites.limiteSuperior){
                                $scope.limitesValidos[index] = false;
                            }
                        }
                    }
                }
            });
        });
        
        return $scope.limitesValidos.includes(false) ? false : true;

    }

    function validarCamposFormulario() {

        let camposValidos = $scope.formDadosAtividade && $scope.formDadosAtividade.tipoCaracterizacaoRadioSimplificadoDLa && $scope.formDadosAtividade.tipoCaracterizacaoRadioSimplificadoDLa.$valid;

        if ($scope.cadastro.retificacao || $scope.cadastro.renovacao){
            if ($scope.formDadosAtividade.tipoCaracterizacaoRadio) {
                camposValidos = $scope.formDadosAtividade && $scope.formDadosAtividade.tipoCaracterizacaoRadio && $scope.formDadosAtividade.tipoCaracterizacaoRadio.$valid;
            }else{
                camposValidos = $scope.formDadosAtividade;
            }
        }else if (etapaAtividade.empreendimento && etapaAtividade.empreendimento.isPessoaFisica) {
            camposValidos = camposValidos && !!$scope.formDadosAtividade.selectTipologia && $scope.formDadosAtividade.selectTipologia.$valid;
        }

        if ($scope.formDadosAtividade.tipoCaracterizacaoRadio){
            camposValidos = camposValidos && $scope.formDadosAtividade.tipoCaracterizacaoRadio && $scope.formDadosAtividade.tipoCaracterizacaoRadio.$valid;
        }

        return camposValidos;
    }

    function redirecionarListagemCaracterizacoes() {
        $location.path('empreendimento/' + $routeParams.idEmpreendimento + '/caracterizacoes');
    }

    // Função para validar se o usuário escolheu alguma solicitação ou renovação
    function validaTiposLicencaEscolhidas() {

        let validacaoLicencaRenovacao = false;
        let validaListaAtualizacao = false;
        let validaListaSolicitacoes = false;
        let validaListaCadastro = false;

        $scope.cadastro.listaSelecionada = etapaAtividade.licencaSelecionada;

        if ($scope.cadastro.renovacao) {
            $scope.cadastro.listaSelecionada =
                [$scope.cadastro.listaFluxoLicencas[$scope.cadastro.caracterizacao.tipoLicenca.sigla][$scope.cadastro.acao]];
            return $scope.cadastro.listaSelecionada.length > 0;
        }else if ($scope.cadastro.retificacao) {
            $scope.cadastro.listaSelecionada = [$scope.cadastro.caracterizacao.tipoLicenca];
            return $scope.cadastro.listaSelecionada.length > 0;
        }else {
            // Percorre a lista de Renovações (radiobuttons)
            validacaoLicencaRenovacao = etapaAtividade.listaRenovacoes.some(function (element) {
                return element.selecionado;
            });

            //Percorre a lista de Solicitações
            validaListaSolicitacoes = etapaAtividade.listaSolicitacoes.some(function (element) {
                return element.selecionado;
            });

            //Percorre a lista com os cadastros
            if(etapaAtividade.listaCadastro) {
                validaListaCadastro = etapaAtividade.listaCadastro.some(function (element) {
                    return element.selecionado;
                });
            }

            // Caso já tenha encontrado um selecionado
            if (validacaoLicencaRenovacao) {
                $scope.cadastro.listaSelecionada = etapaAtividade.listaRenovacoes;
                return validacaoLicencaRenovacao;
            }else if (validaListaCadastro){
                $scope.cadastro.listaSelecionada = etapaAtividade.listaCadastro;
            }else {
                // Percorre a lista de Solicitações (checkboxes)
                validaListaSolicitacoes = etapaAtividade.listaSolicitacoes.some(function (element) {
                    $scope.cadastro.listaSelecionada = etapaAtividade.listaSolicitacoes;
                    return element.selecionado;
                });

                if(etapaAtividade.listaCadastro) {
                    validaListaCadastro = etapaAtividade.listaCadastro.some(function (element) {
                        return element.selecionado;
                    });
                }

                if (validacaoLicencaRenovacao) {
                    $scope.cadastro.listaSelecionada = etapaAtividade.listaRenovacoes;
                    return validacaoLicencaRenovacao;
                }
            }
        }
        return validacaoLicencaRenovacao || validaListaSolicitacoes || validaListaAtualizacao || validaListaCadastro;
    }

    function possuiDescricaoValida() {
        if ($scope.cadastro.renovacao) {
            return true;
        }
        return $scope.formDadosAtividade.descricaoAtividade && $scope.formDadosAtividade.descricaoAtividade.$valid;
    }

    function possuiVigenciaValida() {
        const vigenciaSolicitada = $scope.formDadosAtividade.vigenciaSolicitada;

        if ($scope.cadastro.tipoCaracterizacao === $scope.cadastro.tiposCaracterizacao.DI || $scope.cadastro.finalidadeLicenca === 'CADASTRO') {
            return true;
        } else if ( vigenciaSolicitada.$viewValue >= 1 && vigenciaSolicitada.$viewValue <= etapaAtividade.maxValidadeEmAnos) {
            return true;
        }
        return vigenciaSolicitada && vigenciaSolicitada.$valid;
    }

    function validarLicencaEscolhidaNaEtapaCorreta() {
        return !($scope.cadastro.tipoCaracterizacao === $scope.cadastro.tiposCaracterizacao.SIMPLIFICADO &&
            !validaTiposLicencaEscolhidas());
    }

    function validarParametrosPreenchidos() {
        var parametrosValidos = true;
        $scope.cadastro.caracterizacao.atividadesCaracterizacao.forEach(function (ac) {
            ac.atividadeCaracterizacaoParametros.forEach(function (acp) {
                if ((typeof acp.valorParametro) !== "number") {
                    parametrosValidos = false;
                }
            });
        });
        return parametrosValidos;
    }

    function validarComplexo() {
        if ($scope.cadastro.tiposCaracterizacao.SIMPLIFICADO) {
            if ($scope.cadastro.caracterizacao.atividadesCaracterizacao.length < 2) {
                return true;
            }

            if ($scope.cadastro.caracterizacao.complexo !== undefined) {
                return true;
            }
        }
        return false;
    }

    function verificaImovelRuralValidoLicenciamento() {

        if ($rootScope.empreendimento.localizacao === 'ZONA_RURAL') {
            verificaImovelValidoLicenciamento();

        } else {
            imovelValidoParaLicenciamento = true;

        }

    }

    function verificaImovelValidoLicenciamento() {

        getDadosImovel($rootScope.empreendimento.imovel.codigo);

    }

    function getDadosImovel(codigoImovel) {

        imovelService.getImoveisCompletoByCodigo(codigoImovel)
            .then(function (response) {

                imovelService.verificarLicenciamento(codigoImovel)
                    .then((response) => {
                        imovelValidoParaLicenciamento = true;

                    })
                    .catch((err) => {
                        imovelValidoParaLicenciamento = false;

                    });

            })
            .catch(function () {

                mensagem.error("Não foi possível obter os dados do imóvel no CAR.");

            });

    }

    function proximo() {

        verificaImovelRuralValidoLicenciamento();

        // Força validação de erros no formulário
        $scope.formDadosAtividade.$setSubmitted();

        if($scope.cadastro.tipoCaracterizacao ==='dispensa'){
            $scope.cadastro.proximo();
        }

        if (passoValido() && validaLimiteParametro()) {
            if (($scope.cadastro.renovacao || $scope.cadastro.retificacao) && validarParametrosPreenchidos()) {

                $scope.cadastro.continuacao = true;
                $scope.cadastro.proximo();

            }
            $scope.cadastro.proximo();

        }else {

            if (!$scope.cadastro.tipologia && etapaAtividade.empreendimento.isPessoaFisica) {

                mensagem.warning('Selecione uma tipologia para seguir com o cadastro.');

            } else if (!todasAtividadesComCnaesSelecionados()) {

                mensagem.warning('Selecione uma atividade para prosseguir com o cadastro.');

            } else if (!validarLicencaEscolhidaNaEtapaCorreta()) {

                $scope.hasError = true;
                mensagem.warning('Selecione ao menos um tipo de solicitação para prosseguir com o cadastro.');

            } else if (!possuiDescricaoValida()) {

                mensagem.warning('Escreva uma descrição da solicitação para prosseguir com o cadastro.');

            } else if (!possuiVigenciaValida()) {

                mensagem.warning('Insira um período de vigência da solicitação dentro dos limites estabelecidos para prosseguir com o cadastro.');

            } else if (!validarParametrosPreenchidos()) {

                mensagem.warning('Preencha todos os parâmetros para prosseguir com o cadastro.');

            } else if (!validaLimiteParametro()) {

                mensagem.warning('Preencha todos os parâmetros dentro dos limites estabelecidos para prosseguir com o cadastro.',{ttl: 10000});

            } else if (!validarComplexo()) {

                mensagem.warning('Selecione se as atividade serão desenvolvidas em um complexo ou não.');

            } else if (!imovelValidoParaLicenciamento) {

                mensagem.warning("A condição do CAR não permite a solicitação de Licenciamento. Favor entrar em contato com a SEMA para verificação.");

            } else {

                var tipoEndereco = $scope.cadastro.empreendimento.localizacao;

                var possuiAtividadeSemUrbada = $scope.cadastro.caracterizacao.atividadesCaracterizacao.find((ac) =>
                    ac.atividade.tiposAtividade.filter((t) => t.codigo !== 'URBANA').length > 0
                );

                if ((tipoEndereco === 'ZONA_URBANA' && possuiAtividadeSemUrbada) || tipoEndereco === 'ZONA_RURAL') {
                    mensagem.warning('No momento não é possível continuar com a solicitação de declaração de inexigibilidade e licenças ambientais para imóveis e/ou atividades rurais. Favor procurar a SEMA para prosseguir com o processo de licenciamento.');
                } else {
                    mensagem.warning('Verifique os campos destacados em vermelho para prosseguir com o cadastro.');
                }
            }
        }

        //Por ser renovação foi necessário validar novamente a licença e depois settar a flag pra true para prosseguir o passo
        if ($scope.cadastro.renovacao) {
            $scope.cadastro.continuacao = true;
        }

        $scope.cadastro.licencaSelecionadaDentroDoPeriodo = false;

    }

    function beforeEscolherEtapa() {
        return passoValido();
    }

    function anterior() {
        redirecionarListagemCaracterizacoes();
    }

    function trocarTipoCaracterizacao() {

        if (!etapaAtividade.tipologiaAnterior) {
            executarAlteracaoTipoCaracterizacao();
        } else {
            var configModal = {
                titulo: 'Confirmar alteração do tipo de solicitação',
                conteudo: 'Tem certeza que deseja alterar o tipo da solicitação selecionada? Ao alterar perderá todo o cadastro já preenchido até o momento.'
            };

            var instanciaModal = modalSimplesService.abrirModal(configModal);

            instanciaModal.result.then(function () {
                executarAlteracaoTipoCaracterizacao();
            }, function () {
                $scope.cadastro.tipoCaracterizacao = etapaAtividade.tipoCaracterizacaoAnterior;
            });
        }
    }

    function executarAlteracaoTipoCaracterizacao() {

        limparDados();
        limparAtividades();
        getTipologias();
        getAtividadesPorTipologia();

        if (cadastroSelecionadoDI()) {

            if ($scope.cadastro.etapas.DOCUMENTACAO) {
                $scope.cadastro.listaEtapas.splice($scope.cadastro.etapas.DOCUMENTACAO.indice, 1);
            }

            $scope.cadastro.etapas.ENQUADRAMENTO.indice -= 1;

            delete $scope.cadastro.etapas.DOCUMENTACAO;

            var atividadesCnaesDI = $scope.cadastro.atividadesCnaesEmpreendimento;

            if (!atividadesCnaesDI) {
                etapaAtividade.atividadesCnaes = [];
            } else {
                etapaAtividade.atividadesCnaes = atividadesCnaesDI.filter(function (atividadeCnae) {
                    return atividadeCnae.dispensaLicenciamento === true;
                });
            }

        } else if (cadastroSelecionadoSimplificado()) {

            $scope.cadastro.etapas.DOCUMENTACAO = {
                nome: 'Documentação',
                indice: 3,
                passoValido: undefined,
                class: 'documentacao',
                disabled: false
            };
            $scope.cadastro.etapas.ENQUADRAMENTO.indice += 1;

            $scope.cadastro.listaEtapas.splice($scope.cadastro.etapas.DOCUMENTACAO.indice, 0, $scope.cadastro.etapas.DOCUMENTACAO);

            var atividadesCnaes = $scope.cadastro.atividadesCnaesEmpreendimento;

            if (!atividadesCnaes) {
                etapaAtividade.atividadesCnaes = [];
            } else {
                etapaAtividade.atividadesCnaes = atividadesCnaes.filter(function (atividadeCnae) {
                    return atividadeCnae.licenciamentoSimplificado === true || atividadeCnae.licenciamentoDeclaratorio === true;
                });
            }
        }

        etapaAtividade.tipologiaAnterior = undefined;
        $scope.cadastro.tipologia = undefined;
        etapaAtividade.tipoCaracterizacaoAnterior = $scope.cadastro.tipoCaracterizacao;
    }

    function cadastroSelecionadoDI() {
        return $scope.cadastro.tipoCaracterizacao === $scope.cadastro.tiposCaracterizacao.DI;
    }

    function cadastroSelecionadoSimplificado() {
        return $scope.cadastro.tipoCaracterizacao === $scope.cadastro.tiposCaracterizacao.SIMPLIFICADO;
    }

    function getTipologias() {
        let cpfCnpj = etapaAtividade.empreendimento.empreendimentoEU.pessoa.cpf || etapaAtividade.empreendimento.empreendimentoEU.pessoa.cnpj;
        var params;

        if ($scope.cadastro.tipoCaracterizacao === 'dispensa') {
            params = {
                dispensaLicenciamento: true
            };
        } else {
            params = {
                licenciamentoSimplificado: true
            };
        }

        params.cpfCnpj = cpfCnpj;

        caracterizacaoService.getTipologias(params)
            .then(function (response) {

                etapaAtividade.tipologias = response.data;

                if (etapaAtividade.tipologias === 0) {
                    mensagem.warning("Os CNAE's cadastrados para o empreendimento não permitem esse tipo de solicitação.");
                }
            });
    }

    function getAtividadesPorTipologia() {

        const cpfCnpj = etapaAtividade.empreendimento.empreendimentoEU.pessoa.cpf || etapaAtividade.empreendimento.empreendimentoEU.pessoa.cnpj;
        let params;

        if($scope.cadastro.tipologia){
            verificaTipologiaSelecionada($scope.cadastro.tipologia);
        }

        if (!$scope.cadastro.tipologia) {
            etapaAtividade.atividades = [];
            etapaAtividade.atividadesBckp = [];
        } else {

            if ($scope.cadastro.tipoCaracterizacao !== 'dispensa') {

                params = {
                    licenciamentoSimplificado: true,
                    idTipologia: $scope.cadastro.tipologia.id
                };
            } else {

                params = {
                    dispensaLicenciamento: true,
                    idTipologia: $scope.cadastro.tipologia.id
                };
            }

            params.cpfCnpj = cpfCnpj;

            return caracterizacaoService.getAtividadesPorTipologia(params)
                .then(function (response) {

                    etapaAtividade.atividades = response.data;
                    etapaAtividade.atividadesBckp = etapaAtividade.atividades;
                    return response.data;
                });
        }
    }

    function verificaTipologiaSelecionada (tipologiaSelecionada){
        
        if(tipologiaSelecionada.codigo !== undefined && tipologiaSelecionada.codigo !== null ){
            
            if(tipologiaSelecionada.codigo === etapaAtividade.tipologiasCodigo.INDUSTRIA_BORRACHA || 
                tipologiaSelecionada.codigo === etapaAtividade.tipologiasCodigo.INDUSTRIA_COURO_PELES || 
                tipologiaSelecionada.codigo === etapaAtividade.tipologiasCodigo.INDUSTRIA_TEXTIL || 
                tipologiaSelecionada.codigo === etapaAtividade.tipologiasCodigo.INDUSTRIA_PRODUTOS_ALIMENTARES ||
                tipologiaSelecionada.codigo === etapaAtividade.tipologiasCodigo.INDUSTRIA_BEBIDAS_ALCOOL){
                    etapaAtividade.visualizaDescricao = true;
            }else{
                etapaAtividade.visualizaDescricao = false;
            }
        }else{
            etapaAtividade.visualizaDescricao = false;
        }

    }

    function limparAtividades() {

        $scope.cadastro.caracterizacao.atividadesCaracterizacao = [{}];
        $scope.cadastro.caracterizacao.perguntasCarregadas = false;

        $scope.cadastro.caracterizacao.atividadesCaracterizacao = [];

        $scope.cadastro.caracterizacao.atividadeSelecionada = {};

        $scope.cadastro.caracterizacao.descricaoAtividade = null;

        $scope.cadastro.caracterizacao.vigenciaSolicitada = null;

    }


    function alterarTipologia() {

        if (!etapaAtividade.tipologiaAnterior) {
            executarAlteracaoTipologia();
        } else {
            var configModal = {
                titulo: 'Confirmar alteração da tipologia selecionada',
                conteudo: 'Tem certeza que deseja alterar a tipologia selecionada? Ao alterar perderá todo o cadastro já preenchido até o momento.'
            };

            var instanciaModal = modalSimplesService.abrirModal(configModal);

            instanciaModal.result.then(function () {
                executarAlteracaoTipologia();
            }, function () {
                $scope.cadastro.tipologia = etapaAtividade.tipologiaAnterior;
            });
        }
        etapaAtividade.atividades = etapaAtividade.atividadesBckp;
    }

    function executarAlteracaoTipologia() {

        etapaAtividade.tipologiaAnterior = $scope.cadastro.tipologia;
        limparAtividades();
        getAtividadesPorTipologia();
    }

    function filtrarAtividadesDaCnae(atividadeCnae) {
        etapaAtividade.atividades = atividadeCnae.atividades;
        etapaAtividade.atividades.forEach(function (atividade) {
            atividade.atividadesCnae = [];
            atividade.atividadesCnae.push(atividadeCnae);
        });

        $scope.cadastro.caracterizacao.atividadesCaracterizacao.forEach(function (ac) {
            ac.atividade = undefined;
            ac.atividadesCnae = undefined;
        });
    }

    function listaLicencasAbreModal(atividade) {

        // Variável que possui as licenças permitidas pela atividade
        var todasLicencasPermitidas = [];
        todasLicencasPermitidas = todasLicencasPermitidas.concat($scope.cadastro.caracterizacao.atividadeSelecionada.atividade.tiposLicenca);
        $scope.cadastro.caracterizacao.atividadesCaracterizacao.forEach(function (ac) {
            todasLicencasPermitidas = todasLicencasPermitidas.concat(ac.atividade.tiposLicenca);
        });

        // Remove duplicados
        var licencasPermitidas = [];
        todasLicencasPermitidas.forEach(function (tlp) {
            if (licencasPermitidas.filter(function (lp) {
                return lp.id === tlp.id;
            }).length === 0) {
                licencasPermitidas.push(tlp);
            }
        });

        etapaAtividade.atividadesBckp = etapaAtividade.atividades;

        etapaAtividade.atividades = etapaAtividade.atividades.filter(a => {
            return a.dentroEmpreendimento === $scope.cadastro.caracterizacao.atividadeSelecionada.atividade.dentroEmpreendimento;
        });

        etapaAtividade.atividades = etapaAtividade.atividades.filter(a => {
            if ($scope.cadastro.caracterizacao.atividadeSelecionada.atividade.grupoDocumento) {
                return a.grupoDocumento && a.grupoDocumento.id === $scope.cadastro.caracterizacao.atividadeSelecionada.atividade.grupoDocumento.id;
            } else {
                return !a.grupoDocumento;
            }
        });

        // Busca os valores no backend dos tipos de Licenca
        etapaAtividade.listaTiposLicenca(licencasPermitidas);

        abrirModalSelecionarCnae(atividade, $scope.cadastro.empreendimento);
    }

    function abrirModalSelecionarCnae(atividade, empreendimento) {

        if (atividade.atividadesCnae && atividade.atividadesCnae.length > 1) {

            var modalInstance = $uibModal.open({
                controller: 'modalCaracterizacaoAtividadeController',
                controllerAs: 'modalCtrl',
                backdrop: 'static',
                keyboard: false,
                templateUrl: './features/caracterizacao/cadastro/common/modal-atividade.html',
                resolve: {
                    atividade: function () {
                        return atividade;
                    },
                    addMaisAtividades: function () {
                        return true;
                    },
                    empreendimento: function () {
                        return empreendimento;
                    }
                }
            });

            modalInstance.result.then(function (atividades) {

                var novaAtividade = {};
                novaAtividade.atividadesCnae = atividades.atividadesCnaeSelecionadas;
                novaAtividade.atividade = atividades.atividade;

                if (!$scope.cadastro.caracterizacao.atividadesCaracterizacao ||
                    $scope.cadastro.tipoCaracterizacao === $scope.cadastro.tiposCaracterizacao.DI) {
                    $scope.cadastro.caracterizacao.atividadesCaracterizacao = [];
                }

                $scope.cadastro.caracterizacao.atividadesCaracterizacao.push(novaAtividade);

                $scope.cadastro.caracterizacao.atividadeSelecionada = {};

                $rootScope.$broadcast('atividadeAlterada');

            }, function () {

                $scope.cadastro.caracterizacao.atividadeSelecionada = {};

                $rootScope.$broadcast('atividadeAlterada');

            });

            $scope.cadastro.dadosGEOCarregados = false;

            return modalInstance;

        } else {

            var novaAtividade = {};
            novaAtividade.atividade = atividade;
            novaAtividade.atividadesCnae = atividade.atividadesCnae;

            if (!$scope.cadastro.caracterizacao.atividadesCaracterizacao ||
                $scope.cadastro.tipoCaracterizacao === $scope.cadastro.tiposCaracterizacao.DI) {
                $scope.cadastro.caracterizacao.atividadesCaracterizacao = [];
            }

            $scope.cadastro.caracterizacao.atividadesCaracterizacao.push(novaAtividade);

            $scope.cadastro.dadosGEOCarregados = false;

            $scope.cadastro.caracterizacao.atividadeSelecionada = {};

            $rootScope.$broadcast('atividadeAlterada');
        }
    }

    function carregarMunicipios() {

        enderecoService.listMunicipios('AP').then(
            function (response) {
                etapaAtividade.municipios = response.data;
            })
            .catch(function () {
                mensagem.warning('Não foi possível obter a lista de municípios.');
            });
    }

    init();

    /*********************************************************************************************/


    // Variáveis para controle
    etapaAtividade.listaSolicitacoes = [];
    etapaAtividade.listaRenovacoes = [];
    etapaAtividade.finalidadeItemAtivo = "";
    etapaAtividade.selecionouLicenca = true;


    // Função para buscar os tipos de licença no back, que usa as licenças permitidas pela atividade
    function listaTiposLicenca(licencasAtividade) {

        tiposLicencaService.listaTiposLicenca(licencasAtividade).then(function (response) {
            etapaAtividade.listaSolicitacoes = response.data.tipoLicencaSolicitacao;
            etapaAtividade.listaRenovacoes = response.data.tipoLicencaRenovacao;
            etapaAtividade.listaAtualizacao = response.data.tipoLicencaAtualizacao;
            etapaAtividade.listaCadastro = response.data.tipoLicencaCadastro;
        }).catch(function (error) {
        });

    }

    function setMaxValidadeEmAnos(validadeEmAnos){
        etapaAtividade.maxValidadeEmAnos = validadeEmAnos;
    }

    function configCampoVigenciaRequerida(){
        if (!!$scope.cadastro.tipoLicencaEvoluir.validadeEmAnos){
            setMaxValidadeEmAnos($scope.cadastro.tipoLicencaEvoluir.validadeEmAnos);
            etapaAtividade.selecionouLicenca = false;
        }
        return $scope.cadastro.retificacao || etapaAtividade.selecionouLicenca;
    }

    function setCampoVigenciaRequerida(validadeEmAnos) {
        const campoVigenciaRequerida = document.getElementById('campo-vigencia-requerida');
        campoVigenciaRequerida.value= "";
        campoVigenciaRequerida.placeholder = "Insira um valor entre 1 e " + validadeEmAnos + " anos";
        setMaxValidadeEmAnos(validadeEmAnos);
    }

    // Função para selecionar e limpar a seleção da lista contrária
    function selecionaItem(item, alterarValor) {

        if($scope.cadastro.caracterizacao.vigenciaSolicitada === 999 || $scope.cadastro.finalidadeLicenca === 'CADASTRO'){
            $scope.cadastro.caracterizacao.vigenciaSolicitada = undefined;
            $scope.cadastro.finalidadeLicenca = undefined;
        }

        etapaAtividade.licencaSelecionada = undefined;

        $scope.listaLicencaAtualizacao = [$scope.licencasEmAndamento];
        $scope.hasError = false;
        //Não terá o campo selecionado se for renovação,pois é transient e não armazena em banco de dados usado só para validações.
        if (item.selecionado === undefined) {
            item.selecionado = false;
            etapaAtividade.licencaSelecionada = item;
        }
        if (item.finalidade === undefined) {
            item.finalidade = $scope.cadastro.finalidadeLicenca;
            etapaAtividade.licencaSelecionada = item;
        }

        if (etapaAtividade.selecionouLicenca){
            etapaAtividade.selecionouLicenca = false;
        }

        if (item.finalidade === "SOLICITACAO") {

            if (!item.selecionado) {

                // Limpa a lista Solicitação (checkbox)
                etapaAtividade.listaSolicitacoes.forEach(function (element) {
                    if (element.sigla !== item.sigla) {
                        document.getElementById(element.sigla).checked = false;
                        element.selecionado = false;
                    }
                });

                // Seta o valor
                item.selecionado = !item.selecionado;
                // Caso o usuário clique no texto, ao invés do botão
                if (alterarValor) {
                    document.getElementById(item.sigla).checked = item.selecionado;
                }

                // Salva a finalidade do item selecionado
                etapaAtividade.finalidadeItemAtivo = item.finalidade;

                // Limpa os elementos da lista Renovação (radio)
                etapaAtividade.listaRenovacoes.forEach(function (element) {
                    if (element.sigla !== item.sigla) {
                        document.getElementById(element.sigla).checked = false;
                        element.selecionado = false;
                    }
                });
            }
            if (etapaAtividade.listaSolicitacoes.length === 0 && $scope.licencasEmAndamento.length > 0) {
                etapaAtividade.listaSolicitacoes = $scope.licencasEmAndamento;
            }

        } else if (item.finalidade === "RENOVACAO") {

            if (!item.selecionado) {

                // Limpa os elementos da lista Renovação (radio)
                etapaAtividade.listaRenovacoes.forEach(function (element) {
                    if (element.sigla !== item.sigla) {
                        document.getElementById(element.sigla).checked = false;
                        element.selecionado = false;
                    }
                });

                // Seta o valor
                item.selecionado = !item.selecionado;
                // Caso o usuário clique no texto, ao invés do botão
                if (alterarValor) {
                    document.getElementById(item.sigla).checked = item.selecionado;
                }

                // Salva a finalidade do item selecionado
                etapaAtividade.finalidadeItemAtivo = item.finalidade;

                // Limpa a lista Solicitação (checkbox)
                etapaAtividade.listaSolicitacoes.forEach(function (element) {
                    if (element.sigla !== item.sigla) {
                        document.getElementById(element.sigla).checked = false;
                        element.selecionado = false;
                    }
                });
            }
            if (etapaAtividade.listaRenovacoes.length === 0 && $scope.licencasEmAndamento.length > 0) {
                etapaAtividade.listaRenovacoes = $scope.licencasEmAndamento;
            }
        } else if (item.finalidade === "ATUALIZACAO") {

            if (!item.selecionado) {

                // Seta o valor
                item.selecionado = !item.selecionado;
                // Caso o usuário clique no texto, ao invés do botão
                if (alterarValor) {
                    document.getElementById(item.sigla).checked = item.selecionado;
                }

                // Salva a finalidade do item selecionado
                etapaAtividade.finalidadeItemAtivo = item.finalidade;
            }
        }
        else if(item.finalidade === 'CADASTRO') {

            if (!item.selecionado) {
                item.selecionado = !item.selecionado;
                if (alterarValor) {
                    document.getElementById(item.sigla).checked = item.selecionado;
                }
                etapaAtividade.finalidadeItemAtivo = item.finalidade;
                $scope.cadastro.caracterizacao.vigenciaSolicitada = 999;
                $scope.cadastro.finalidadeLicenca = 'CADASTRO';
            }

        }
        if (item.selecionado && !etapaAtividade.licencaSelecionada) {
            etapaAtividade.licencaSelecionada = [];
            etapaAtividade.licencaSelecionada.push(item);
            $scope.itemEscolhido = item;
        }

        if(etapaAtividade.licencaSelecionada[0].finalidade === 'CADASTRO'){
            $scope.cadastro.caracterizacao.vigenciaSolicitada = 999;
            $scope.cadastro.finalidadeLicenca = 'CADASTRO';
        }

        setCampoVigenciaRequerida(item.validadeEmAnos);
    }

    function quantidadeCasasDecimais(casasDecimais) {
        return ((typeof casasDecimais) === 'number' ? casasDecimais : '4');
    }

};

exports.controllers.CaracterizacaoDLAAtividadeController = CaracterizacaoDLAAtividadeController;
