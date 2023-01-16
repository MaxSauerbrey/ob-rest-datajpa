package com.example.obrestdatajpa.controllers;

import com.example.obrestdatajpa.entities.Book;
import com.example.obrestdatajpa.repositories.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class BookController {

    private final Logger log = LoggerFactory.getLogger(BookController.class);

    //Atributos
    private BookRepository bookRepository;

    //Constructores

    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /*
    Los controladores REST estan pensados para la comunicacion entre dos sistemas de software.
    En si el API REST es una clase de java pensada para ser un punto de comunicacion con nuestra
    aplicación. Otro sistema de software como java, una terminal o un navegador se pueda comunicar
    con esta mediante JSON.

    Quien se comunica con esta API REST seria del lado del navegador mediante javascript, haciendo las
    llamadas a la aplicacion de spring con el objetivo de obtener informacion y luego mostrarsela al
    usuario. Con javascript transformariamos la respuesta JSON en elementos html
    Pero sirve tambien para que otras aplicaciones java u otras tecnologias puedan comunicarse.

    Esta api nos permitira devolver datos de libros
    */

    //CRUD sobre la entidad Book

    //! Buscar todos los libros (lista de libros)

    /**
     * http://localhost:8080/api/books
     *
     * @return
     */
    @GetMapping("/api/books")
    public List<Book> findAll() {
        // Recuperar y devolver los libros de base de datos
        return bookRepository.findAll();
    }

    //! Buscar un solo libro en bdd segund su id

    /**
     * http://localhost:8080/api/books/{id}
     *
     * @return
     */
    @GetMapping("/api/books/{id}")
//    @ApiOperation("Buscar un libro por clave primaria id Long")
//    public ResponseEntity<Book> findOneById(@ApiParam("Clave primaria tipo Long") @PathVariable Long id) {

    public ResponseEntity<Book> findOneById(@PathVariable Long id) {
        Optional<Book> bookOpt = bookRepository.findById(id);
        /*
            El objeto optional es como un envoltorio para no tener que trabajar con valores null, que
            causa errores.
            Una vez que tenemos la variable optional, por defecto el objeto optional tiene metodos para
            saber si existe algo adentro de la variable, un objeto de la clase Book o tendra un null.
            Si .isPresent() es true, hay libro  y devolvemos un responseEntity ok.
            Si .isPresent() es false, es null y devolvemos un responseEntity de tipo notFound
         */
        //Option 1
        if (bookOpt.isPresent()) {
            return ResponseEntity.ok(bookOpt.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    //Option 2
//        return bookOpt.map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
//    }

    //! Buscar un solo libro en bdd segund su nombre
//    public Book findBook(Long libro){
//        return bookRepository.findById(libro);
//    }

    //! Crear un nuevo libro en bdd

    /**
     * Metodo POST, no coliciona con finAll porque son diferentes metodos HTTP: GET VS POST
     * El metodo POST HTTP lo utilizamos para recibir datos con el objetivo de crear un objeto.
     *
     * @param book
     * @param headers
     * @return
     */
    @PostMapping("/api/books")
    public ResponseEntity<Book> create(@RequestBody Book book, @RequestHeader HttpHeaders headers) {
        System.out.println(headers.get("Content-Type"));
        //guardar el libro recibido por parametro en la base de datos
        if (book.getId() != null) { //quiere decir que existe el id y por lo tanto no es una creacion
            log.warn("Trying to create a book with id");
            return ResponseEntity.badRequest().build();
        }
        Book result = bookRepository.save(book);
        return ResponseEntity.ok(result); //se genera el libro devuelto, tiene una clave primaria.
    }

    //! Actualizar un libro existente en bdd

    /**
     * El metodo PUT HTTP lo utilizamos para recibir datos con el objetivo de actualizar un objeto.
     *
     * @return
     */
    @PutMapping("/api/books")
    public ResponseEntity<Book> update(@RequestBody Book book) {
        if (book.getId() == null) { // si no tiene id quiere decir que es una creación
            log.warn("Trying to update a non existent book");
            return ResponseEntity.badRequest().build();
        }
        if (!bookRepository.existsById(book.getId())) {
            log.warn("Trying to update a non existent book");
            return ResponseEntity.notFound().build();

        }
        //El proceso de actualizacion
        Book result = bookRepository.save(book);
        return ResponseEntity.ok(result);
    }

    //! Borrar un libro en bdd
//    @ApiIgnore // ignorar este método para que no aparezca en la documentacion de la api Swangger
    @DeleteMapping("/api/books/{id}")
    public ResponseEntity<Book> delete(@PathVariable Long id) {
        if(!bookRepository.existsById(id)) {
            log.warn("Trying to delete a non existent book");
            return ResponseEntity.notFound().build();
        }

        bookRepository.deleteById(id);
        return ResponseEntity.noContent().build();
        //Normalmente cuando se borra algo y deja de existir un contenido
        //devolvemos una respuesta de tipo noContent
    }

    //! Borrar todos los libro en bdd

//    @ApiIgnore // ignorar este método para que no aparezca en la documentacion de la api Swangger
    @DeleteMapping("api/books")
    public ResponseEntity<Book> deleteAll(){
        log.info("REST Request for delete all books");
        bookRepository.deleteAll();
        return ResponseEntity.noContent().build();
    }
}
