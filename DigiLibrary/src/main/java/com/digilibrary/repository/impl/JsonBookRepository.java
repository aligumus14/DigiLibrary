package com.digilibrary.repository.impl;

import com.digilibrary.model.Book;
import com.digilibrary.repository.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * BookRepository arayüzünün JSON dosya tabanlı implementasyonu.
 */
public class JsonBookRepository implements BookRepository {
    private final String dataFile;
    private final ObjectMapper objectMapper;
    private List<Book> books;

    /**
     * Yapıcı metod.
     * 
     * @param dataDirectory Veri dizini
     */
    public JsonBookRepository(String dataDirectory) {
        this.dataFile = dataDirectory + "/books.json";
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.books = loadBooks();
    }

    /**
     * Kitapları JSON dosyasından yükler.
     */
    private List<Book> loadBooks() {
        File file = new File(dataFile);
        if (file.exists()) {
            try {
                return objectMapper.readValue(file, 
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Book.class));
            } catch (IOException e) {
                System.err.println("Kitaplar yüklenirken hata oluştu: " + e.getMessage());
            }
        }
        return new ArrayList<>();
    }

    /**
     * Kitapları JSON dosyasına kaydeder.
     */
    private void saveBooks() {
        try {
            File file = new File(dataFile);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            objectMapper.writeValue(file, books);
        } catch (IOException e) {
            System.err.println("Kitaplar kaydedilirken hata oluştu: " + e.getMessage());
        }
    }

    @Override
    public Book save(Book book) {
        Optional<Book> existingBook = findById(book.getId());
        if (existingBook.isPresent()) {
            books.remove(existingBook.get());
        }
        books.add(book);
        saveBooks();
        return book;
    }

    @Override
    public Optional<Book> findById(String id) {
        return books.stream()
                .filter(book -> book.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Book> findAll() {
        return new ArrayList<>(books);
    }

    @Override
    public List<Book> findByAuthor(String author) {
        return books.stream()
                .filter(book -> book.getAuthor() != null && book.getAuthor().contains(author))
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> findByGenre(String genre) {
        return books.stream()
                .filter(book -> book.getGenre() != null && book.getGenre().contains(genre))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(String id) {
        books.removeIf(book -> book.getId().equals(id));
        saveBooks();
    }
}
