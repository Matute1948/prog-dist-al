plugins {
    id("java") // Soporte para proyectos Java.
    id("io.freefair.lombok") version "8.13.1"
    id("io.quarkus") version "3.22.2" // Plugin para compilar y empaquetar apps Quarkus.
}

group = "org.example"
version = "unspecified"

repositories {
    mavenCentral()
}

val quarkusVersion = "3.22.2"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

dependencies {
    //IMPORTANTE EL BOM
    //Es el BOM (Bill of Materials) que asegura que todas las dependencias de Quarkus usen versiones compatibles.
    implementation(enforcedPlatform("io.quarkus.platform:quarkus-bom:${quarkusVersion}"))

    //contenerdor de cdi arc
    implementation("io.quarkus:quarkus-arc") // Contenedor liviano de CDI (inyección de dependencias).

    //REST-API
    implementation("io.quarkus:quarkus-rest") // Implementa JAX-RS para crear servicios RESTful.
    implementation("io.quarkus:quarkus-rest-jsonb") // Serializa objetos a JSON usando JSON-B.

    // Librerias para consumir servicios Client REST
    //Permiten consumir otros servicios REST desde este microservicio como si fueran interfaces
    implementation("io.quarkus:quarkus-rest-client") //Es implmentacion de jax rs
    implementation("io.quarkus:quarkus-rest-client-jsonb")

    //JPA
    implementation("io.quarkus:quarkus-hibernate-orm-panache") // ORM usando Hibernate + Panache para simplificar DAOs.
    implementation("io.quarkus:quarkus-jdbc-postgresql") // Driver JDBC para PostgreSQL.

    //Sirve para mapear fácilmente objetos (ej: de entidad a DTO y viceversa).
    implementation("org.modelmapper:modelmapper:3.2.3")

    //Service Discovery
    implementation("io.quarkus:quarkus-smallrye-stork") // Permite balanceo y descubrimiento de servicios.
    //implementation("io.smallrye.stork:stork-service-discovery-static-list") // Descubrimiento estático (por configuración).
    implementation("io.smallrye.stork:stork-service-discovery-consul")
    //cliente para trabjar con consult de manera reactiva
    implementation("io.smallrye.reactive:smallrye-mutiny-vertx-consul-client")


}

tasks.withType<JavaCompile>{
    options.encoding = "UTF-8"
    options.compilerArgs.add("-parameters")
}