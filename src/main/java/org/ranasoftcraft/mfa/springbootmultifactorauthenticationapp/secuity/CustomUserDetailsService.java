package org.ranasoftcraft.mfa.springbootmultifactorauthenticationapp.secuity;

import lombok.Data;
import org.jboss.aerogear.security.otp.api.Base32;
import org.ranasoftcraft.mfa.springbootmultifactorauthenticationapp.config.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

/**
 * @author sandeep.rana
 */
@Data @Service @Transactional(timeout = 10)
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final User user =  userRepository.findById(username).orElseThrow(()-> new UsernameNotFoundException("Username " + username +" not found!"));
        return new CustomUserDetails(user.getUsername(), user.getPassword(),user.getSecrete(), user.isEnable2FA(), Arrays.asList(new SimpleGrantedAuthority("USER")));
    }

    public User signup(final User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnable2FA(Boolean.TRUE);
        user.setSecrete(Base32.random());
        userRepository.save(user);
        return user;
    }

    public User findById(String username) throws UsernameNotFoundException{
        return userRepository.findById(username).orElseThrow(()-> new UsernameNotFoundException("Username " + username +" not found!"));
    }
}
