package com.team17.backend.Greeting.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.team17.backend.Greeting.model.Greeting;

public interface GreetingRepository extends JpaRepository<Greeting, Long> {

    // public List<Book> findAllByOrderByPriceDesc();

    // public List<Book> findBooksByPriceGreaterThan(Double price);
    // public List<Book> findBooksByPriceLessThan(Double price);

    // public Book findBookByTitle(String title);
    // public List<Book> findBooksByinColorIsTrue();

    // public Book deleteById(long id);
    // public Book deleteByTitle(String title);

    // public Book findBookById(long id);

    // public List<Greeting> findAll(Sort sort);
}