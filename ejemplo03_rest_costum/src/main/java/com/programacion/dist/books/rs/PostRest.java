package com.programacion.dist.books.rs;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import com.programacion.dist.books.db.Post;
import com.programacion.dist.books.repo.PostRepository;

import java.util.List;

@Path("/posts")
@Produces({MediaType.APPLICATION_JSON})

public class PostRest {

    @Inject
    PostRepository repository;

    @GET
    public List<Post> findAll(){
        return List.of();
    }

    @GET
    @Path("/{postID}")
    public Post findById(@PathParam("postID") Integer id){
        return null;
    }
}
