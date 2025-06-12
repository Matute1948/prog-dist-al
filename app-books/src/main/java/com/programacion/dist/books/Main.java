package com.programacion.dist.books;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class Main {
    public static void main(String[] args) {
        System.out.println("Running main 2");
        Quarkus.run(args);
    }
}