package org.ranasoftcraft.mfa.springbootmultifactorauthenticationapp.exception;

import javax.naming.AuthenticationException;

/**
 * @author sandeep.rana
 */
public class TotpMissingException extends AuthenticationException{
    public TotpMissingException(String explanation) {
        super(explanation);
    }

    public TotpMissingException() {
        super();
    }

}
