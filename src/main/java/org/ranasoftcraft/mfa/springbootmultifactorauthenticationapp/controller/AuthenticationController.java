package org.ranasoftcraft.mfa.springbootmultifactorauthenticationapp.controller;

import lombok.extern.slf4j.Slf4j;
import org.jboss.aerogear.security.otp.Totp;
import org.ranasoftcraft.mfa.springbootmultifactorauthenticationapp.mfa.GenerateQrCode;
import org.ranasoftcraft.mfa.springbootmultifactorauthenticationapp.secuity.CustomUserDetailsService;
import org.ranasoftcraft.mfa.springbootmultifactorauthenticationapp.secuity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Controller @Slf4j
public class AuthenticationController {

    @Autowired private CustomUserDetailsService customUserDetailsService;

    @Autowired private GenerateQrCode generateQrCode;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/welcome")
    public String welcome (Authentication authentication) {
        log.info("username : {}", authentication.getName());
        return "welcome";
    }

    @PostMapping("/signup")
    @ResponseBody
    public String signup(@RequestBody User user) {
        return  customUserDetailsService.signup(user).getUsername();
    }

    @GetMapping("/mfa/authenticator")
    public String mfa(@RequestParam String token  , Model model, HttpServletRequest request) throws UnsupportedEncodingException {
        String str = new String(Base64.getUrlDecoder().decode(token));
        final String username = str.split(":")[0];
        final  String password = str.split(":")[1];
        model.addAttribute("qr",generateQrCode.generate(customUserDetailsService.findById(username)));
        model.addAttribute("username", username);
        model.addAttribute("password", password);
        model.addAttribute("message", request.getParameter("message"));
        return "mfa";
    }


    @PostMapping("/validate/mfa/authenticator")
    public String validateMfa(Authentication authentication, Model model, @RequestParam String code) throws UnsupportedEncodingException {
        Totp totp = new Totp(customUserDetailsService.findById(authentication.getName()).getSecrete());
        if(!generateQrCode.isValidLong(code) || !totp.verify(code)) {
            model.addAttribute("error", "Code is invalid.");
            model.addAttribute("qr",generateQrCode.generate(customUserDetailsService.findById(authentication.getName())));
            return "mfa";
        }
        return "welcome";
    }

}
