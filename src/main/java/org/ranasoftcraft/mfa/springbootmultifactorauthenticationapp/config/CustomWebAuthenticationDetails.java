package org.ranasoftcraft.mfa.springbootmultifactorauthenticationapp.config;

import lombok.Getter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

/**
 * @author sandeep.rana
 */
@Getter
public class CustomWebAuthenticationDetails extends WebAuthenticationDetails {

    private String code;
    public CustomWebAuthenticationDetails(HttpServletRequest request) {
        super(request);
        this.code = request.getParameter("code");
    }

    public CustomWebAuthenticationDetails(String remoteAddress, String sessionId) {
        super(remoteAddress, sessionId);
    }
}
