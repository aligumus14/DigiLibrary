package com.digilibrary.controller;

import com.digilibrary.model.*;
import com.digilibrary.service.*;
import java.util.List;
import java.util.Optional;

/**
 * Dijital Kütüphane sisteminin ana kontrolcü sınıfı.
 * Tüm kullanıcı etkileşimlerini ve iş mantığını yönetir.
 */
public class LibraryController {
    private final BookService bookService;
    private final UserService userService;
    private final QuoteService quoteService;
    private final ReadingProgressService progressService;
    private final ReadingStatsService statsService;
    private final ReadingGoalService goalService;
    
    /**
     * Yapıcı metod.
     */
    public LibraryController(BookService bookService, UserService userService, 
                            QuoteService quoteService, ReadingProgressService progressService,
                            ReadingStatsService statsService, ReadingGoalService goalService) {
        this.bookService = bookService;
        this.userService = userService;
        this.quoteService = quoteService;
        this.progressService = progressService;
        this.statsService = statsService;
        this.goalService = goalService;
    }
    
    // Kitap yönetimi metodları
    
    /**
     * Yeni kitap ekler.
     */
    public Book addBook(Book book) {
        return bookService.addBook(book);
    }
    
    /**
     * Kitap bilgilerini günceller.
     */
    public Book updateBook(Book book) {
        return bookService.updateBook(book);
    }
    
    /**
     * ID'ye göre kitap getirir.
     */
    public Optional<Book> getBookById(String id) {
        return bookService.getBookById(id);
    }
    
