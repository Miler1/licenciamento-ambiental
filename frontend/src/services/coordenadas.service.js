/**
 * Service para conversão entre sistemas de coordenadas
 */
var CoordenadasService = function(){

    var coordenadas = this;

    /**
     * Converte as coordenadas entre os sistemas informados
     * @param  {String} crsOrigem  String do sistema de coordenadas, e.g, 'EPSG:4326' de origem
     * @param  {String} crsDestino String do sistema de coordenadas, e.g, 'EPSG:4326' de destino
     * @param  {Number} x Coordenada x (longitude) de entrada
     * @param  {Number} y Coordenada y (latitude) de entrada
     * @param  {String} formato Formato de saida, e.g, 'gms' (graus, minutos e segundos) ou 'dd' (graus decimais)
     * @return {Object} Objeto com propriedades 'x' e 'y' convertidas para crsDestino
     */
    coordenadas.converter = function(crsOrigem, crsDestino, formatoOrigem, formatoDestino, x, y, comMascara){

        if(!(x && y)){
            return {x:x, y:y};
        }

        var point;

        /* Graus, minutos e segundos para graus decimais */
        if(formatoOrigem === 'gms' && formatoDestino === 'dd'){

            point = {x: x, y: y};
            point = gms2LatLng(point);

            var converted = app.Coordenadas.converter(crsOrigem, crsDestino, point);

            if(comMascara){
                converted.x = toMaskedString(converted.x);
                converted.y = toMaskedString(converted.y);
            }

            return converted;

        }
        /* Graus decimais para outros formatos */
        else if(formatoOrigem === 'dd'){

            point = {x: readFloat(x), y: readFloat(y)};

            point = app.Coordenadas.converter(crsOrigem, crsDestino, point);

            if(formatoDestino === 'gms'){

                return {x: lng2gms(point.x), y: lat2gms(point.y)};

            }else{

                if(comMascara){
                    point.x = toMaskedString(point.x);
                    point.y = toMaskedString(point.y);
                }

                return point;

            }

        }

        return point;

    };

    coordenadas.getMask = function(tipo){

        return MASKS[tipo] || MASKS.dd;

    };

    // var pointRegex = /\./g;
    var readFloat = function(string) {
        if(string && string.replace){
            return parseFloat(string.replace(',','.'));
        }
        return string;
    };

    var toMaskedString = function(float) {

        return float.toString().replace('.', ',');

    };

    // Máscara para DMS
    var GMS_MASK = {
        x: ['W00°60\'60,7777"', {
            translation: {
                '"': {pattern: /"/, optional: true},
                '6': {pattern: /[0-5]/, optional: true},
                '7': {pattern: /[0-9]/, optional: true}
            }
        }],

        y: ['~09°60\'60,7777"', {
            translation: {
                '"': {pattern: /"/, optional: true},
                '6': {pattern: /[0-5]/, optional: true},
                '7': {pattern: /[0-9]/, optional: true},
                '~': {pattern: /[NS]/i}
            }
        }]
    };


    // Máscara para Projetadas
    var PROJ_MASK = {
        x: ['~099999999999,999', {
            translation: {
                '~': {pattern: /[\-\+]/, optional: true}
            }
        }]
    };

    PROJ_MASK.y = PROJ_MASK.x;

    // Máscara para Geográficas
    var LL_MASK = {
        x: ['-09,999999', {
            translation: {
                6: {pattern: /[0-5]/, optional: true}
            }
        }],
        y: ['~09,999999', {
            translation: {
                '~': {pattern: /[\-\+]/, optional: true},
                6: {pattern: /[0-5]/, optional: true}
            }
        }]
    };

    var MASKS = {
        gms: GMS_MASK,
        dd: LL_MASK,
        utm: PROJ_MASK
    };

    var DMS_X_REGEX = /^([WE])(\d{1,2})(°((\d{1,2})('(((\d{1,2})(\,\d*)?)(")?)?)?)?)?$/i;
    var DMS_Y_REGEX = /^([NS])(\d{1,2})(°((\d{1,2})('(((\d{1,2})(\,\d*)?)(")?)?)?)?)?$/i;
    var DEC_REGEX = /^([\-\+])?(\d+)(\,(\d*))?$/;

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

    function lng2gms(lng) {

        if(!lng || lng === ''){
            return '';
        }

        dec2gms(lng);

        return (lng < 0 ? 'W' : 'E') + d + '°' + m + '\'' + toMaskedString(s) + '"';

    }

    function lat2gms(lat) {

        if(!lat || lat === ''){
            return '';
        }

        dec2gms(lat);

        return (lat < 0 ? 'S' : 'N') + d + '°' + m + '\'' + toMaskedString(s) + '"';

    }


    function gms2LatLng(point){

        var px = DMS_X_REGEX.exec(point.x),
            py = DMS_Y_REGEX.exec(point.y),
            x, y;

        if (px && py) {
            x = parseInt(px[2]) + parseInt(px[5] || '0') / 60 + parseFloat(readFloat(px[8] || '0')) / 3600;
            y = parseInt(py[2]) + parseInt(py[5] || '0') / 60 + parseFloat(readFloat(py[8] || '0')) / 3600;

            if (px[1].toUpperCase() === 'W'){
                x = -x;
            }
            if (py[1].toUpperCase() === 'S'){
                y = -y;
            }
        }

        return {x: x, y: y};

    }

};

exports.services.CoordenadasService = CoordenadasService;
