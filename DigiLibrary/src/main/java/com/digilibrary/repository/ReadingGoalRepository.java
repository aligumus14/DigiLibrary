package com.digilibrary.repository;

import com.digilibrary.model.ReadingGoal;
import java.util.List;
import java.util.Optional;

/**
 * Okuma hedefi verilerine erişim için repository arayüzü.
 */
public interface ReadingGoalRepository {
    /**
     * Okuma hedefini kaydeder veya günceller.
     * 
     * @param goal Kaydedilecek okuma hedefi
     * @return Kaydedilen okuma hedefi
     */
    ReadingGoal save(ReadingGoal goal);
    
    /**
     * ID'ye göre okuma hedefi arar.
     * 
     * @param id Okuma hedefi ID'si
     * @return Bulunan okuma hedefi, yoksa boş Optional
     */
    Optional<ReadingGoal> findById(String id);
    
    /**
     * Kullanıcı ID'sine göre okuma hedeflerini arar.
     * 
     * @param userId Kullanıcı ID'si
     * @return Kullanıcıya ait okuma hedefleri listesi
     */
    List<ReadingGoal> findByUserId(String userId);
    
    /**
     * Okuma hedefini siler.
     * 
     * @param id Silinecek okuma hedefinin ID'si
     */
    void delete(String id);
}
