package com.digilibrary.service;

import com.digilibrary.model.Book;
import com.digilibrary.repository.BookRepository;
import java.util.List;
import java.util.Optional;

/**
 * Kitap işlemlerini yöneten servis sınıfı.
 */
public class BookService {
    private final BookRepository bookRepository;
    
    /**
     * Yapıcı metod.
     * 
     * @param bookRepository Kitap repository'si
     */
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }
    
    /**
     * Yeni kitap ekler.
     * 
     * @param book Eklenecek kitap
     * @return Eklenen kitap
     */
    public Book addBook(Book book) {
        return bookRepository.save(book);
    }
    
    /**
     * Kitap bilgilerini günceller.
     * 
     * @param book Güncellenecek kitap
     * @return Güncellenen kitap
     */
    public Book updateBook(Book book) {
        return bookRepository.save(book);
    }
    
    /**
     * ID'ye göre kitap getirir.
     * 
     * @param id Kitap ID'si
     * @return Bulunan kitap, yoksa boş Optional
     */
    public Optional<Book> getBookById(String id) {
        return bookRepository.findById(id);
    }
    
    /**
     * Tüm kitapları listeler.
     * 
     * @return Kitap listesi
     */
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
    
    /**
     * Yazara göre kitapları listeler.
     * 
     * @param author Yazar adı
     * @return Yazara ait kitap listesi
     */
    public List<Book> getBooksByAuthor(String author) {
        return bookRepository.findByAuthor(author);
    }
    
    /**
     * Türe göre kitapları listeler.
     * 
     * @param genre Kitap türü
     * @return Belirtilen türdeki kitap listesi
     */
    public List<Book> getBooksByGenre(String genre) {
        return bookRepository.findByGenre(genre);
    }
    
    /**
     * Kitabı siler.
     * 
     * @param id Silinecek kitabın ID'si
     */
    public void deleteBook(String id) {
        bookRepository.delete(id);
    }
}
