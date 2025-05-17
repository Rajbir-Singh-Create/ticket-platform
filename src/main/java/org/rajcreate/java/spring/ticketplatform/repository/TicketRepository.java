package org.rajcreate.java.spring.ticketplatform.repository;

import java.util.List;

import org.rajcreate.java.spring.ticketplatform.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TicketRepository extends JpaRepository<Ticket, Integer> {

    // ricerca tickets per stringa di testo sul titolo
    public List<Ticket> findByTitleContainingIgnoreCase(String title);
}
