package com.tinyiko.tdd.tdddemoapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinyiko.tdd.tdddemoapp.controller.BookController;
import com.tinyiko.tdd.tdddemoapp.dto.BookRequest;
import com.tinyiko.tdd.tdddemoapp.model.Book;
import com.tinyiko.tdd.tdddemoapp.service.BookServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookServiceImpl bookService;

    @Captor
    private ArgumentCaptor<BookRequest> argumentCaptor;

    @Test
    public void postingANewBookShouldCreateANewBookInTheDatabase() throws Exception {

        BookRequest bookRequest = new BookRequest();
        bookRequest.setAuthor("Duke");
        bookRequest.setTitle("data structures and algorithm");
        bookRequest.setIsbn("111167");

        when(bookService.createNewBook(argumentCaptor.capture())).thenReturn(1L);

        this.mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", "http://localhost/api/books/1"));

        assertThat(argumentCaptor.getValue().getAuthor(), is("Duke"));
        assertThat(argumentCaptor.getValue().getIsbn(), is("111167"));
        assertThat(argumentCaptor.getValue().getTitle(), is("data structures and algorithm"));
    }

    @Test
    public void allbooksEndPointShouldReturnTwoBooks(){
        when(bookService.getAllBooks()).thenReturn((Book) List.of(createBook(1L, "Java 11", "Duke", "187993286"),
                createBook(2L, "Java 11", "Duke", "187993286")));
    }

    private Book createBook(long id, String title, String author, String isbn) {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setIsbn(isbn);
        book.setId(id);

        return book;
    }


}
