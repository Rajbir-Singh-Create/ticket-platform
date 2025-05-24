package org.rajcreate.java.spring.ticketplatform.controller;

import org.rajcreate.java.spring.ticketplatform.model.Category;
import org.rajcreate.java.spring.ticketplatform.service.CategoryService;
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

import jakarta.validation.Valid;


@Controller
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    // index gestione categorie
    @GetMapping
    public String index(Authentication authentication, Model model){
        // diamo la possibilità di vedere il nome nella pagina
        model.addAttribute("email", authentication.getName());
        
        // Carichiamo tutte le categorie esistenti
        model.addAttribute("list", categoryService.findAllCategories());

        // Oggetto "vuoto" da passare per creare le categorie direttamente in pagina
        model.addAttribute("categoryObj", new Category());

        return "/categories/index";
    }

    // CREATE category
    @PostMapping("/create")
    public String store(@Valid @ModelAttribute("categoryObj") Category category, BindingResult bindingResult){

        if(!bindingResult.hasErrors()){
            categoryService.create(category);
        }

        return "redirect:/categories";
    }

    // DELETE category
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        categoryService.deleteCategoryById(id);
        
        return "redirect:/categories";
    }
    
}