    /**
     * Tüm kitapları listeler.
     */
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }
    
    /**
     * Yazara göre kitapları listeler.
     */
    public List<Book> getBooksByAuthor(String author) {
        return bookService.getBooksByAuthor(author);
    }
    
    /**
     * Türe göre kitapları listeler.
     */
    public List<Book> getBooksByGenre(String genre) {
        return bookService.getBooksByGenre(genre);
    }
    
    /**
     * Kitabı siler.
     */
    public void deleteBook(String id) {
        bookService.deleteBook(id);
    }
    
    // Kullanıcı yönetimi metodları
    
    /**
     * Yeni kullanıcı kaydeder.
     */
    public User registerUser(User user) {
        return userService.registerUser(user);
    }
    
    /**
     * Kullanıcı bilgilerini günceller.
     */
    public User updateUser(User user) {
        return userService.updateUser(user);
    }
    
    /**
     * ID'ye göre kullanıcı getirir.
     */
    public Optional<User> getUserById(String id) {
        return userService.getUserById(id);
    }
    
    /**
     * E-posta adresine göre kullanıcı getirir.
     */
    public Optional<User> getUserByEmail(String email) {
        return userService.getUserByEmail(email);
    }
    
    /**
     * Tüm kullanıcıları listeler.
     */
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
    
    /**
     * Kullanıcı kimlik doğrulaması yapar.
     */
    public boolean authenticateUser(String email, String password) {
        return userService.authenticateUser(email, password);
    }
    
    /**
     * Kullanıcıyı siler.
     */
    public void deleteUser(String id) {
        userService.deleteUser(id);
    }
    
    // Alıntı yönetimi metodları
    
    /**
     * Yeni alıntı ekler.
     */
    public Quote addQuote(Quote quote) {
        return quoteService.addQuote(quote);
    }
    
    /**
     * Alıntı bilgilerini günceller.
     */
    public Quote updateQuote(Quote quote) {
        return quoteService.updateQuote(quote);
    }
    
    /**
     * ID'ye göre alıntı getirir.
     */
    public Optional<Quote> getQuoteById(String id) {
        return quoteService.getQuoteById(id);
    }
    
    /**
     * Kullanıcı ID'sine göre alıntıları listeler.
     */
    public List<Quote> getQuotesByUserId(String userId) {
        return quoteService.getQuotesByUserId(userId);
    }
    
    /**
     * Kitap ID'sine göre alıntıları listeler.
     */
    public List<Quote> getQuotesByBookId(String bookId) {
        return quoteService.getQuotesByBookId(bookId);
    }
    
    /**
     * Etikete göre alıntıları listeler.
     */
    public List<Quote> getQuotesByTag(String tag) {
        return quoteService.getQuotesByTag(tag);
    }
    
    /**
     * Alıntıyı siler.
     */
    public void deleteQuote(String id) {
        quoteService.deleteQuote(id);
    }
    
    // Okuma ilerleme yönetimi metodları
    
    /**
     * Kitap okumaya başlar.
     */
    public ReadingProgress startReading(String userId, String bookId) {
        return progressService.startReading(userId, bookId);
    }
    
    /**
     * Okuma ilerlemesini günceller.
     */
    public ReadingProgress updateProgress(ReadingProgress progress) {
        return progressService.updateProgress(progress);
    }
    
    /**
     * Okuma ilerlemesini tamamlanmış olarak işaretler.
     */
    public ReadingProgress completeReading(String progressId) {
        return progressService.completeReading(progressId);
    }
    
    /**
     * ID'ye göre okuma ilerlemesi getirir.
     */
    public Optional<ReadingProgress> getProgressById(String id) {
        return progressService.getProgressById(id);
    }
    
    /**
     * Kullanıcı ID'sine göre okuma ilerlemelerini listeler.
     */
    public List<ReadingProgress> getProgressByUserId(String userId) {
        return progressService.getProgressByUserId(userId);
    }
    
    /**
     * Kitap ID'sine göre okuma ilerlemelerini listeler.
     */
    public List<ReadingProgress> getProgressByBookId(String bookId) {
        return progressService.getProgressByBookId(bookId);
    }
    
    /**
     * Okuma durumuna göre okuma ilerlemelerini listeler.
     */
    public List<ReadingProgress> getProgressByStatus(ReadingStatus status) {
        return progressService.getProgressByStatus(status);
    }
    
    /**
     * Okuma ilerlemesini bekletmeye alır.
     */
    public ReadingProgress pauseReading(String progressId) {
        return progressService.pauseReading(progressId);
    }
    
    /**
     * Bekletilen okuma ilerlemesini devam ettirir.
     */
    public ReadingProgress resumeReading(String progressId) {
        return progressService.resumeReading(progressId);
    }
    
    // Okuma istatistikleri metodları
    
    /**
     * Kullanıcının toplam okuduğu kitap sayısını hesaplar.
     */
    public int getTotalBooksRead(String userId) {
        return statsService.getTotalBooksRead(userId);
    }
    
    /**
     * Kullanıcının toplam okuduğu sayfa sayısını hesaplar.
     */
    public int getTotalPagesRead(String userId) {
        return statsService.getTotalPagesRead(userId);
    }
    
    /**
     * Kullanıcının belirli bir yıl içinde aylara göre okuduğu kitap sayısını hesaplar.
     */
    public java.util.Map<String, Integer> getBooksReadByMonth(String userId, int year) {
        return statsService.getBooksReadByMonth(userId, year);
    }
    
    /**
     * Kullanıcının belirli bir yıl içinde aylara göre okuduğu sayfa sayısını hesaplar.
     */
    public java.util.Map<String, Integer> getPagesReadByMonth(String userId, int year) {
        return statsService.getPagesReadByMonth(userId, year);
    }
    
    /**
     * Kullanıcının ortalama okuma hızını hesaplar.
     */
    public double getAverageReadingSpeed(String userId) {
        return statsService.getAverageReadingSpeed(userId);
    }
    
    // Okuma hedefleri yönetimi metodları
    
    /**
     * Yeni okuma hedefi oluşturur.
     */
    public ReadingGoal createGoal(ReadingGoal goal) {
        return goalService.createGoal(goal);
    }
    
    /**
     * Okuma hedefini günceller.
     */
    public ReadingGoal updateGoal(ReadingGoal goal) {
        return goalService.updateGoal(goal);
    }
    
    /**
     * ID'ye göre okuma hedefi getirir.
     */
    public Optional<ReadingGoal> getGoalById(String id) {
        return goalService.getGoalById(id);
    }
    
    /**
     * Kullanıcı ID'sine göre okuma hedeflerini listeler.
     */
    public List<ReadingGoal> getGoalsByUserId(String userId) {
        return goalService.getGoalsByUserId(userId);
    }
    
    /**
     * Okuma hedefinin ilerleme durumunu takip eder.
     */
    public ReadingGoalService.GoalProgress trackGoalProgress(String goalId) {
        return goalService.trackGoalProgress(goalId);
    }
    
    /**
     * Okuma hedefini siler.
     */
    public void deleteGoal(String id) {
        goalService.deleteGoal(id);
    }
}
