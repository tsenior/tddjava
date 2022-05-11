package com.tinyiko.tdd.tdddemoapp.resource;

import com.tinyiko.tdd.tdddemoapp.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
}
