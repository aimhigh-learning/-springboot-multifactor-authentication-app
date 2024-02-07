package org.ranasoftcraft.mfa.springbootmultifactorauthenticationapp.config;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
public class CustomUserDetails extends User {

    private String secrete;

    private boolean enable2FA;

    public CustomUserDetails(String username, String password, String secrete, boolean enable2FA,  Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.secrete = secrete;
        this.enable2FA = enable2FA;
    }

    public CustomUserDetails(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        throw  new RuntimeException("Not implemented yet");
    }
}
