package com.digilibrary.service;

import com.digilibrary.model.ReadingProgress;
import com.digilibrary.model.ReadingStatus;
import com.digilibrary.repository.ReadingProgressRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Okuma ilerlemesi işlemlerini yöneten servis sınıfı.
 */
public class ReadingProgressService {
    private final ReadingProgressRepository progressRepository;
    
    /**
     * Yapıcı metod.
     * 
     * @param progressRepository Okuma ilerlemesi repository'si
     */
    public ReadingProgressService(ReadingProgressRepository progressRepository) {
        this.progressRepository = progressRepository;
    }
    
    /**
     * Kitap okumaya başlar ve yeni bir okuma ilerlemesi kaydı oluşturur.
     * 
     * @param userId Kullanıcı ID'si
     * @param bookId Kitap ID'si
     * @return Oluşturulan okuma ilerlemesi
     */
    public ReadingProgress startReading(String userId, String bookId) {
        ReadingProgress progress = new ReadingProgress(bookId, userId);
        progress.setStatus(ReadingStatus.IN_PROGRESS);
        progress.setStartDate(LocalDate.now());
        progress.setCurrentPage(0);
        return progressRepository.save(progress);
    }
    
    /**
     * Okuma ilerlemesini günceller.
     * 
     * @param progress Güncellenecek okuma ilerlemesi
     * @return Güncellenen okuma ilerlemesi
     */
    public ReadingProgress updateProgress(ReadingProgress progress) {
        return progressRepository.save(progress);
    }
    
    /**
     * Okuma ilerlemesini tamamlanmış olarak işaretler.
     * 
     * @param progressId Okuma ilerlemesi ID'si
     * @return Güncellenen okuma ilerlemesi
     */
    public ReadingProgress completeReading(String progressId) {
        Optional<ReadingProgress> progressOpt = progressRepository.findById(progressId);
        if (progressOpt.isPresent()) {
            ReadingProgress progress = progressOpt.get();
            progress.setStatus(ReadingStatus.COMPLETED);
            progress.setEndDate(LocalDate.now());
            return progressRepository.save(progress);
        }
        throw new IllegalArgumentException("Okuma ilerlemesi bulunamadı: " + progressId);
    }
    
    /**
     * ID'ye göre okuma ilerlemesi getirir.
     * 
     * @param id Okuma ilerlemesi ID'si
     * @return Bulunan okuma ilerlemesi, yoksa boş Optional
     */
    public Optional<ReadingProgress> getProgressById(String id) {
        return progressRepository.findById(id);
    }
    
    /**
     * Kullanıcı ID'sine göre okuma ilerlemelerini listeler.
     * 
     * @param userId Kullanıcı ID'si
     * @return Kullanıcıya ait okuma ilerlemeleri listesi
     */
    public List<ReadingProgress> getProgressByUserId(String userId) {
        return progressRepository.findByUserId(userId);
    }
    
    /**
     * Kitap ID'sine göre okuma ilerlemelerini listeler.
     * 
     * @param bookId Kitap ID'si
     * @return Kitaba ait okuma ilerlemeleri listesi
     */
    public List<ReadingProgress> getProgressByBookId(String bookId) {
        return progressRepository.findByBookId(bookId);
    }
    
    /**
     * Okuma durumuna göre okuma ilerlemelerini listeler.
     * 
     * @param status Okuma durumu
     * @return Belirtilen durumdaki okuma ilerlemeleri listesi
     */
    public List<ReadingProgress> getProgressByStatus(ReadingStatus status) {
        return progressRepository.findByStatus(status);
    }
    
    /**
     * Okuma ilerlemesini bekletmeye alır.
     * 
     * @param progressId Okuma ilerlemesi ID'si
     * @return Güncellenen okuma ilerlemesi
     */
    public ReadingProgress pauseReading(String progressId) {
        Optional<ReadingProgress> progressOpt = progressRepository.findById(progressId);
        if (progressOpt.isPresent()) {
            ReadingProgress progress = progressOpt.get();
            progress.setStatus(ReadingStatus.ON_HOLD);
            return progressRepository.save(progress);
        }
        throw new IllegalArgumentException("Okuma ilerlemesi bulunamadı: " + progressId);
    }
    
    /**
     * Bekletilen okuma ilerlemesini devam ettirir.
     * 
     * @param progressId Okuma ilerlemesi ID'si
     * @return Güncellenen okuma ilerlemesi
     */
    public ReadingProgress resumeReading(String progressId) {
        Optional<ReadingProgress> progressOpt = progressRepository.findById(progressId);
        if (progressOpt.isPresent()) {
            ReadingProgress progress = progressOpt.get();
            progress.setStatus(ReadingStatus.IN_PROGRESS);
            return progressRepository.save(progress);
        }
        throw new IllegalArgumentException("Okuma ilerlemesi bulunamadı: " + progressId);
    }
}
