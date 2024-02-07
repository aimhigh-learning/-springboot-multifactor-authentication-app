package org.ranasoftcraft.mfa.springbootmultifactorauthenticationapp.exception;

/**
 * @author sandeep.rana
 */
public class TotpMissingException extends RuntimeException {
    public TotpMissingException() {
        super();
    }

    public TotpMissingException(String message) {
        super(message);
    }

    public TotpMissingException(String message, Throwable cause) {
        super(message, cause);
    }

    public TotpMissingException(Throwable cause) {
        super(cause);
    }

    protected TotpMissingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
