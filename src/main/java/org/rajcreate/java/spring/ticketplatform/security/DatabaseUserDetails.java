package org.rajcreate.java.spring.ticketplatform.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.rajcreate.java.spring.ticketplatform.model.Role;
import org.rajcreate.java.spring.ticketplatform.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class DatabaseUserDetails implements UserDetails {

    private final Integer id;
    private final String email;
    private final String password;
    private final List<GrantedAuthority> authorities;

    public DatabaseUserDetails(User user){
        // prendiamo i dati dal DB e li mettiamo nelle variabili di istanza
        this.id = user.getId();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.authorities = new ArrayList<>();

        // Cicliamo sui ruoli per convertire da oggetto Role a GrantedAuthority
        // perché Spring parla con questo tipo di oggetto
        // Convertiamo il concetto di ruolo in un concetto di Authority che può capire
        for (Role ruolo : user.getRoles()){
            this.authorities.add(new SimpleGrantedAuthority(ruolo.getRoleName()));
        }
    }

    // Restituzione dei dati tramite i seguenti metodi
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword(){
        return this.password;
    }

    @Override
    public String getUsername(){
        return this.email;
    }

}
