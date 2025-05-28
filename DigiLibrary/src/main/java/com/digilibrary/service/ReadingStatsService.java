package com.digilibrary.service;

import com.digilibrary.model.ReadingProgress;
import com.digilibrary.repository.ReadingProgressRepository;
import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Okuma istatistiklerini hesaplayan servis sınıfı.
 */
public class ReadingStatsService {
    private final ReadingProgressRepository progressRepository;
    
    /**
     * Yapıcı metod.
     * 
     * @param progressRepository Okuma ilerlemesi repository'si
     */
    public ReadingStatsService(ReadingProgressRepository progressRepository) {
        this.progressRepository = progressRepository;
    }
    
    /**
     * Kullanıcının toplam okuduğu kitap sayısını hesaplar.
     * 
     * @param userId Kullanıcı ID'si
     * @return Toplam okunan kitap sayısı
     */
    public int getTotalBooksRead(String userId) {
        List<ReadingProgress> completedProgresses = progressRepository.findByUserId(userId).stream()
                .filter(p -> p.getStatus() == com.digilibrary.model.ReadingStatus.COMPLETED)
                .collect(Collectors.toList());
        return completedProgresses.size();
    }
    
    /**
     * Kullanıcının toplam okuduğu sayfa sayısını hesaplar.
     * 
     * @param userId Kullanıcı ID'si
     * @return Toplam okunan sayfa sayısı
     */
    public int getTotalPagesRead(String userId) {
        return progressRepository.findByUserId(userId).stream()
                .mapToInt(ReadingProgress::getCurrentPage)
                .sum();
    }
    
    /**
     * Kullanıcının belirli bir yıl içinde aylara göre okuduğu kitap sayısını hesaplar.
     * 
     * @param userId Kullanıcı ID'si
     * @param year Yıl
     * @return Aylara göre okunan kitap sayısı haritası
     */
    public Map<String, Integer> getBooksReadByMonth(String userId, int year) {
        Map<String, Integer> booksPerMonth = new HashMap<>();
        
        // Tüm aylar için başlangıç değerlerini 0 olarak ayarla
        for (Month month : Month.values()) {
            booksPerMonth.put(month.toString(), 0);
        }
        
        // Tamamlanan kitapları filtrele ve aylara göre grupla
        List<ReadingProgress> completedProgresses = progressRepository.findByUserId(userId).stream()
                .filter(p -> p.getStatus() == com.digilibrary.model.ReadingStatus.COMPLETED)
                .filter(p -> p.getEndDate() != null && p.getEndDate().getYear() == year)
                .collect(Collectors.toList());
        
        // Her ay için tamamlanan kitap sayısını hesapla
        for (ReadingProgress progress : completedProgresses) {
            String month = progress.getEndDate().getMonth().toString();
            booksPerMonth.put(month, booksPerMonth.get(month) + 1);
        }
        
        return booksPerMonth;
    }
    
    /**
     * Kullanıcının belirli bir yıl içinde aylara göre okuduğu sayfa sayısını hesaplar.
     * 
     * @param userId Kullanıcı ID'si
     * @param year Yıl
     * @return Aylara göre okunan sayfa sayısı haritası
     */
    public Map<String, Integer> getPagesReadByMonth(String userId, int year) {
        Map<String, Integer> pagesPerMonth = new HashMap<>();
        
        // Tüm aylar için başlangıç değerlerini 0 olarak ayarla
        for (Month month : Month.values()) {
            pagesPerMonth.put(month.toString(), 0);
        }
        
        // Tamamlanan kitapları filtrele ve aylara göre grupla
        List<ReadingProgress> completedProgresses = progressRepository.findByUserId(userId).stream()
                .filter(p -> p.getStatus() == com.digilibrary.model.ReadingStatus.COMPLETED)
                .filter(p -> p.getEndDate() != null && p.getEndDate().getYear() == year)
                .collect(Collectors.toList());
        
        // Her ay için okunan sayfa sayısını hesapla
        for (ReadingProgress progress : completedProgresses) {
            String month = progress.getEndDate().getMonth().toString();
            pagesPerMonth.put(month, pagesPerMonth.get(month) + progress.getCurrentPage());
        }
        
        return pagesPerMonth;
    }
    
    /**
     * Kullanıcının ortalama okuma hızını hesaplar (günlük sayfa sayısı).
     * 
     * @param userId Kullanıcı ID'si
     * @return Ortalama okuma hızı (sayfa/gün)
     */
    public double getAverageReadingSpeed(String userId) {
        List<ReadingProgress> completedProgresses = progressRepository.findByUserId(userId).stream()
                .filter(p -> p.getStatus() == com.digilibrary.model.ReadingStatus.COMPLETED)
                .filter(p -> p.getStartDate() != null && p.getEndDate() != null)
                .collect(Collectors.toList());
        
        if (completedProgresses.isEmpty()) {
            return 0.0;
        }
        
        int totalPages = 0;
        long totalDays = 0;
        
        for (ReadingProgress progress : completedProgresses) {
            totalPages += progress.getCurrentPage();
            long days = java.time.temporal.ChronoUnit.DAYS.between(progress.getStartDate(), progress.getEndDate());
            totalDays += (days > 0) ? days : 1; // En az 1 gün olarak hesapla
        }
        
        return (double) totalPages / totalDays;
    }
}
