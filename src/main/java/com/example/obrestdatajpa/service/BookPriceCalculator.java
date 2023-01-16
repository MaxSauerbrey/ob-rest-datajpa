package com.example.obrestdatajpa.service;

import com.example.obrestdatajpa.entities.Book;

public class BookPriceCalculator {

    public Double calculatePrice(Book book){
        Double price = book.getPrice();
        if(book.getPages() > 300){
            price +=5;
        }
        price += 2.99;
        return price;
    }
}
