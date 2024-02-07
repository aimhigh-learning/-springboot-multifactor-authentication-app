package org.ranasoftcraft.mfa.springbootmultifactorauthenticationapp.config;

import org.ranasoftcraft.mfa.springbootmultifactorauthenticationapp.secuity.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.DispatcherType;
/**
 * @author sandeep.rana
 */
@EnableWebSecurity
@Configuration
public class SecurityConfig {


    private CustomUserDetailsService customUserDetailsService;

    private  CustomWebAuthenticationDetailsSource customWebAuthenticationDetailsSource;

    @Autowired
    public void setCustomUserDetailsService(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Autowired
    public void setCustomWebAuthenticationDetailsSource(CustomWebAuthenticationDetailsSource customWebAuthenticationDetailsSource) {
        this.customWebAuthenticationDetailsSource = customWebAuthenticationDetailsSource;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers("/login","/signup","/mfa/authenticator").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .authenticationDetailsSource(customWebAuthenticationDetailsSource)
                .loginPage("/login")
                .loginProcessingUrl("/process-login")
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/welcome")
                .failureHandler(authenticationFailureHandler())
//                .failureUrl("/login?error=true")
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
                .and()
                .csrf()
                .disable();

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) ->{
            web.ignoring().antMatchers("/login","/webjars/**","/static/*","/mfa/authenticator");
        };
    }


    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }


    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(daoAuthenticationProvider())
                .build();
    }


    @Bean
    public CustomUserDetailsAuthenticationProvider daoAuthenticationProvider() {
        return new CustomUserDetailsAuthenticationProvider(passwordEncoder(), customUserDetailsService);
    }

}
