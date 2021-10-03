var ValidaOutorga = {

    bindings: {
        caracterizacao: '<',
        tituloOutorgaValido: '='
    },

    controller: function (mensagem, tiposTituloOutorga) {

        var ctrl = this;

        ctrl.validarTituloOutorga = function() {

            if (!ctrl.caracterizacao.numeroTituloOutorga || !ctrl.caracterizacao.tipoTituloOutorga) {

                ctrl.tituloOutorgaValido = false;
                return;
            }
        };
    },

    templateUrl: 'components/validaOutorga/validaOutorga.html'

};

exports.directives.ValidaOutorga = ValidaOutorga;