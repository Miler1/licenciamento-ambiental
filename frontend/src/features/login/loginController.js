var Login = function ($rootScope, $location, loginService, $window, mensagem) {


	var formUtils = new app.utils.FormUtils();

	$rootScope.verificaRotaLogin();

	var login = this;

	$rootScope.bodyLogin = true;

	login.CPF = $window.CPF;

	login.CNPJ = $window.CNPJ;

	login.urlEntradaUnicaPortal = getUrlEntradaUnicaPortal();

	login.urlEntradaUnicaCadastro = getUrlEntradaUnicaCadastro();

	login.urlPortalAp = getUrlPortalAp();

	login.urlProdapRecuperarSenha = getUrlProdapRecuperarSenha();


	login.fazLogin = function() {

		var cpfValido = login.CPF.isValid(login.usuario);
		var cnpjValido = login.CNPJ.isValid(login.usuario);

		var params = {
			login : login.usuario,
			senha : login.senha
		};

		if((cpfValido) || (cnpjValido) && login.senha) {

			loginService.login(params).then(function(response) {

				$rootScope.usuarioSessao = response.data;
				$rootScope.usuarioLogado = true;
				$location.path('/empreendimentos/listagem');

			}).catch(function(erro){
				mensagem.error(erro.data.texto, {ttl: 15000});

			});

		} else {

			 mensagem.error('Usuário ou senha inválidos');
		}
	};

	function getUrlEntradaUnicaPortal() {

		loginService.getUrlEntradaUnicaPortal().then(function(response) {
			login.urlEntradaUnicaPortal = response.data;

		});
	}

	function getUrlEntradaUnicaCadastro() {

		loginService.getUrlEntradaUnicaCadastro().then(function(response) {
			login.urlEntradaUnicaCadastro = response.data;
			
		});
	}

	function getUrlPortalAp() {
		
		loginService.getUrlPortalAp().then(function(response) {
			login.urlPortalAp = response.data;
			
		});
	}

	function getUrlProdapRecuperarSenha() {
		
		loginService.getUrlProdapRecuperarSenha().then(function(response) {
			login.urlProdapRecuperarSenha = response.data;
			
		});
	}

	login.fazLogout = function() {

		loginService.logout().then(function (response) {
			$rootScope.usuarioLogado = false;
			$location.path('/login');

		});
	};

	function clearModalRecuperaSenha() {

		$rootScope.cpf = null;
		$rootScope.email = null;
		login.cpfDigitado = '';
		formUtils.cleanDirty();

	}

	function clearModalCriarConta() {

		$rootScope.cpf = null;
		$rootScope.usuario = {
			
			pessoa: {
				email: ''
			}
		};
		$rootScope.estadoNascimento = '';
		delete $rootScope.municipios;
		login.cpfCriarConta = '';
		formUtils.cleanDirty();

	}
	
	login.cancelarRecuperaSenha = function() {

		$rootScope.$emit('hideMessageEvent');
		clearModalRecuperaSenha();
		$("#modalRecuperaSenha").modal('hide');

	};

	login.recuperaSenha = function() {
	
		getUrlProdapRecuperarSenha();
		window.open(login.urlProdapRecuperarSenha);
		
		//rec pelo EU:
		// formUtils.initialize($rootScope, "formRecuperaSenha");
		// clearModalRecuperaSenha();
		// $rootScope.$emit('hideMessageEvent');
		// $("#modalRecuperaSenha").modal('show');

	};

	login.criarConta = function() {
		
		getUrlPortalAp();
		window.open(login.urlPortalAp);

	};

	login.openModalCadastroUsuarioExterno = function() {
		
		formUtils.initialize($rootScope, "cadastrarUsuarioExterno");
		$rootScope.$emit('hideMessageEvent');
		$("#cadastroUsuarioExterno").modal('show');
		formUtils.cleanDirty();

	};

	login.cancelarCadastrarUsuarioExterno = function() {

		$rootScope.$emit('hideMessageEvent');
		clearModalCriarConta();
		$("#cadastroUsuarioExterno").modal('hide');

	};

	login.saveUsuarioExterno = function() {

		if (!(login.CPF.isValid(login.cpfCriarConta) || login.CNPJ.isValid(login.cpfCriarConta))){
			mensagem.error('Digite um CPF/CNPJ válido', {referenceId: 1});

		}else{
			loginService.validaUsuario(login.cpfCriarConta).then(function(response){

				if(!response.data){
					$window.open(login.urlEntradaUnicaCadastro + "/#/public/validacao/" + login.cpfCriarConta);

				}else if(response.data){
					mensagem.error('CPF/CNPJ já cadastrado no sistema.', {referenceId: 1});

				}				
			});
		}
	};

	login.recuperarSenhaViaEmail = function() {

		if (!(login.CPF.isValid(login.cpfDigitado) || login.CNPJ.isValid(login.cpfDigitado))){
			mensagem.error('Digite um CPF/CNPJ válido', {referenceId: 2});

		}else{
			loginService.validaUsuario(login.cpfDigitado).then((response) => {

				loginService.recuperarSenhaEmail(login.cpfDigitado).then(()=>{
					clearModalRecuperaSenha();
					$("#modalRecuperaSenha").modal('hide');
					mensagem.success('Sua senha foi enviada para o email cadastrado!', {referenceId: 3});
				})
				.catch(()=>{
					mensagem.error('Erro de comunicação com o entrada única.', {referenceId: 2});
				});

			})
			.catch((err)=>{
				console.log(err);
				mensagem.error('Usuário não cadastrado', {referenceId: 2});
			});

		}
	};
};

exports.controllers.Login = Login;