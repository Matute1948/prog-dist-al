package com.programacion.dist.books.config;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.ws.rs.Produces;

@ApplicationScoped
public class JpaConfig {
    private EntityManagerFactory dmf;

    @PostConstruct
    void init(){
        dmf = Persistence.createEntityManagerFactory();
    }

    @Produces
    public EntityManager entityManager(){
        return dmf.createEntityManager();
    }
}
