package org.rajcreate.java.spring.ticketplatform.security;



import java.util.List;
import java.util.Optional;

import org.rajcreate.java.spring.ticketplatform.model.User;
import org.rajcreate.java.spring.ticketplatform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class DatabaseUserDetailsService implements UserDetailsService{

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Eseguiamo la query per recuperare l'utente dal suo username
        // utilizzando il repository di User iniettato in precedenza
        // Lo salviamo nella variabile optUser
        Optional<User> optUser = userRepository.findByUsername(username);

        // Se l'username è presente, viene restituita un'istanza di DatabaseUserDetails
        // passando nel costruttore optUser perché accetta un oggetto di tipo User
        if(optUser.isPresent()) {
            return new DatabaseUserDetails(optUser.get());
        } else {
            throw new UsernameNotFoundException("Username not found");
        }
    }

    // Ricerca di operatori disponibili
    public List<User> findAvailableOperators() {
        return userRepository.findAvailableOperators();
    }
}
