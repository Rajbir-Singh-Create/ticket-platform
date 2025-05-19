package org.rajcreate.java.spring.ticketplatform.repository;

import org.rajcreate.java.spring.ticketplatform.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<Note, Integer> {

}
