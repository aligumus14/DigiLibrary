package com.digilibrary.model;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * Kullanıcının okuma hedeflerini temsil eden sınıf.
 */
public class ReadingGoal {
    private String id;
    private String userId;
    private int targetBooks;
    private int targetPages;
    private LocalDate startDate;
    private LocalDate endDate;
    private GoalPeriod period;

    /**
     * Varsayılan yapıcı metod.
     */
    public ReadingGoal() {
        this.id = UUID.randomUUID().toString();
        this.startDate = LocalDate.now();
    }

    /**
     * Parametreli yapıcı metod (otomatik tarih hesaplayan).
     *
     * @param userId Kullanıcı ID'si
     * @param targetBooks Hedeflenen kitap sayısı
     * @param targetPages Hedeflenen sayfa sayısı
     * @param period Hedef periyodu
     */
    public ReadingGoal(String userId, int targetBooks, int targetPages, GoalPeriod period) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.targetBooks = targetBooks;
        this.targetPages = targetPages;
        this.startDate = LocalDate.now();
        this.period = period;

        // Periyoda göre bitiş tarihini ayarla
        switch (period) {
            case WEEKLY:
                this.endDate = this.startDate.plusWeeks(1);
                break;
            case MONTHLY:
                this.endDate = this.startDate.plusMonths(1);
                break;
            case YEARLY:
                this.endDate = this.startDate.plusYears(1);
                break;
            default:
                this.endDate = this.startDate.plusMonths(1); // Varsayılan olarak aylık
        }
    }

    /**
     * Parametreli yapıcı metod (tüm tarihleri dışarıdan alır).
     *
     * @param userId Kullanıcı ID'si
     * @param targetBooks Hedeflenen kitap sayısı
     * @param targetPages Hedeflenen sayfa sayısı
     * @param startDate Başlangıç tarihi
     * @param endDate Bitiş tarihi
     * @param period Hedef periyodu
     */
    public ReadingGoal(String userId, int targetBooks, int targetPages, LocalDate startDate, LocalDate endDate, GoalPeriod period) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.targetBooks = targetBooks;
        this.targetPages = targetPages;
        this.startDate = startDate;
        this.endDate = endDate;
        this.period = period;
    }

    // Getter ve Setter metodları

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getTargetBooks() {
        return targetBooks;
    }

    public void setTargetBooks(int targetBooks) {
        this.targetBooks = targetBooks;
    }

    public int getTargetPages() {
        return targetPages;
    }

    public void setTargetPages(int targetPages) {
        this.targetPages = targetPages;
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

    public GoalPeriod getPeriod() {
        return period;
    }

    public void setPeriod(GoalPeriod period) {
        this.period = period;
    }

    /**
     * Hedefin aktif olup olmadığını kontrol eder.
     *
     * @return Hedef aktifse true, değilse false
     */
    public boolean isActive() {
        LocalDate today = LocalDate.now();
        return !today.isBefore(startDate) && !today.isAfter(endDate);
    }

    /**
     * Hedefin süresinin dolup dolmadığını kontrol eder.
     *
     * @return Hedefin süresi dolduysa true, dolmadıysa false
     */
    public boolean isExpired() {
        return LocalDate.now().isAfter(endDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReadingGoal that = (ReadingGoal) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ReadingGoal{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", targetBooks=" + targetBooks +
                ", targetPages=" + targetPages +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", period=" + period +
                '}';
    }
}
