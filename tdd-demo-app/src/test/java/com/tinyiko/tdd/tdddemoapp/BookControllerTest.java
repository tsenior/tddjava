package com.tinyiko.tdd.tdddemoapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinyiko.tdd.tdddemoapp.controller.BookController;
import com.tinyiko.tdd.tdddemoapp.customexception.BookNotFoundException;
import com.tinyiko.tdd.tdddemoapp.dto.BookRequest;
import com.tinyiko.tdd.tdddemoapp.model.Book;
import com.tinyiko.tdd.tdddemoapp.service.BookServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


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
    public void allBooksEndPointShouldReturnTwoBooks() throws Exception {
        when(bookService.getAllBooks()).thenReturn(List.of(createBook(
                1L, "Java 11", "Duke", "187993286"),
                createBook(2L, "Java 11", "Duke", "187993286")));

        this.mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title").value("Java 11"))
                .andExpect(jsonPath("$[0].author").value("Duke"))
                .andExpect(jsonPath("$[0].isbn").value("187993286"))
                .andExpect(jsonPath("$[0].id").value(1));

    }

    @Test
    public void getBookWithAnIdShouldReturnOneBook() throws Exception {

        when(bookService.getBookById(1L)).thenReturn(createBook(
                1L, "Java 11", "Duke", "187993286"));

        this.mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Java 11"))
                .andExpect(jsonPath("$.author").value("Duke"))
                .andExpect(jsonPath("$.isbn").value("187993286"))
                .andExpect(jsonPath("$.id").value(1));

    }

    @Test
    public void getBookWithUnknownIdShouldReturn404() throws Exception {

        when(bookService.getBookById(45L)).thenThrow(new BookNotFoundException("no books found"));

        this.mockMvc.perform(get("/api/books/45"))
                .andExpect(status().isNotFound());

    }
    @Test
    public void updateBookWithKnownIdShouldUpdateTheBook() throws Exception{
        BookRequest bookRequest = new BookRequest();
        bookRequest.setAuthor("Ntikelo Aya");
        bookRequest.setIsbn("112233");
        bookRequest.setTitle("Ntikelo");

        when(bookService.updateBook(eq(1L), argumentCaptor.capture()))
                .thenReturn(createBook(1L, "Ntikelo","Ntikelo Aya", "112233"));

        this.mockMvc.perform(put("/api/books/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Ntikelo"))
                .andExpect(jsonPath("$.author").value("Ntikelo Aya"))
                .andExpect(jsonPath("$.isbn").value("112233"))
                .andExpect(jsonPath("$.id").value(1));

        assertThat(argumentCaptor.getValue().getAuthor(), is("Ntikelo Aya"));
        assertThat(argumentCaptor.getValue().getIsbn(), is("112233"));
        assertThat(argumentCaptor.getValue().getTitle(), is("Ntikelo"));


    }

    @Test
    public void updateBookWithUnknownIdShouldReturn404() throws Exception{
        BookRequest bookRequest = new BookRequest();
        bookRequest.setAuthor("Ntikelo Aya");
        bookRequest.setIsbn("112233");
        bookRequest.setTitle("Ntikelo");

        when(bookService.updateBook(eq(47L), argumentCaptor.capture()))
                .thenThrow(new BookNotFoundException("Book not found"));

        this.mockMvc.perform(put("/api/books/47")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookRequest)))
                .andExpect(status().isNotFound());

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
