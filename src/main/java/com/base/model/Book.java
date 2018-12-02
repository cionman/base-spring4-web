package com.base.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

public class Book {
    private String bookId;

    @NotNull
    @Size(min = 3)
    private String name;

    @DateTimeFormat(iso=DateTimeFormat.ISO.DATE)
    private LocalDate publishDate;


    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(LocalDate publishDate) {
        this.publishDate = publishDate;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Book{");
        sb.append("bookId='").append(bookId).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", publishDate=").append(publishDate);
        sb.append('}');
        return sb.toString();
    }

}
