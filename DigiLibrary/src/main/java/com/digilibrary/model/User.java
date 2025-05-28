package com.digilibrary.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Kullanıcı bilgilerini temsil eden sınıf.
 */
public class User {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private UserRole role;
    private List<ReadingProgress> readingProgresses;
    private List<Quote> quotes;
    
    /**
     * Varsayılan yapıcı metod.
     */
    public User() {
        this.id = UUID.randomUUID().toString();
        this.role = UserRole.USER;
        this.readingProgresses = new ArrayList<>();
        this.quotes = new ArrayList<>();
    }
    
    /**
     * Parametreli yapıcı metod.
     * 
     * @param firstName Kullanıcı adı
     * @param lastName Kullanıcı soyadı
     * @param email E-posta adresi
     * @param password Şifre
     */
    public User(String firstName, String lastName, String email, String password) {
        this.id = UUID.randomUUID().toString();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = UserRole.USER;
        this.readingProgresses = new ArrayList<>();
        this.quotes = new ArrayList<>();
    }
    
    // Getter ve Setter metodları
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public List<ReadingProgress> getReadingProgresses() {
        return readingProgresses;
    }

    public void setReadingProgresses(List<ReadingProgress> readingProgresses) {
        this.readingProgresses = readingProgresses;
    }

    public List<Quote> getQuotes() {
        return quotes;
    }

    public void setQuotes(List<Quote> quotes) {
        this.quotes = quotes;
    }
    
    /**
     * Kullanıcının okuma ilerlemesini ekler.
     * 
     * @param progress Eklenecek okuma ilerlemesi
     */
    public void addReadingProgress(ReadingProgress progress) {
        if (this.readingProgresses == null) {
            this.readingProgresses = new ArrayList<>();
        }
        this.readingProgresses.add(progress);
    }
    
    /**
     * Kullanıcının alıntısını ekler.
     * 
     * @param quote Eklenecek alıntı
     */
    public void addQuote(Quote quote) {
        if (this.quotes == null) {
            this.quotes = new ArrayList<>();
        }
        this.quotes.add(quote);
    }
    
    /**
     * Kullanıcının tam adını döndürür.
     * 
     * @return Kullanıcının tam adı
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                '}';
    }
}
