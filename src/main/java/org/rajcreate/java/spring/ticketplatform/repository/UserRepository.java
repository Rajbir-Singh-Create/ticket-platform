package org.rajcreate.java.spring.ticketplatform.repository;

import java.util.List;
import java.util.Optional;

import org.rajcreate.java.spring.ticketplatform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Integer>{

    public Optional<User> findByEmail(String email);

    public Optional<User> findByUsername(String username);

    // Riceca di utenti con ruolo operatore e disponibili = true
    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.roleName = 'OPERATOR' AND u.disponibile = true")
    List<User> findAvailableOperators();
}
