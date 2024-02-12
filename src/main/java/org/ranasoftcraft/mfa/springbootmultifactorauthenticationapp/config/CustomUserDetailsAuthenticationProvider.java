package org.ranasoftcraft.mfa.springbootmultifactorauthenticationapp.config;

import lombok.SneakyThrows;
import org.jboss.aerogear.security.otp.Totp;
import org.ranasoftcraft.mfa.springbootmultifactorauthenticationapp.secuity.CustomUserDetailsService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

/**
 * @author sandeep.rana
 */
public class CustomUserDetailsAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {


    public static String TOTP_MISSING = "TOTP_MISSING";

    public static String TOTP_INVALID = "TOTP_INVALID";

    private PasswordEncoder passwordEncoder;

    private CustomUserDetailsService customUserDetailsService;

    private String userNotFoundEncodedPassword;


    public CustomUserDetailsAuthenticationProvider(PasswordEncoder passwordEncoder, CustomUserDetailsService customUserDetailsService) {
        this.passwordEncoder = passwordEncoder;
        this.customUserDetailsService = customUserDetailsService;
    }

    @SneakyThrows
    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        if (authentication.getCredentials() == null) {
            this.logger.debug("Failed to authenticate since no credentials provided");
            throw new BadCredentialsException(this.messages
                    .getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
        }
        String presentedPassword = authentication.getCredentials().toString();
        if (!this.passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {
            this.logger.debug("Failed to authenticate since password does not match stored value");
            throw new BadCredentialsException(this.messages
                    .getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
        }

        // check the mfa code
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
        if(customUserDetails.isEnable2FA()) {
            CustomWebAuthenticationDetails customWebAuthenticationDetails = (CustomWebAuthenticationDetails) authentication.getDetails();
            Totp totp = new Totp(customUserDetails.getSecrete());
            if(!StringUtils.hasText(customWebAuthenticationDetails.getCode())) {
                throw new BadCredentialsException(TOTP_MISSING);
            }

            if(!totp.verify(customWebAuthenticationDetails.getCode())) {
                throw new BadCredentialsException(TOTP_INVALID);
            }
        }
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        CustomUserDetails loadedUser;
        try {
            loadedUser = this.customUserDetailsService.loadUserByUsername(username);
        } catch (UsernameNotFoundException notFound) {
            if (authentication.getCredentials() != null) {
                String presentedPassword = authentication.getCredentials()
                        .toString();
                passwordEncoder.matches(presentedPassword, userNotFoundEncodedPassword);
            }
            throw notFound;
        } catch (Exception repositoryProblem) {
            throw new InternalAuthenticationServiceException(repositoryProblem.getMessage(), repositoryProblem);
        }

        if (loadedUser == null) {
            throw new InternalAuthenticationServiceException("UserDetailsService returned null, "
                    + "which is an interface contract violation");
        }
        return loadedUser;
    }
}
