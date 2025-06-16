package com.programacion.dist.books;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import io.vertx.ext.consul.CheckOptions;
import io.vertx.ext.consul.ConsulClientOptions;
import io.vertx.ext.consul.ServiceOptions;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.ext.consul.ConsulClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.net.InetAddress;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class BooksLifeCicle {

    @Inject
    @ConfigProperty(name="consul.host", defaultValue="127.0.0.1")
    String consulHost;

    @Inject
    @ConfigProperty(name="consul.port", defaultValue="8500")
    Integer consulPort;

    @Inject
    @ConfigProperty(name="quarkus.http.port")
    Integer appPort;

    String serviceId;

    //cuando arranque la app
    void init(@Observes StartupEvent event, Vertx vertx) throws Exception {

        System.out.println("Iniciando servicio de books...");
        //creamos el consul client
        ConsulClientOptions options = new ConsulClientOptions()
                .setHost(consulHost)
                .setPort(consulPort);

        ConsulClient consulClient = ConsulClient.create(vertx,options);

        serviceId = UUID.randomUUID().toString();
        var ipAddress = InetAddress.getLocalHost();

        //registro
        var tags = List.of(
                "traefik.enable=true",
                "traefik.http.routers.app-books.rule=PathPrefix(`/app-books`)",
                "traefik.http.routers.app-books.middlewares=strip-prefix-books",
                "traefik.http.middlewares.strip-prefix-books.stripprefix.prefixes=/app-books"
        );

        var checkOptions = new CheckOptions()
//                .setHttp("http://127.0.0.1:9090/ping") // Esto estatico
                .setHttp(String.format(("http://%s:%s/ping"), ipAddress.getHostAddress(), appPort))
                .setInterval("10s")
                .setDeregisterAfter("20s");

        ServiceOptions serviceOption = new ServiceOptions()
                .setName("app-books")
                .setId(serviceId)
                .setAddress(ipAddress.getHostAddress())
                .setPort(appPort)
                .setTags(tags)
                .setCheckOptions(checkOptions);

        consulClient.registerServiceAndAwait(serviceOption);


    }

    //cuando pare la app
    void stop(@Observes ShutdownEvent event, Vertx vertx){

        System.out.println("Deteniendo servicio de books...");

        ConsulClientOptions options = new ConsulClientOptions()
                .setHost(consulHost)
                .setPort(consulPort);

        ConsulClient consulClient = ConsulClient.create(vertx,options);

        consulClient.deregisterServiceAndAwait(serviceId);

    }
}
