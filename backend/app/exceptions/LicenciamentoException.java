package exceptions;

import play.i18n.Messages;

public class LicenciamentoException  extends RuntimeException {

    private String messageKey;
    private Object [] messageArgs;

    public LicenciamentoException () {

    }

    public LicenciamentoException (Throwable throwable) {
        super(throwable);
    }

    public LicenciamentoException(String error) {
        super(error);
    }

    public LicenciamentoException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getUserMessage () {
        return Messages.get(messageKey, messageArgs);
    }

    public LicenciamentoException userMessage(String messageKey, Object ... messageArgs) {

        this.messageArgs = messageArgs;
        this.messageKey = messageKey;

        return this;

    }

}
