package org.rajcreate.java.spring.ticketplatform.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.rajcreate.java.spring.ticketplatform.model.Note;
import org.rajcreate.java.spring.ticketplatform.model.Ticket;
import org.rajcreate.java.spring.ticketplatform.repository.NoteRepository;
import org.rajcreate.java.spring.ticketplatform.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TicketService {

    // Inietto la repository per utilizzarne i metodi concretizzati dalla IoC
    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private NoteRepository noteRepository;

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
        // Aggiorniamo la data di modifica
        ticket.setLastUpdate(LocalDate.now());

        return ticketRepository.save(ticket);
    }

    // Cancellazione
    public void deletebyId(Integer id){
        Ticket ticket = ticketRepository.findById(id).get();

        // cancellazione delle note associate al ticket
        for(Note n : ticket.getNote()){
            noteRepository.deleteById(n.getId());
        }

        ticketRepository.deleteById(id);
    }
}
