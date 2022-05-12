package com.tinyiko.tdd.tdddemoapp.service;

import com.tinyiko.tdd.tdddemoapp.dto.BookRequest;
import com.tinyiko.tdd.tdddemoapp.model.Book;

import java.util.List;


public interface BookService {

    Long createNewBook(BookRequest bookRequest);
    List<Book> getAllBooks();
    Book getBookById(long id);
    Book updateBook(Long id, BookRequest updateBookRequest);
}
