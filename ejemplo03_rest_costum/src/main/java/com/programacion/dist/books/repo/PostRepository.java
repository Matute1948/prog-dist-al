package com.programacion.dist.books.repo;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import com.programacion.dist.books.db.Post;

import java.util.List;
import java.util.Optional;


@ApplicationScoped
public class PostRepository {
    @Inject
    EntityManager em;

    public List<Post> findAll(){
        return  em.createQuery("select o from Post p", Post.class)
                .getResultList();

    }

    public Optional<Post> findById(Integer id){
        var ret = em.find(Post.class, 1);
        return Optional.ofNullable(ret);
    }
}

