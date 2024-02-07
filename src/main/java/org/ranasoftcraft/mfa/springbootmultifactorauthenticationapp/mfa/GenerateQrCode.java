package org.ranasoftcraft.mfa.springbootmultifactorauthenticationapp.mfa;

import org.ranasoftcraft.mfa.springbootmultifactorauthenticationapp.secuity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Component
public class GenerateQrCode {

    public static final  String QR_PREFIX =
            "https://chart.googleapis.com/chart?chs=200x200&chld=M%%7C0&cht=qr&chl=";

    @Value("${app.name}")
    private String appName;

    public String generate(final User user) throws UnsupportedEncodingException {
        return QR_PREFIX + URLEncoder.encode(String.format(
                        "otpauth://totp/%s:%s?secret=%s&issuer=%s",
                        appName, user.getUsername(), user.getSecrete(), appName),
                "UTF-8");
    }


    public boolean isValidLong(String code) {
        try {
            Long.parseLong(code);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
