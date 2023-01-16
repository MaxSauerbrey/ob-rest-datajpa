package com.example.obrestdatajpa.service;

import com.example.obrestdatajpa.entities.Book;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class BookPriceCalculatorTest {

    @Test
    void calculatePrice() {

        // Configuracion de la prueba, preparo el escenario de testing
        Book book = new Book(null,"El principito","Saint-Exupéry",111,7D, LocalDate.of(1954,4,15),true);
        Book book1 = new Book(null,"El señor de los anillos","J.R.R. Tolkien",460,10D, LocalDate.of(1954,4,15),true);
        BookPriceCalculator calculator = new BookPriceCalculator();

        //Se ejecuta el comportamiento a testear
        Double price = calculator.calculatePrice(book);
        Double price1 = calculator.calculatePrice(book1);
        System.out.println(price);
        System.out.println(price1);

        //Comprobaciones asersiones
        assertTrue(price>0);
        assertEquals(17.990000000000002,price1);

    }
}