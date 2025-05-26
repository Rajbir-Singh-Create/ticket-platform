package org.rajcreate.java.spring.ticketplatform.controller;

import java.util.Optional;

import org.rajcreate.java.spring.ticketplatform.model.Note;
import org.rajcreate.java.spring.ticketplatform.model.User;
import org.rajcreate.java.spring.ticketplatform.repository.NoteRepository;
import org.rajcreate.java.spring.ticketplatform.repository.TicketRepository;
import org.rajcreate.java.spring.ticketplatform.repository.UserRepository;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;





@Controller
@RequestMapping("/note")
public class NoteController {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    // CREATE Nota
    @PostMapping("/create")
    public String store(Authentication authentication, @Valid @ModelAttribute("note") Note formNote, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        
        if(bindingResult.hasErrors()){
            model.addAttribute("editMode", false);
            return "/notes/edit";
        }
        
        // Recupera l’utente loggato
        String username = authentication.getName();
        User author = userRepository.findByUsername(username).get();
        formNote.setAuthor(author);

        ticketRepository.save(formNote.getTicket());
        noteRepository.save(formNote);

        redirectAttributes.addFlashAttribute("successMessage", "Nota aggiunta con successo");

        // ritorniamo allo show del ticket tramite il suo ID
        return "redirect:/ticket/show/" + formNote.getTicket().getId();
    }
    
    // UPDATE Nota
    @GetMapping("/edit/{id}")
    public String edit(Authentication authentication, @PathVariable Integer id, Model model) {
        // diamo la possibilità di vedere il nome nella pagina
        model.addAttribute("email", authentication.getName());

        // Recupero la nota
        Note note = noteRepository.findById(id).get();

        model.addAttribute("note", note);
        model.addAttribute("editMode", true);

        return "/notes/edit";
    }
    
    @PostMapping("/edit/{id}")
    public String update(Authentication authentication, @Valid @ModelAttribute("note") Note formNote, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        
        if(bindingResult.hasErrors()){
            model.addAttribute("editMode", false);
            return "/notes/edit";
        }

        // Recupera l’utente loggato
        String username = authentication.getName();
        User author = userRepository.findByUsername(username).get();
        formNote.setAuthor(author);

        ticketRepository.save(formNote.getTicket());
        noteRepository.save(formNote);

        redirectAttributes.addFlashAttribute("successMessage", "Nota modificata con successo");
        
        // ritorniamo allo show del ticket tramite il suo ID
        return "redirect:/ticket/show/" + formNote.getTicket().getId();
    }
    
    // DELETE Nota
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        // Recupero la nota dal repository
        Optional<Note> note = noteRepository.findById(id);
        
        // Recupero l'ID del ticket associato prima di eliminare la nota
        Integer ticketId = note.get().getTicket().getId();

        // Elimino la nota
        noteRepository.deleteById(id);

        redirectAttributes.addFlashAttribute("successMessage", "Nota eliminata con successo");

        // Redirect al dettaglio ticket
        return "redirect:/ticket/show/" + ticketId;
    }
    

}
