package com.base.controller.api;

import com.base.model.Book;
import com.base.service.example.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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



}
