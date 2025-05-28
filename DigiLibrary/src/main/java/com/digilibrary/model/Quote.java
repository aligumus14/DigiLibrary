package com.digilibrary.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Kitaplardan alınan alıntıları temsil eden sınıf.
 */
public class Quote {
    private String id;
    private String content;
    private int pageNumber;
    private LocalDate creationDate;
    private String bookId;
    private String userId;
    private List<String> tags;

    /**
     * Varsayılan yapıcı metod.
     */
    public Quote() {
        this.id = UUID.randomUUID().toString();
        this.creationDate = LocalDate.now();
        this.tags = new ArrayList<>();
    }

    /**
     * 4 parametreli yapıcı metod.
     *
     * @param content Alıntı içeriği
     * @param pageNumber Alıntının alındığı sayfa numarası
     * @param bookId Kitabın ID'si
     * @param userId Kullanıcının ID'si
     */
    public Quote(String content, int pageNumber, String bookId, String userId) {
        this.id = UUID.randomUUID().toString();
        this.content = content;
        this.pageNumber = pageNumber;
        this.creationDate = LocalDate.now();
        this.bookId = bookId;
        this.userId = userId;
        this.tags = new ArrayList<>();
    }

    /**
     * 6 parametreli yapıcı metod.
     *
     * @param content Alıntı içeriği
     * @param pageNumber Sayfa numarası
     * @param creationDate Oluşturulma tarihi
     * @param bookId Kitabın ID'si
     * @param userId Kullanıcının ID'si
     * @param tags Etiketler listesi
     */
    public Quote(String content, int pageNumber, LocalDate creationDate, String bookId, String userId, List<String> tags) {
        this.id = UUID.randomUUID().toString();
        this.content = content;
        this.pageNumber = pageNumber;
        this.creationDate = creationDate;
        this.bookId = bookId;
        this.userId = userId;
        this.tags = (tags != null) ? tags : new ArrayList<>();
    }
    // Getter ve Setter metodları
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
    
    /**
     * Alıntıya etiket ekler.
     * 
     * @param tag Eklenecek etiket
     */
    public void addTag(String tag) {
        if (this.tags == null) {
            this.tags = new ArrayList<>();
        }
        this.tags.add(tag);
    }
    
    /**
     * Alıntıdan etiket kaldırır.
     * 
     * @param tag Kaldırılacak etiket
     * @return Etiketin başarıyla kaldırılıp kaldırılmadığı
     */
    public boolean removeTag(String tag) {
        if (this.tags != null) {
            return this.tags.remove(tag);
        }
        return false;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quote quote = (Quote) o;
        return Objects.equals(id, quote.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Quote{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                ", pageNumber=" + pageNumber +
                ", creationDate=" + creationDate +
                ", bookId='" + bookId + '\'' +
                ", userId='" + userId + '\'' +
                ", tags=" + tags +
                '}';
    }
}
