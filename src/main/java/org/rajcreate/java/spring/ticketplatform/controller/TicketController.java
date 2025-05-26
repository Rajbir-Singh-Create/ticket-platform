package org.rajcreate.java.spring.ticketplatform.controller;

import java.util.List;
import java.util.Optional;

import org.rajcreate.java.spring.ticketplatform.model.Category;
import org.rajcreate.java.spring.ticketplatform.model.Note;
import org.rajcreate.java.spring.ticketplatform.model.Ticket;
import org.rajcreate.java.spring.ticketplatform.model.User;
import org.rajcreate.java.spring.ticketplatform.repository.UserRepository;
import org.rajcreate.java.spring.ticketplatform.security.DatabaseUserDetailsService;
import org.rajcreate.java.spring.ticketplatform.service.CategoryService;
import org.rajcreate.java.spring.ticketplatform.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private DatabaseUserDetailsService databaseUserDetailsService;

    @Autowired
    private UserRepository userRepository;

    // READ
    // Dashboard
    @GetMapping
    public String index(Authentication authentication, @RequestParam(name="keyword", required=false) String title, Model model){
        // diamo la possibilità di vedere il nome nella pagina
        model.addAttribute("email", authentication.getName());
        
        model.addAttribute("list", ticketService.findTicketList(title));

        return "/tickets/index";
    }

    // Detail page
    @GetMapping("/show/{id}")
    public String show(Authentication authentication, @PathVariable Integer id, Model model) {
        // diamo la possibilità di vedere il nome nella pagina
        model.addAttribute("email", authentication.getName());

        Optional<Ticket> optTicket = ticketService.findTicketById(id);
        
        if(optTicket.isPresent()){
            model.addAttribute("ticket", optTicket.get());
            return "/tickets/show";
        }
        
        model.addAttribute("errorCause", "Non esiste ticket con id " + id);
        model.addAttribute("errorMessage", "Errore di ricerca ticket");

        return "/error_pages/genericError";
    }
    

    // CREATE
    // GET per la form di creazione ticket
    @GetMapping("/create")
    public String create(Authentication authentication, Model model){
        // diamo la possibilità di vedere il nome nella pagina
        model.addAttribute("email", authentication.getName());

        model.addAttribute("ticket", new Ticket());

        // Carico le categorie
        model.addAttribute("categoryList", categoryService.findAllCategories());

        // Carico gli operatori disponibili
        List<User> availableOperators = databaseUserDetailsService.findAvailableOperators();
        model.addAttribute("operatorList", availableOperators);

        return "/tickets/create";
    }

    // POST per la form di creazione ticket
    @PostMapping("/create")
    public String store(@Valid @ModelAttribute("ticket") Ticket formTicket, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        
        // Logica validazione
        if(bindingResult.hasErrors()){
            // Ricarico le categorie e gli operatori
            model.addAttribute("categoryList", categoryService.findAllCategories());
            List<User> availableOperators = databaseUserDetailsService.findAvailableOperators();
            model.addAttribute("operatorList", availableOperators);

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
    public String edit(Authentication authentication, @PathVariable Integer id, Model model) {
        // diamo la possibilità di vedere il nome nella pagina
        model.addAttribute("email", authentication.getName());

        model.addAttribute("adminRole", true);

        Optional<Ticket> optTicket = ticketService.findTicketById(id);
        
        if(optTicket.isPresent()){
            model.addAttribute("ticket", optTicket.get());
            // Carico le categorie
            model.addAttribute("categoryList", categoryService.findAllCategories());

            // Carico gli operatori disponibili
            List<User> availableOperators = databaseUserDetailsService.findAvailableOperators();
            model.addAttribute("operatorList", availableOperators);
        
            return "/tickets/edit";
        }

        model.addAttribute("errorCause", "Non esiste ticket con id " + id);
        model.addAttribute("errorMessage", "Errore di ricerca ticket");

        return "/error_pages/genericError";
    }
    
    // POST per la form di modifica ticket
    @PostMapping("/edit/{id}")
    public String update(@Valid @ModelAttribute("ticket") Ticket formTicket, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {

        if(bindingResult.hasErrors()){
            // Ricarico le categorie
            model.addAttribute("categoryList", categoryService.findAllCategories());

            // Carico gli operatori disponibili
            List<User> availableOperators = databaseUserDetailsService.findAvailableOperators();
            model.addAttribute("operatorList", availableOperators);

            return "/tickets/edit";
        }

        ticketService.update(formTicket);

        redirectAttributes.addFlashAttribute("successMessage", "Ticket aggiornato con successo");
        
        return "redirect:/ticket";
    }
        
    // DELETE
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        ticketService.deletebyId(id);
        
        redirectAttributes.addFlashAttribute("successMessage", "Ticket eliminato con successo");

        return "redirect:/ticket";
    }

    // GET per aggiunta di note
    @GetMapping("/{id}/note")
    public String note(Authentication authentication, @PathVariable Integer id, Model model) {
        // diamo la possibilità di vedere il nome nella pagina
        model.addAttribute("email", authentication.getName());

        // Creazione nuovo oggetto nota
        Note note = new Note();

        // Passiamo il ticket di riferimento
        note.setTicket(ticketService.findTicketById(id).get());

        model.addAttribute("note", note);
        model.addAttribute("editMode", false);

        return "/notes/edit";
    }
    
    // UPDATE DELLO STATUS DA PARTE DELL'OPERATORE
    // GET per la form di modifica ticket dall'operator
    @GetMapping("/edit/status/{id}")
    public String statusEdit(Authentication authentication, @PathVariable Integer id, Model model) {
        // diamo la possibilità di vedere il nome nella pagina
        model.addAttribute("email", authentication.getName());

        model.addAttribute("adminRole", false);

        Optional<Ticket> optTicket = ticketService.findTicketById(id);
        
        if(optTicket.isPresent()){
            model.addAttribute("ticket", optTicket.get());

            // Carico gli operatori disponibili
            List<User> availableOperators = databaseUserDetailsService.findAvailableOperators();
            model.addAttribute("operatorList", availableOperators);
                    
            return "/tickets/edit";
        }

        model.addAttribute("errorCause", "Non esiste ticket con id " + id);
        model.addAttribute("errorMessage", "Errore di ricerca ticket");

        return "/error_pages/genericError";
    }
    
    // POST per la form di modifica ticket
    @PostMapping("/edit/status/{id}")
    public String statusUpdate(@Valid @ModelAttribute("ticket") Ticket formTicket, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {

        if(bindingResult.hasErrors()){
            // Ricarico le categorie
            model.addAttribute("categoryList", categoryService.findAllCategories());

            return "/tickets/edit";
        }

        // Gestisco l'associazione della categoria se non presente a DB
        if (formTicket.getCategory() != null && formTicket.getCategory().getId() != null) {
            Optional<Category> optCategory = categoryService.findCategoryById(formTicket.getCategory().getId());
            if(optCategory.isPresent()){
                formTicket.setCategory(optCategory.get());
            } else {
                formTicket.setCategory(null);
            }            
        } else {
            formTicket.setCategory(null);
        }

        ticketService.update(formTicket);

        redirectAttributes.addFlashAttribute("successMessage", "Status aggiornato con successo");
        
        return "redirect:/ticket/show/" + formTicket.getId();
    }
}
