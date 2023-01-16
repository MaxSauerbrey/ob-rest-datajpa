package com.example.obrestdatajpa;

import com.example.obrestdatajpa.entities.Book;
import com.example.obrestdatajpa.repositories.BookRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.time.LocalDate;

@SpringBootApplication
public class ObRestDatajpaApplication {

    public static void main(String[] args) {
       ApplicationContext context =  SpringApplication.run(ObRestDatajpaApplication.class, args);
       BookRepository repository= context.getBean(BookRepository.class);

       //CRUD

        // Crear un libro
       Book libro = new Book(null,"Harry Potter y la piedra filosofal" ,"JK Rowling",375,10d, LocalDate.of(1997,12,1) ,true);
       Book libro1 = new Book(null,"Harry Potter y la camara de los secretos" ,"JK Rowling",425,12d, LocalDate.of(1998,6,15) ,false);
       Book libro2 = new Book(null,"Harry Potter y el prisionero de Azkaban" ,"JK Rowling",525,13d, LocalDate.of(2000,8,1) ,true);

       //Almacenar un libro
        System.out.println("Num libros en base de datos: " + repository.findAll().size());
        repository.save(libro);
        repository.save(libro1);
        repository.save(libro2);

       //Recuperar todos los libros
        System.out.println("Num libros en base de datos: " + repository.findAll().size());

        repository.findAll();
       // Borrar un libro
//        repository.deleteById(3l);
        System.out.println("Num libros en base de datos: " + repository.findAll().size());

    }

}
