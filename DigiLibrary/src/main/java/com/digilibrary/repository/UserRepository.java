package com.digilibrary.repository;

import com.digilibrary.model.User;
import java.util.List;
import java.util.Optional;

/**
 * Kullanıcı verilerine erişim için repository arayüzü.
 */
public interface UserRepository {
    /**
     * Kullanıcıyı kaydeder veya günceller.
     * 
     * @param user Kaydedilecek kullanıcı
     * @return Kaydedilen kullanıcı
     */
    User save(User user);
    
    /**
     * ID'ye göre kullanıcı arar.
     * 
     * @param id Kullanıcı ID'si
     * @return Bulunan kullanıcı, yoksa boş Optional
     */
    Optional<User> findById(String id);
    
    /**
     * E-posta adresine göre kullanıcı arar.
     * 
     * @param email Kullanıcı e-posta adresi
     * @return Bulunan kullanıcı, yoksa boş Optional
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Tüm kullanıcıları listeler.
     * 
     * @return Kullanıcı listesi
     */
    List<User> findAll();
    
    /**
     * Kullanıcıyı siler.
     * 
     * @param id Silinecek kullanıcının ID'si
     */
    void delete(String id);
}
