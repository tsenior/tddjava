package com.tinyiko.tdd.tdddemoapp.service;

import com.tinyiko.tdd.tdddemoapp.dto.BookRequest;
import com.tinyiko.tdd.tdddemoapp.model.Book;
import com.tinyiko.tdd.tdddemoapp.resource.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService{

    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository){
        this.bookRepository = bookRepository;
    }

    public Long createNewBook(BookRequest bookRequest) {
        Book book = new Book();
        book.setAuthor(bookRequest.getAuthor());
        book.setIsbn(bookRequest.getIsbn());
        book.setTitle(bookRequest.getTitle());

        book = bookRepository.save(book);

        return book.getId();
    }

    public Book getAllBooks() {
        return null;
    }
}
