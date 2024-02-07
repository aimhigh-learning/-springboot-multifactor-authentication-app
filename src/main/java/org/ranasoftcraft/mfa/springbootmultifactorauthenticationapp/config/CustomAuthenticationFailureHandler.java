package org.ranasoftcraft.mfa.springbootmultifactorauthenticationapp.config;

import org.ranasoftcraft.mfa.springbootmultifactorauthenticationapp.exception.TotpMissingException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author sandeep.rana
 */
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String msg =  exception.getMessage();
        if(msg.equals(CustomUserDetailsAuthenticationProvider.TOTP_MISSING)) {
            byte [] bc = new String(request.getParameter("username") + ":" +request.getParameter("password")).getBytes(StandardCharsets.UTF_8);

            final String basicAuthToken = new String(Base64.getUrlEncoder().encode(bc));
            response.sendRedirect("/mfa/authenticator?token="+basicAuthToken);
        } else if(msg.equals(CustomUserDetailsAuthenticationProvider.TOTP_INVALID)) {
            byte [] bc = new String(request.getParameter("username") + ":" +request.getParameter("password")).getBytes(StandardCharsets.UTF_8);
            final String basicAuthToken = new String(Base64.getUrlEncoder().encode(bc));
            response.sendRedirect("/mfa/authenticator?token="+basicAuthToken+"&failed=true&message=Invalid code.");
        } else {
            response.sendRedirect("/login?error=true&message=Invalid username or password");
        }
    }
}
