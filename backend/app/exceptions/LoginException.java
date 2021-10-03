package exceptions;

public class LoginException extends RuntimeException {

    private String mensagem;

    public LoginException(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getMensagem () {
        return this.mensagem;
    }

}
