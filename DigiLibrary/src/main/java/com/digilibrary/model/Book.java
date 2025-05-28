package com.digilibrary.model;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * Kitap bilgilerini temsil eden sınıf.
 */
public class Book {
    private String id;
    private String title;
    private String author;
    private LocalDate publicationDate;
    private int pageCount;
    private String genre;
    private String isbn;
    private String description;
    
    /**
     * Varsayılan yapıcı metod.
     */
    public Book() {
        this.id = UUID.randomUUID().toString();
    }
    
    /**
     * Parametreli yapıcı metod.
     * 
     * @param title Kitap başlığı
     * @param author Kitap yazarı
     * @param publicationDate Yayın tarihi
     * @param pageCount Sayfa sayısı
     * @param genre Kitap türü
     * @param isbn ISBN numarası
     * @param description Kitap açıklaması
     */
    public Book(String title, String author, LocalDate publicationDate, int pageCount, 
                String genre, String isbn, String description) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.author = author;
        this.publicationDate = publicationDate;
        this.pageCount = pageCount;
        this.genre = genre;
        this.isbn = isbn;
        this.description = description;
    }
    
    // Getter ve Setter metodları
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(id, book.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Book{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", publicationDate=" + publicationDate +
                ", pageCount=" + pageCount +
                ", genre='" + genre + '\'' +
                ", isbn='" + isbn + '\'' +
                '}';
    }
}
