package org.rajcreate.java.spring.ticketplatform.service;

import java.util.List;
import java.util.Optional;

import org.rajcreate.java.spring.ticketplatform.model.Category;
import org.rajcreate.java.spring.ticketplatform.model.Ticket;
import org.rajcreate.java.spring.ticketplatform.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    // Metodo per raccogliere tutte le categorie
    public List<Category> findAllCategories(){
        return categoryRepository.findAll();        
    }

    // Ricerca categoria per ID
    public Optional<Category> findCategoryById(Integer id){
        return categoryRepository.findById(id);
    }

    // Metodo per creare una categoria
    public Category create(Category category){
        return categoryRepository.save(category);
    }

    // Metodo per eliminare una categoria in base all'ID
    public void deleteById(Integer id){

        // Rimuoviamo eventuali ticket associati
        Category cat = categoryRepository.findById(id).get();
        for(Ticket t : cat.getTicket()){
            t.setCategory(null);
        }

        // Eliminiamo la categoria
        categoryRepository.deleteById(id);
    }
}
