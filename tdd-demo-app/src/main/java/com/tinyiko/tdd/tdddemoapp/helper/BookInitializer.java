package com.tinyiko.tdd.tdddemoapp.helper;

import com.github.javafaker.Faker;
import com.tinyiko.tdd.tdddemoapp.model.Book;
import com.tinyiko.tdd.tdddemoapp.resource.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class BookInitializer implements CommandLineRunner {

    private final BookRepository bookRepository;

    BookInitializer(BookRepository bookRepository){
        this.bookRepository = bookRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("populating the sample data records...");

        Faker faker = new Faker();
        int numOfBooksRecords = 10;

        for (int i = 0; i < numOfBooksRecords; i++) {
            Book book = new Book();

            book.setAuthor(faker.book().author());
            book.setTitle(faker.book().title());
            book.setIsbn(UUID.randomUUID().toString());

            bookRepository.save(book);
        }
        log.info("...populated the sample data records");
    }
}
