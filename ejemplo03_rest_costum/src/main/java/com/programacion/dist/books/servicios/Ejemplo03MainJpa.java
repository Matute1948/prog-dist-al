package com.programacion.dist.books.servicios;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import com.programacion.dist.books.db.Post;

public class Ejemplo03MainJpa {
    public static void main(String[] args) {
       EntityManagerFactory emf = Persistence.createEntityManagerFactory("post-unit");
        EntityManager em = emf.createEntityManager();

        var post1 = em.find(Post.class,1);
        System.out.println(post1);

    }
}
