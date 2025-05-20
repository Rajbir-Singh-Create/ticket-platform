package org.rajcreate.java.spring.ticketplatform.controller.api;

import java.util.List;

import org.rajcreate.java.spring.ticketplatform.model.Ticket;
import org.rajcreate.java.spring.ticketplatform.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/ticket")
public class TicketRestApiController {

    @Autowired
    private TicketService ticketService;

    // Metodo per visualizzare l'elenco dei ticket - filtra anche per titolo
    @GetMapping
    public ResponseEntity<List<Ticket>> index(@RequestParam(name="keyword", required = false) String title) {
        List<Ticket> tickets = ticketService.findTicketList(title);
        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }

    // Metodo per filtrare l'elenco dei ticket per stato
    @GetMapping("/status")
    public ResponseEntity<List<Ticket>> findByStatus(@RequestParam(name="keyword", required = false) String status) {
        List<Ticket> ticketStatus = ticketService.findTicketByStatus(status);
        return new ResponseEntity<>(ticketStatus, HttpStatus.OK);
    }
    
    // Metodo per filtrare l'elenco dei ticket per categoria
    @GetMapping("/category")
    public ResponseEntity<List<Ticket>> findByCategory(@RequestParam(name="keyword", required = false) String category){
        List<Ticket> ticketCategory = ticketService.findTicketByCategory(category);
        return new ResponseEntity<>(ticketCategory, HttpStatus.OK);
    }

}
