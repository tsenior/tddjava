package com.tinyiko.tdd.tdddemoapp.service;

import com.tinyiko.tdd.tdddemoapp.customexception.BookNotFoundException;
import com.tinyiko.tdd.tdddemoapp.dto.BookRequest;
import com.tinyiko.tdd.tdddemoapp.model.Book;
import com.tinyiko.tdd.tdddemoapp.resource.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(long id) {
        Optional<Book> requestedBook = bookRepository.findById(id);

        if (requestedBook.isEmpty()) {
            throw new BookNotFoundException(String.format("Book with id: '%s' not found",id));
        }

        return requestedBook.get();
    }

    public Book updateBook(Long id, BookRequest updateBookRequest) {

        Optional<Book> bookFromDatabase = bookRepository.findById(id);

        if (bookFromDatabase.isEmpty()) {
            throw new BookNotFoundException(String.format("Book with id: '%s' not found",id));
        }

        Book bookToUpdate = bookFromDatabase.get();
        bookToUpdate.setAuthor(updateBookRequest.getAuthor());
        bookToUpdate.setIsbn(updateBookRequest.getIsbn());
        bookToUpdate.setTitle(updateBookRequest.getTitle());

        return bookToUpdate;
    }
}
