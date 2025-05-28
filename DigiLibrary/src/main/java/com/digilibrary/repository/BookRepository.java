package com.digilibrary.repository;

import com.digilibrary.model.Book;
import java.util.List;
import java.util.Optional;

/**
 * Kitap verilerine erişim için repository arayüzü.
 */
public interface BookRepository {
    /**
     * Kitabı kaydeder veya günceller.
     * 
     * @param book Kaydedilecek kitap
     * @return Kaydedilen kitap
     */
    Book save(Book book);
    
    /**
     * ID'ye göre kitap arar.
     * 
     * @param id Kitap ID'si
     * @return Bulunan kitap, yoksa boş Optional
     */
    Optional<Book> findById(String id);
    
    /**
     * Tüm kitapları listeler.
     * 
     * @return Kitap listesi
     */
    List<Book> findAll();
    
    /**
     * Yazara göre kitapları arar.
     * 
     * @param author Yazar adı
     * @return Yazara ait kitap listesi
     */
    List<Book> findByAuthor(String author);
    
    /**
     * Türe göre kitapları arar.
     * 
     * @param genre Kitap türü
     * @return Belirtilen türdeki kitap listesi
     */
    List<Book> findByGenre(String genre);
    
    /**
     * Kitabı siler.
     * 
     * @param id Silinecek kitabın ID'si
     */
    void delete(String id);
}
