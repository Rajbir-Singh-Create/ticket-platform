package org.rajcreate.java.spring.ticketplatform.controller;

import java.util.Optional;

import org.rajcreate.java.spring.ticketplatform.model.Ticket;
import org.rajcreate.java.spring.ticketplatform.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;





@Controller
@RequestMapping("/ticket")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    // READ
    // Dashboard
    @GetMapping
    public String index(@RequestParam(name="keyword", required=false) String title, Model model){
        model.addAttribute("list", ticketService.findTicketList(title));

        return "/tickets/index";
    }

    // Detail page
    @GetMapping("/show/{id}")
    public String show(@PathVariable("id") Integer id, Model model) {
        
        Optional<Ticket> optTicket = ticketService.findTicketById(id);
        
        if(optTicket.isPresent()){
            model.addAttribute("ticket", optTicket.get());
            return "/tickets/show";
        }
        
        model.addAttribute("errorCause", "Non esiste ticket con id " + id);
        model.addAttribute("errorMessage", "Errore di ricerca ticket");

        return "/error_pages/genericError";
    }
    

    // CREAZIONE
    // GET per la form di creazione ticket
    @GetMapping("/create")
    public String create(Model model){

        model.addAttribute("ticket", new Ticket());

        return "/tickets/create";
    }

    // POST per la form di creazione ticket
    @PostMapping("/create")
    public String store(@Valid @ModelAttribute("ticket") Ticket formTicket, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        
        // Logica validazione
        if(bindingResult.hasErrors()){
            // Se il bindingResult ha dato errori, resto nella pagina
            return "/tickets/create";
        }

        // Logica di salvataggio
        ticketService.create(formTicket);
        
        redirectAttributes.addFlashAttribute("successMessage", "Ticket aperto con successo");
        return "redirect:/ticket";
    }

    // UPDATE
    // GET per la form di modifica ticket
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model) {
        
        model.addAttribute("ticket", ticketService.findTicketById(id).get());
        
        return "/tickets/edit";
    }
    
    // POST per la form di modifica ticket
    @PostMapping("/edit/{id}")
    public String update(@Valid @ModelAttribute("ticket") Ticket formTicket, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        
        if(bindingResult.hasErrors()){
            return "/tickets/edit";
        }

        ticketService.update(formTicket);

        redirectAttributes.addFlashAttribute("successMessage", "Ticket aggiornato con successo");
        
        return "redirect:/ticket";
    }
    
    
    // DELETE
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        ticketService.deletebyId(id);
        
        return "redirect:/ticket";
    }

}
