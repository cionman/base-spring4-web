package com.base.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

public class Book implements Serializable {
    private static final long serialVersionUID = 323355870119793038L;
    private String bookId;
    private String name;
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
