package com.digilibrary.model;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * Kullanıcının kitap okuma ilerlemesini temsil eden sınıf.
 */
public class ReadingProgress {
    private String id;
    private String bookId;
    private String userId;
    private LocalDate startDate;
    private LocalDate endDate;
    private int currentPage;
    private ReadingStatus status;
    
    /**
     * Varsayılan yapıcı metod.
     */
    public ReadingProgress() {
        this.id = UUID.randomUUID().toString();
        this.startDate = LocalDate.now();
        this.status = ReadingStatus.NOT_STARTED;
        this.currentPage = 0;
    }
    
    /**
     * Parametreli yapıcı metod.
     * 
     * @param bookId Kitap ID'si
     * @param userId Kullanıcı ID'si
     */
    public ReadingProgress(String bookId, String userId) {
        this.id = UUID.randomUUID().toString();
        this.bookId = bookId;
        this.userId = userId;
        this.startDate = LocalDate.now();
        this.status = ReadingStatus.IN_PROGRESS;
        this.currentPage = 0;
    }
    
    // Getter ve Setter metodları
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public ReadingStatus getStatus() {
        return status;
    }

    public void setStatus(ReadingStatus status) {
        this.status = status;
    }
    
    /**
     * Okumayı tamamlar ve bitiş tarihini ayarlar.
     */
    public void completeReading() {
        this.status = ReadingStatus.COMPLETED;
        this.endDate = LocalDate.now();
    }
    
    /**
     * Okumayı bekletmeye alır.
     */
    public void pauseReading() {
        this.status = ReadingStatus.ON_HOLD;
    }
    
    /**
     * Okumaya devam eder.
     */
    public void resumeReading() {
        this.status = ReadingStatus.IN_PROGRESS;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReadingProgress that = (ReadingProgress) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ReadingProgress{" +
                "id='" + id + '\'' +
                ", bookId='" + bookId + '\'' +
                ", userId='" + userId + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", currentPage=" + currentPage +
                ", status=" + status +
                '}';
    }
}
