package org.rajcreate.java.spring.ticketplatform.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        // Definizione di come dare l'autorizzazione alle seguenti richieste
        http.authorizeHttpRequests()
                .requestMatchers("/ticket/create").hasAuthority("ADMIN")
                .requestMatchers("/ticket/edit/status/**").hasAnyAuthority("OPERATOR", "ADMIN")
                .requestMatchers("/ticket/edit/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.POST, "/ticket/edit/status/**").hasAnyAuthority("OPERATOR", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/ticket/**").hasAuthority("ADMIN")
                .requestMatchers("/categories", "/categories/**").hasAuthority("ADMIN")
                .requestMatchers("/ticket", "/ticket/**").hasAnyAuthority("OPERATOR", "ADMIN")
                .requestMatchers("/operator", "/operator/**").hasAnyAuthority("OPERATOR", "ADMIN")
                .requestMatchers("/**").permitAll()
                .and().formLogin()
                .and().logout()
                .and().exceptionHandling()
                .and().csrf().disable();

        return http.build();
    }

    // Definiamo come deve inizializzare l'oggetto
    @Bean
    DatabaseUserDetailsService userDetailsService() {
        return new DatabaseUserDetailsService();
    }

    // Definiamo un password encoder
    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        // Settiamo lo userDetailsService che contiene il metodo per caricare l'utente
        // restituendo tutte le informazioni dettagliate
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

}
