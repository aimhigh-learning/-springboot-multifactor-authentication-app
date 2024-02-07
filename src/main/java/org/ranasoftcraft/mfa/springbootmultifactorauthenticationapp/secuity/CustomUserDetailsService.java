package org.ranasoftcraft.mfa.springbootmultifactorauthenticationapp.secuity;

import lombok.Data;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author sandeep.rana
 */
@Data
public class UserDetails extends UserDetailsService {

    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
