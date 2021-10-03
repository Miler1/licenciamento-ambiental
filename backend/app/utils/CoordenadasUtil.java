package utils;

import java.text.DecimalFormat;

public class CoordenadasUtil {

    private static String formataCoordenadaDecimalParaGMS(Double decimal) {

        Long grau = Math.abs(decimal.longValue()) ;

        Double parteAposVirgula = decimal % 1;
        Double minuto =  Math.abs(parteAposVirgula * 60);

        parteAposVirgula = minuto % 1;
        Double segundo = Math.abs((parteAposVirgula * 60));

        String segundoFormatado = new DecimalFormat("###.####").format(segundo);

        String gms = (grau > 9 ? grau.toString() : "0" + grau) + "º " +
                (minuto > 9D ? minuto.longValue() : "0" +  minuto.longValue()) + "' " +
                (segundo > 9D ? segundoFormatado : "0" + segundoFormatado) + "\"";

        return gms;

    }

    public static String formataLatitudeString(Double latitude) {

        String codigo;

        if(latitude > 0D) {
            codigo = "N ";
        } else {
            codigo = "S ";
        }

        return codigo + formataCoordenadaDecimalParaGMS(latitude);

    }

    public static String formataLongitudeString(Double longitude) {

        String codigo;

        if(longitude > 0D) {
            codigo = "E ";
        } else {
            codigo = "W ";
        }

        return codigo + formataCoordenadaDecimalParaGMS(longitude);

    }
}
