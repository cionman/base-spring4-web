package com.base.service.example;

import com.base.common.exception.NotFoundException;
import com.base.model.Book;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class BookService {
    private final Map<String, Book> bookRepository = new ConcurrentHashMap<>();

    @PostConstruct
    public  void loadDummyData(){
        Book book1 = new Book();
        book1.setBookId("9791852323");
        book1.setName("스프링 철저입문");
        book1.setPublishDate(LocalDate.of(2017,8,10));
        bookRepository.put(book1.getBookId(), book1);

        Book book2 = new Book();
        book2.setBookId("35782323");
        book2.setName("감자캐기");
        book2.setPublishDate(LocalDate.of(2017,12,10));
        bookRepository.put(book2.getBookId(), book2);

    }

    public  Book find(String bookId){
        return bookRepository.get(bookId);
    }

    public void create(Book newBook) {
        String bookId = UUID.randomUUID().toString();
        newBook.setBookId(bookId);
        bookRepository.put(bookId, newBook);
    }

    public void update(Book updateBook) {
        bookRepository.put(updateBook.getBookId(), updateBook);
    }

    public void delete(String bookId) {
         Book book = bookRepository.get(bookId);
         if(book == null) throw new NotFoundException();
         bookRepository.remove(bookId);
    }

    public List<Book> searchBooks(Book searchBook) {
        return bookRepository.values().stream()
                .filter(book -> (searchBook.getName() ==null
                                || book.getName().contains(searchBook.getName())) &&
                                (searchBook.getPublishDate() == null
                                || book.getPublishDate().equals(searchBook.getPublishDate()))
                )
                .sorted((o1, o2) -> o1.getPublishDate().compareTo(o2.getPublishDate()))
                .collect(Collectors.toList());
    }
}
