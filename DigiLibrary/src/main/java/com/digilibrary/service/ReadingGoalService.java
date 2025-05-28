package com.digilibrary.service;

import com.digilibrary.model.ReadingGoal;
import com.digilibrary.model.ReadingProgress;
import com.digilibrary.repository.ReadingGoalRepository;
import java.util.List;
import java.util.Optional;

/**
 * Okuma hedefi işlemlerini yöneten servis sınıfı.
 */
public class ReadingGoalService {
    private final ReadingGoalRepository goalRepository;
    private final ReadingStatsService statsService;
    
    /**
     * Yapıcı metod.
     * 
     * @param goalRepository Okuma hedefi repository'si
     * @param statsService Okuma istatistikleri servisi
     */
    public ReadingGoalService(ReadingGoalRepository goalRepository, ReadingStatsService statsService) {
        this.goalRepository = goalRepository;
        this.statsService = statsService;
    }
    
    /**
     * Yeni okuma hedefi oluşturur.
     * 
     * @param goal Oluşturulacak okuma hedefi
     * @return Oluşturulan okuma hedefi
     */
    public ReadingGoal createGoal(ReadingGoal goal) {
        return goalRepository.save(goal);
    }
    
    /**
     * Okuma hedefini günceller.
     * 
     * @param goal Güncellenecek okuma hedefi
     * @return Güncellenen okuma hedefi
     */
    public ReadingGoal updateGoal(ReadingGoal goal) {
        return goalRepository.save(goal);
    }
    
    /**
     * ID'ye göre okuma hedefi getirir.
     * 
     * @param id Okuma hedefi ID'si
     * @return Bulunan okuma hedefi, yoksa boş Optional
     */
    public Optional<ReadingGoal> getGoalById(String id) {
        return goalRepository.findById(id);
    }
    
    /**
     * Kullanıcı ID'sine göre okuma hedeflerini listeler.
     * 
     * @param userId Kullanıcı ID'si
     * @return Kullanıcıya ait okuma hedefleri listesi
     */
    public List<ReadingGoal> getGoalsByUserId(String userId) {
        return goalRepository.findByUserId(userId);
    }
    
    /**
     * Okuma hedefinin ilerleme durumunu takip eder.
     * 
     * @param goalId Okuma hedefi ID'si
     * @return Hedef ilerleme durumu
     */
    public GoalProgress trackGoalProgress(String goalId) {
        Optional<ReadingGoal> goalOpt = goalRepository.findById(goalId);
        if (goalOpt.isPresent()) {
            ReadingGoal goal = goalOpt.get();
            String userId = goal.getUserId();
            
            int completedBooks = statsService.getTotalBooksRead(userId);
            int completedPages = statsService.getTotalPagesRead(userId);
            
            double bookCompletionPercentage = (goal.getTargetBooks() > 0) 
                ? (double) completedBooks / goal.getTargetBooks() * 100 
                : 0;
            
            double pageCompletionPercentage = (goal.getTargetPages() > 0) 
                ? (double) completedPages / goal.getTargetPages() * 100 
                : 0;
            
            return new GoalProgress(
                goal.getTargetBooks(),
                completedBooks,
                goal.getTargetPages(),
                completedPages,
                bookCompletionPercentage,
                pageCompletionPercentage
            );
        }
        throw new IllegalArgumentException("Okuma hedefi bulunamadı: " + goalId);
    }
    
    /**
     * Okuma hedefini siler.
     * 
     * @param id Silinecek okuma hedefinin ID'si
     */
    public void deleteGoal(String id) {
        goalRepository.delete(id);
    }
    
    /**
     * Hedef ilerleme durumunu temsil eden iç sınıf.
     */
    public static class GoalProgress {
        private final int targetBooks;
        private final int completedBooks;
        private final int targetPages;
        private final int completedPages;
        private final double bookCompletionPercentage;
        private final double pageCompletionPercentage;
        
        /**
         * Yapıcı metod.
         */
        public GoalProgress(int targetBooks, int completedBooks, int targetPages, int completedPages,
                           double bookCompletionPercentage, double pageCompletionPercentage) {
            this.targetBooks = targetBooks;
            this.completedBooks = completedBooks;
            this.targetPages = targetPages;
            this.completedPages = completedPages;
            this.bookCompletionPercentage = bookCompletionPercentage;
            this.pageCompletionPercentage = pageCompletionPercentage;
        }
        
        // Getter metodları
        
        public int getTargetBooks() {
            return targetBooks;
        }
        
        public int getCompletedBooks() {
            return completedBooks;
        }
        
        public int getTargetPages() {
            return targetPages;
        }
        
        public int getCompletedPages() {
            return completedPages;
        }
        
        public double getBookCompletionPercentage() {
            return bookCompletionPercentage;
        }
        
        public double getPageCompletionPercentage() {
            return pageCompletionPercentage;
        }
    }
}
