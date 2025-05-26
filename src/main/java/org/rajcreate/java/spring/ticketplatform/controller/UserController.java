package org.rajcreate.java.spring.ticketplatform.controller;

import java.util.List;
import java.util.Optional;

import org.rajcreate.java.spring.ticketplatform.model.Ticket;
import org.rajcreate.java.spring.ticketplatform.model.User;
import org.rajcreate.java.spring.ticketplatform.repository.TicketRepository;
import org.rajcreate.java.spring.ticketplatform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;



@Controller
@RequestMapping("/operator")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TicketRepository ticketRepository;

    // Pagina personale operatore
    @GetMapping
    public String index(Authentication authentication, Model model){
        // diamo la possibilità di vedere il nome nella pagina
        model.addAttribute("email", authentication.getName());

        // Recupero l'utente loggato
        Optional<User> optUser = userRepository.findByUsername(authentication.getName());

        // Recupero la lista di ticket assegnati all'utente
        List<Ticket> tickets = ticketRepository.findByOperatore(optUser.get());

        model.addAttribute("user", optUser.get());
        model.addAttribute("ticketList", tickets);
        // model.addAttribute("canBeUnavailable", !hasOpenTickets);

        return "/operator/index";
    }

    @PostMapping("/toggle")
    public String toggleAvailability(Authentication authentication, RedirectAttributes redirectAttributes) {
        
        // Recupero l'utente loggato
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).get();
        
        // Recupero la lista di ticket assegnati all'operatore
        List<Ticket> tickets = ticketRepository.findByOperatore(user);

        // Controllo se ci sono ticket "Da fare" o "In corso"
        boolean hasOpenTickets = false;
        for(Ticket ticket : tickets){
            if(ticket.getTicketStatus().equals("Da fare") || ticket.getTicketStatus().equals("In corso")){
                hasOpenTickets = true;
                break;
            }
        }

        // Se l'utente è già non disponibile e non ha ticket aperti, lo riattivo
        if(!hasOpenTickets && !user.isDisponibile()){
            user.setDisponibile(true);
            userRepository.save(user);
            redirectAttributes.addFlashAttribute("successMessage", "Ora sei segnato come disponibile.");
        }

        // Se è disponibile e non ha ticket aperti, lo disattivo
        else if(!hasOpenTickets){
            user.setDisponibile(false);
            userRepository.save(user);
            redirectAttributes.addFlashAttribute("successMessage", "Ora sei segnato come non disponibile.");
        }

        // Se ha ticket aperti
        else {
            redirectAttributes.addFlashAttribute("errorMessage", "Non puoi modificare lo stato: hai ancora ticket attivi.");
        }


        return "redirect:/operator";
    }
    
}
