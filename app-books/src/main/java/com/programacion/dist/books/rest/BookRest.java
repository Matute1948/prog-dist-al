package com.programacion.dist.books.rest;

import com.programacion.dist.books.cliente.AuthorRestClient;
import com.programacion.dist.books.db.Book;
import com.programacion.dist.books.dtos.AuthorDto;
import com.programacion.dist.books.dtos.BookDto;
import com.programacion.dist.books.repo.BooksRepository;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.stork.Stork;
import io.smallrye.stork.api.Service;
import io.smallrye.stork.api.ServiceInstance;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.modelmapper.ModelMapper;
import java.util.List;
import java.util.Map;

@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
@Transactional
public class BookRest {

    @Inject
    BooksRepository booksRepository;


    @Inject
    ModelMapper mapper;

//    @Inject
//    @ConfigProperty(name = "authors.url")
//    String authorsURL;

    @Inject
    @RestClient
    private AuthorRestClient client;

    @GET
    @Path("/{isbn}")
    public Response findById(@PathParam("isbn") String isbn) {
        var stork = Stork.getInstance();

        //--listar servicios
        Map<String, Service> services = stork.getServices();

        services.entrySet()
                        .stream()
                        .forEach(it ->{

                                    System.out.println(it.getKey()+" "+it.getValue());

                                    Multi<ServiceInstance> instances = it.getValue()
                                            .getInstances()
                                            .onItem()
                                            .transformToMulti(items -> Multi.createFrom().iterable(items));
                                    instances.subscribe()
                                            .with(item ->{
                                                System.out.println(" "+item.getHost()+":" + item.getPort());
                                            });
                        });

        //--seleccionar una instancia
        Service service = stork.getService("authors-api");
        Uni<ServiceInstance> instance = service.selectInstance();
        instance
                .subscribe()
                .with(inst ->{
                    System.out.println("**Instancia Seleccionada: "+inst.getHost()+":"+inst.getPort());
                });

        BookDto bookDto = new BookDto();
        //1. Buscar el libro
        var obj = booksRepository.findByIdOptional(isbn);
        if(obj.isEmpty()){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        mapper.map(obj.get(), bookDto);


        //3. Buscar authores
 //       var client = ClientBuilder.newClient();
//        AuthorDto[] authorDtos = client.target("http://localhost:8081")
//                .path("authors/find/{isbn}")
//                .resolveTemplate("isbn", isbn)
//                .request(MediaType.APPLICATION_JSON)
//                .get(AuthorDto[].class);
//        bookDto.setAuthors(
//                Stream.of(authorDtos)
//                        .map(AuthorDto::getName)
//                        .toList()
//        );
//          Por el momento no usaremos esto ya que debemos usar el stork
//        client = RestClientBuilder.newBuilder()
//                .baseUri("http://localhost:8081")
//                .build(AuthorRestClient.class);

        var authors = client.findByBookIsbn(isbn)
                .stream()
                .map(AuthorDto::getName)
                .toList();

        bookDto.setAuthors(authors);

        return Response.ok(bookDto).build();

    }

    @GET
    public List<BookDto> findAll(){

        //var client = ClientBuilder.newClient();

        //la diferencia es que nos permite
        // usar un proxy hacia el servicio original
        //Por el momento no ncesitamos esto ya que estamos haciendo una lista estatica
//        client = RestClientBuilder.newBuilder()
//                .baseUri("http://localhost:8081")
//                .build(AuthorRestClient.class);

        return booksRepository.streamAll()
                .map(book -> {
                    var dto = new BookDto();
                    mapper.map(book,dto);
                    return dto;
                })
                .map(book -> {
                    var authors = client.findByBookIsbn(book.getIsbn())
                            .stream()
                            .map(AuthorDto::getName)
                            .toList();
                    book.setAuthors(authors);
                    return book;
                })
                .toList();
    }

    @POST
    public void insert (Book book){
        booksRepository.persist(book);
    }

    @PUT
    @Path("/{isbn}")
    public void update(@PathParam("isbn") String isbn, Book book) {
        booksRepository.update(isbn, book);
    }
}
