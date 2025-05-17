package org.rajcreate.java.spring.ticketplatform.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.rajcreate.java.spring.ticketplatform.model.Ticket;
import org.rajcreate.java.spring.ticketplatform.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TicketService {

    // Inietto la repository per utilizzarne i metodi concretizzati dalla IoC
    @Autowired
    private TicketRepository ticketRepository;

    // Metodo per gestire la ricerca dei ticket tramite titolo
    public List<Ticket> findTicketList(String title){
        List <Ticket> result;

        if(title != null && !title.isBlank()){
            result = ticketRepository.findByTitleContainingIgnoreCase(title);
        } else {
            result = ticketRepository.findAll();
        }

        return result;
    }

    // Metodo ricerca per ID
    public Optional<Ticket> findTicketById(Integer id){
        return ticketRepository.findById(id);
    }

    // Salvataggio
    public Ticket create(Ticket ticket){
        return ticketRepository.save(ticket);
    }
    
    // Modifica
    public Ticket update(Ticket ticket){
        Optional<Ticket> optTicket = ticketRepository.findById(ticket.getId());
        
        // Cerchiamo se esiste il ticket
        if(optTicket.isEmpty()){
            throw new IllegalArgumentException("impossibile aggiornare il ticket senza l'ID");
        }

        // Aggiorniamo la data di modifica
        ticket.setLastUpdate(LocalDate.now());

        return ticketRepository.save(ticket);
    }

    // Cancellazione
    public void deletebyId(Integer id){
        // Ticket ticket = ticketRepository.findById(id).get();

        // TODO cancellare le note associate al ticket

        ticketRepository.deleteById(id);
    }
}
