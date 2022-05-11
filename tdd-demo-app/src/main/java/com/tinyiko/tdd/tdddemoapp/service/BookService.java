package com.tinyiko.tdd.tdddemoapp.service;

import com.tinyiko.tdd.tdddemoapp.dto.BookRequest;


public interface BookService {

    public Long createNewBook(BookRequest bookRequest);
}
