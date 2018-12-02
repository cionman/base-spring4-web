package com.base.service.example;

import com.base.model.Book;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class BookService {
    private final Map<String, Book> bookRepository = new ConcurrentHashMap<>();

    @PostConstruct
    public  void loadDummyData(){
        Book book = new Book();
        book.setBookId("9791852323");
        book.setName("스프링 철저입문");
        book.setPublishDate(LocalDate.of(2017,8,10));
        bookRepository.put(book.getBookId(), book);
    }

    public  Book find(String bookId){
        return bookRepository.get(bookId);
    }
}
