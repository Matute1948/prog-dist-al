package com.programacion.dist.authors;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import io.vertx.ext.consul.ConsulClientOptions;
import io.vertx.ext.consul.ServiceOptions;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.ext.consul.ConsulClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.net.InetAddress;
import java.util.UUID;

@ApplicationScoped
public class AuthorsLifeCicle {

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

        //creamos el consul client
        ConsulClientOptions options = new ConsulClientOptions()
                .setHost(consulHost)
                .setPort(consulPort);

        ConsulClient consulClient = ConsulClient.create(vertx,options);

        serviceId = UUID.randomUUID().toString();
        var ipAddress = InetAddress.getLoopbackAddress();

        //registro
        ServiceOptions serviceOption = new ServiceOptions()
                .setName("app-authors")
                .setId(serviceId)
                .setAddress(ipAddress.getHostAddress())
                .setPort(appPort);

        consulClient.registerServiceAndAwait(serviceOption);


    }

    //cuando pare la app
    void stop(@Observes ShutdownEvent event, Vertx vertx){

        ConsulClientOptions options = new ConsulClientOptions()
                .setHost(consulHost)
                .setPort(consulPort);

        ConsulClient consulClient = ConsulClient.create(vertx,options);

        consulClient.deregisterServiceAndAwait(serviceId);

    }
}
