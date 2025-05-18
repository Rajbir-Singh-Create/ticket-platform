package org.rajcreate.java.spring.ticketplatform.repository;

import org.rajcreate.java.spring.ticketplatform.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer>{

}
