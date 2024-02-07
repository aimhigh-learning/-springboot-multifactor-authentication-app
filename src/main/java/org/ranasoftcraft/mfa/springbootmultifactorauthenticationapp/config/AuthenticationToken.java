package org.ranasoftcraft.mfa.springbootmultifactorauthenticationapp.config;

import lombok.Data;
import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author sandeep.rana
 */
@Getter
public class AuthenticationToken extends UsernamePasswordAuthenticationToken implements Serializable {

    private String code;

    public AuthenticationToken(Object principal, Object credentials, String code) {
        super(principal, credentials);
        this.code = code;
        super.setAuthenticated(Boolean.FALSE);
    }

    public AuthenticationToken(Object principal, Object credentials,String code, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
        this.code = code;
        super.setAuthenticated(true); // must use super, as we override
    }
}
