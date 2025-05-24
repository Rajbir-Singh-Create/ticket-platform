package org.rajcreate.java.spring.ticketplatform.repository;

import java.util.Optional;

import org.rajcreate.java.spring.ticketplatform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer>{

    public Optional<User> findByEmail(String email);
}
