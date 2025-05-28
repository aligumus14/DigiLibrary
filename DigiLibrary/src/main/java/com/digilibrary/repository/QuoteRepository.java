package com.digilibrary.repository;

import com.digilibrary.model.Quote;
import java.util.List;
import java.util.Optional;

/**
 * Alıntı verilerine erişim için repository arayüzü.
 */
public interface QuoteRepository {
    /**
     * Alıntıyı kaydeder veya günceller.
     * 
     * @param quote Kaydedilecek alıntı
     * @return Kaydedilen alıntı
     */
    Quote save(Quote quote);
    
    /**
     * ID'ye göre alıntı arar.
     * 
     * @param id Alıntı ID'si
     * @return Bulunan alıntı, yoksa boş Optional
     */
    Optional<Quote> findById(String id);
    
    /**
     * Kullanıcı ID'sine göre alıntıları arar.
     * 
     * @param userId Kullanıcı ID'si
     * @return Kullanıcıya ait alıntı listesi
     */
    List<Quote> findByUserId(String userId);
    
    /**
     * Kitap ID'sine göre alıntıları arar.
     * 
     * @param bookId Kitap ID'si
     * @return Kitaba ait alıntı listesi
     */
    List<Quote> findByBookId(String bookId);
    
    /**
     * Etikete göre alıntıları arar.
     * 
     * @param tag Etiket
     * @return Etikete sahip alıntı listesi
     */
    List<Quote> findByTag(String tag);
    
    /**
     * Alıntıyı siler.
     * 
     * @param id Silinecek alıntının ID'si
     */
    void delete(String id);
}
