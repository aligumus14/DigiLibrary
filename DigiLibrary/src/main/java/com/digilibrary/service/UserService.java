package com.digilibrary.service;

import com.digilibrary.model.User;
import com.digilibrary.repository.UserRepository;
import java.util.List;
import java.util.Optional;

/**
 * Kullanıcı işlemlerini yöneten servis sınıfı.
 */
public class UserService {
    private final UserRepository userRepository;
    
    /**
     * Yapıcı metod.
     * 
     * @param userRepository Kullanıcı repository'si
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    /**
     * Yeni kullanıcı kaydeder.
     * 
     * @param user Kaydedilecek kullanıcı
     * @return Kaydedilen kullanıcı
     */
    public User registerUser(User user) {
        return userRepository.save(user);
    }
    
    /**
     * Kullanıcı bilgilerini günceller.
     * 
     * @param user Güncellenecek kullanıcı
     * @return Güncellenen kullanıcı
     */
    public User updateUser(User user) {
        return userRepository.save(user);
    }
    
    /**
     * ID'ye göre kullanıcı getirir.
     * 
     * @param id Kullanıcı ID'si
     * @return Bulunan kullanıcı, yoksa boş Optional
     */
    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }
    
    /**
     * E-posta adresine göre kullanıcı getirir.
     * 
     * @param email Kullanıcı e-posta adresi
     * @return Bulunan kullanıcı, yoksa boş Optional
     */
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    /**
     * Tüm kullanıcıları listeler.
     * 
     * @return Kullanıcı listesi
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    /**
     * Kullanıcı kimlik doğrulaması yapar.
     * 
     * @param email Kullanıcı e-posta adresi
     * @param password Kullanıcı şifresi
     * @return Doğrulama başarılıysa true, değilse false
     */
    public boolean authenticateUser(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            return user.getPassword().equals(password);
        }
        return false;
    }
    
    /**
     * Kullanıcıyı siler.
     * 
     * @param id Silinecek kullanıcının ID'si
     */
    public void deleteUser(String id) {
        userRepository.delete(id);
    }
}
