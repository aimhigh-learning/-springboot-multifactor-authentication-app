package org.ranasoftcraft.mfa.springbootmultifactorauthenticationapp.exception;

import javax.naming.AuthenticationException;

/**
 * @author sandeep.rana
 */
public class TotpFailedException extends AuthenticationException {
    public TotpFailedException(String explanation) {
        super(explanation);
    }

    public TotpFailedException() {
        super();
    }
}
