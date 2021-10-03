package utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MensagemPortalSegurancaUtil {

    public static String convertMensagem(String mensagem) {

        final Pattern pattern = Pattern.compile("<h1>(.+?)</h1>", Pattern.DOTALL);
        final Matcher matcher = pattern.matcher(mensagem);
        boolean match = matcher.find();

        if (!match) {

            return mensagem;
        }

        return matcher.group(1);
    }

}
