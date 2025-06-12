package com.programacion.dist.authors.db;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter

public class BookAuthorId {
    @Column(name = "books_isbn")
    private String booksIsbn;

    @Column(name = "authors_id")
    private Integer authorsId;
}
