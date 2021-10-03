/* @ngInject */
var LoginService = function (request,$http) {

	this.login = function (params) {

        var parameter = 'login=' + params.login.replace(/[.-]/g, "") + '&password=' + params.senha;

        return $http({
            url: 'login',
            method: "POST",
            data: parameter,
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        });
  };
    
  this.logout = function () {
    return request.get('logout');
  };

	this.getUrlEntradaUnicaPortal = function () {
		return request.get('configuracao/urlEntradaUnicaPortal');
  };

  this.getUrlEntradaUnicaCadastro = function () {
		return request.get('configuracao/urlEntradaUnicaCadastro');
  };

  this.getUrlPortalAp = function () {
		return request.get('configuracao/urlPortalAp');
  };

  this.getUrlProdapRecuperarSenha = function () {
		return request.get('configuracao/urlProdapRecuperarSenha');
  };

  this.getUsuarioSessao = function() {
    return request.get('usuarioSessao');
  };

  this.recuperarSenhaEmail = function(params){
    return request.post('recuperarSenha/' + params);
  };

  this.validaUsuario = function(params) {
    return request.get('validaUsuario/' + params);
  };

};

exports.services.LoginService = LoginService;