package com.digilibrary.repository;

import com.digilibrary.model.ReadingProgress;
import com.digilibrary.model.ReadingStatus;
import java.util.List;
import java.util.Optional;

/**
 * Okuma ilerlemesi verilerine erişim için repository arayüzü.
 */
public interface ReadingProgressRepository {
    /**
     * Okuma ilerlemesini kaydeder veya günceller.
     * 
     * @param progress Kaydedilecek okuma ilerlemesi
     * @return Kaydedilen okuma ilerlemesi
     */
    ReadingProgress save(ReadingProgress progress);
    
    /**
     * ID'ye göre okuma ilerlemesi arar.
     * 
     * @param id Okuma ilerlemesi ID'si
     * @return Bulunan okuma ilerlemesi, yoksa boş Optional
     */
    Optional<ReadingProgress> findById(String id);
    
    /**
     * Kullanıcı ID'sine göre okuma ilerlemelerini arar.
     * 
     * @param userId Kullanıcı ID'si
     * @return Kullanıcıya ait okuma ilerlemeleri listesi
     */
    List<ReadingProgress> findByUserId(String userId);
    
    /**
     * Kitap ID'sine göre okuma ilerlemelerini arar.
     * 
     * @param bookId Kitap ID'si
     * @return Kitaba ait okuma ilerlemeleri listesi
     */
    List<ReadingProgress> findByBookId(String bookId);
    
    /**
     * Okuma durumuna göre okuma ilerlemelerini arar.
     * 
     * @param status Okuma durumu
     * @return Belirtilen durumdaki okuma ilerlemeleri listesi
     */
    List<ReadingProgress> findByStatus(ReadingStatus status);
    
    /**
     * Okuma ilerlemesini siler.
     * 
     * @param id Silinecek okuma ilerlemesinin ID'si
     */
    void delete(String id);
}
