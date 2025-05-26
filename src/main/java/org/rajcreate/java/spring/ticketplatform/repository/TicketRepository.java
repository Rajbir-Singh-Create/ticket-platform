package org.rajcreate.java.spring.ticketplatform.repository;

import java.util.List;

import org.rajcreate.java.spring.ticketplatform.model.Ticket;
import org.rajcreate.java.spring.ticketplatform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface TicketRepository extends JpaRepository<Ticket, Integer> {

    // Ricerca ticket per stringa di testo sul titolo
    public List<Ticket> findByTitleContainingIgnoreCase(String title);

    // Ricerca ticket per stringa di testo sullo status
    public List<Ticket> findByTicketStatus(String ticketStatus);

    // Ricerca tickets per categoria
    @Query(value = "SELECT t.* FROM ticket AS t JOIN category c ON t.category_id = c.id WHERE c.category_name LIKE ?", nativeQuery = true)
    List<Ticket> findTicketByCategoryName(String categoryName);

    // Ricerca ticket in base all'operatore
    List<Ticket> findByOperatore(User user);
    
}
