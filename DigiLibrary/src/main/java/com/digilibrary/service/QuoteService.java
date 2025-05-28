package com.digilibrary.service;

import com.digilibrary.model.Quote;
import com.digilibrary.repository.QuoteRepository;
import java.util.List;
import java.util.Optional;

/**
 * Alıntı işlemlerini yöneten servis sınıfı.
 */
public class QuoteService {
    private final QuoteRepository quoteRepository;
    
    /**
     * Yapıcı metod.
     * 
     * @param quoteRepository Alıntı repository'si
     */
    public QuoteService(QuoteRepository quoteRepository) {
        this.quoteRepository = quoteRepository;
    }
    
    /**
     * Yeni alıntı ekler.
     * 
     * @param quote Eklenecek alıntı
     * @return Eklenen alıntı
     */
    public Quote addQuote(Quote quote) {
        return quoteRepository.save(quote);
    }
    
    /**
     * Alıntı bilgilerini günceller.
     * 
     * @param quote Güncellenecek alıntı
     * @return Güncellenen alıntı
     */
    public Quote updateQuote(Quote quote) {
        return quoteRepository.save(quote);
    }
    
    /**
     * ID'ye göre alıntı getirir.
     * 
     * @param id Alıntı ID'si
     * @return Bulunan alıntı, yoksa boş Optional
     */
    public Optional<Quote> getQuoteById(String id) {
        return quoteRepository.findById(id);
    }
    
    /**
     * Kullanıcı ID'sine göre alıntıları listeler.
     * 
     * @param userId Kullanıcı ID'si
     * @return Kullanıcıya ait alıntı listesi
     */
    public List<Quote> getQuotesByUserId(String userId) {
        return quoteRepository.findByUserId(userId);
    }
    
    /**
     * Kitap ID'sine göre alıntıları listeler.
     * 
     * @param bookId Kitap ID'si
     * @return Kitaba ait alıntı listesi
     */
    public List<Quote> getQuotesByBookId(String bookId) {
        return quoteRepository.findByBookId(bookId);
    }
    
    /**
     * Etikete göre alıntıları listeler.
     * 
     * @param tag Etiket
     * @return Etikete sahip alıntı listesi
     */
    public List<Quote> getQuotesByTag(String tag) {
        return quoteRepository.findByTag(tag);
    }
    
    /**
     * Alıntıyı siler.
     * 
     * @param id Silinecek alıntının ID'si
     */
    public void deleteQuote(String id) {
        quoteRepository.delete(id);
    }
}
