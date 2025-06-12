package com.programacion.dist.authors.db;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "books_authors")
public class BookAuthor {

    @EmbeddedId
    private BookAuthorId id;

    @ManyToOne
    @MapsId("authorsId")
    @JoinColumn(name = "authors_id", nullable=false)
    private Author author;
}
