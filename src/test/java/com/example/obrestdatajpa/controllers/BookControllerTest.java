package com.example.obrestdatajpa.controllers;

import com.example.obrestdatajpa.entities.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerTest {

    /*
       El testing es fundamental en cualquier proyecto para poder verificar la calidad del codigo
       desarrollado y cumplir los minimos requisitos propuestos al iniciar el proyecto

       Testing manual: Lo realizamos nosotros mismos con una funcion main o probando y ejecutando el codigo
       Es mas costoso ya que es manual y requiere de repetirlo varias veces y la supervisacion de nosotros mismos

       Testing automatizado: Creamos un codigo que testea/prueba nuestro codigo original

       Podemos testear varias capas, podemos testear los controladores, podemos testear los repositorios,
       podemos testear servicios

    */

    private TestRestTemplate testRestTemplate;

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        restTemplateBuilder = restTemplateBuilder.rootUri("http://localhost:" + port);
        testRestTemplate = new TestRestTemplate(restTemplateBuilder);

    }

    @DisplayName("Comprobar hola mundo desde controladores Spring REST")
    @Test
    void hello() {
        //Con el getForEntity lanzo una petición HTTP al puerto seleccionado
        ResponseEntity<String> response = testRestTemplate.getForEntity("/hola", String.class); // me pide la url, y el tipo de respuesta, en este caso string
        //Cuando queremos indicar el tipo de dato colocamos el tipo de dato .class
        //Esto no nos va a devolver directamente un string con el texto, sino que nos va a devolver un
        //Response entity(response), al response entity(response) vamos a poder sacarle datos, ya que es una respuesta HTTP
        //Tiene el status, el body, los headers, etc

        // Lo que nos interesa es comprobar el status, que el status sea ok y no devuelva ningun fallo.
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Hola que tal vamos!!! Hasta luego", response.getBody());
        // Corroboro que el string que devuelve el body sea igual al que devuelve el string del metodo
//        assertEquals(200,response.getStatusCodeValue());

    }

    @Test
    void findAll() {
        ResponseEntity<Book[]> response =
                testRestTemplate.getForEntity("/api/books", Book[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<Book> books = Arrays.asList(response.getBody());
        for (Book book : books) {
            System.out.println(book.toString());

        }
        System.out.println(books.size());
    }

    @Test
    void findOneById() {
        ResponseEntity<Book> response = testRestTemplate.getForEntity("/api/books/1", Book.class); // me pide la url, y el tipo de respuesta, en este caso string
        assertEquals(HttpStatus.OK, response.getStatusCode());
        System.out.println(response.getBody());
    }

    @Test
    void create() {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        String json = """
                    {
                    "title": "Harry Potter y el caliz de fuego",
                    "author": "JK Rowling",
                    "pages": 625,
                    "price": 13.5,
                    "releaseDate": "2002-02-10",
                    "online": true 
                        }
                """;

        HttpEntity<String> request = new HttpEntity<>(json, headers);

        ResponseEntity<Book> response = testRestTemplate.exchange("/api/books", HttpMethod.POST, request, Book.class);

        Book result = response.getBody();
//        assertEquals(4L, result.getId());
        assertEquals("Harry Potter y el caliz de fuego", result.getTitle());
    }


    @Test
    void update() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        String json = """
                {
                      "id": 3,
                      "title": "Harry poster",
                      "author": "JK Rowling",
                      "pages": 625,
                      "price": 13.5,
                      "releaseDate": "2002-02-10",
                      "online": true
                 }
                  """;
        HttpEntity<String> request = new HttpEntity<>(json, headers);
        ResponseEntity<Book> response = testRestTemplate.exchange("/api/books", HttpMethod.PUT, request, Book.class);
        Book resultado = response.getBody();
        System.out.println(resultado.getId());
//        assertEquals(2l, resultado.getId());

        assertEquals("Harry poster", resultado.getTitle());

    }

    @Test
    void delete() {
        ResponseEntity<Book> response = testRestTemplate.getForEntity("/api/books/1", Book.class);
        Book book = response.getBody();
        testRestTemplate.delete("/api/books/1", book, "/api/books/{id}");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        findAll();
    }

    @Test
    void deleteAll() {
        ResponseEntity<Book[]> response = testRestTemplate.getForEntity("/api/books", Book[].class);

        if (response.getBody() != null) {
            testRestTemplate.delete("/api/books");
            assertEquals(HttpStatus.OK, response.getStatusCode());
            System.out.println("OKKKKK");
        } else {
            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
            System.out.println("NO OKK");
        }
    }

}