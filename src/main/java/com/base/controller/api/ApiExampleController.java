package com.base.controller.api;

import com.base.model.Book;
import com.base.service.example.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController //REST 용 컨트롤러
@RequestMapping("/api/example")
public class ApiExampleController {
    @Autowired
    BookService bookService;

    @RequestMapping(path="{bookId}", method = RequestMethod.GET)
    public Book getBook(@PathVariable String bookId)

    {
        return bookService.find(bookId);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> CreateBook(@Validated @RequestBody Book newBook){
        bookService.create(newBook);
        String resourceUri = "http://localhost:8080/api/example/" + newBook.getBookId();
        return ResponseEntity.created(URI.create(resourceUri)).build();
    }

    @RequestMapping(path="{bookId}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void put(
            @PathVariable String bookId
            , @Validated @RequestBody Book updateBook
    ){
        bookService.update(updateBook);
    }

    @RequestMapping(path="{bookId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String bookId){
        bookService.delete(bookId);
    }

    @RequestMapping(method=RequestMethod.GET)
    public List<Book> searchBooks(@Validated Book book){
        return bookService.searchBooks(book);
    }
}
