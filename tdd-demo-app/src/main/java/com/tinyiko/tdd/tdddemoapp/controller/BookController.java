package com.tinyiko.tdd.tdddemoapp.controller;

import com.tinyiko.tdd.tdddemoapp.dto.BookRequest;
import com.tinyiko.tdd.tdddemoapp.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @PostMapping
    public ResponseEntity<Void> createNewBook(@Valid @RequestBody BookRequest bookRequest,
                                              UriComponentsBuilder uriComponentsBuilder){

        Long id = bookService.createNewBook(bookRequest);

        UriComponents uriComponents = uriComponentsBuilder.path("/api/books/{id}").buildAndExpand(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(uriComponents.toUri());

        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }
}
