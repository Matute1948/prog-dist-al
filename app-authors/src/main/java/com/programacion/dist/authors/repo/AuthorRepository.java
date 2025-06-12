package com.programacion.dist.authors.repo;

import com.programacion.dist.authors.db.Author;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
@Transactional
public class AuthorRepository implements PanacheRepositoryBase<Author,Integer> {

    public List<Author> findByBook(String isbn) {
        return this.list("SELECT o.author FROM BookAuthor o where o.id.booksIsbn=?1",isbn);
    }


}
