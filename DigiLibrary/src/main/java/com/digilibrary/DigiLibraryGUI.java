package com.digilibrary;

import com.digilibrary.controller.LibraryController;
import com.digilibrary.model.*;
import com.digilibrary.repository.*;
import com.digilibrary.repository.impl.*;
import com.digilibrary.service.*;
import com.digilibrary.ui.LoginPanel;
import com.digilibrary.ui.MainPanel;
import com.digilibrary.ui.RegisterPanel;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

/**
 * Dijital Kütüphane ve Alıntı Takip Sistemi GUI uygulaması.
 */
public class DigiLibraryGUI extends JFrame {
    private final LibraryController controller;
    private User currentUser;
    
    private CardLayout cardLayout;
    private JPanel mainContainer;
    
    private LoginPanel loginPanel;
    private RegisterPanel registerPanel;
    private MainPanel mainPanel;
    
    /**
     * Yapıcı metod.
     */
    public DigiLibraryGUI() {
        // Uygulama başlığını ayarla
        super("DigiLibrary - Dijital Kütüphane ve Alıntı Takip Sistemi");
        
        // Veri dizinini ayarla
        String dataDirectory = "data";
        
        // Repository'leri oluştur
        BookRepository bookRepository = new JsonBookRepository(dataDirectory);
        UserRepository userRepository = new JsonUserRepository(dataDirectory);
        QuoteRepository quoteRepository = new JsonQuoteRepository(dataDirectory);
        ReadingProgressRepository progressRepository = new JsonReadingProgressRepository(dataDirectory);
        ReadingGoalRepository goalRepository = new JsonReadingGoalRepository(dataDirectory);
        
        // Servisleri oluştur
        BookService bookService = new BookService(bookRepository);
        UserService userService = new UserService(userRepository);
        QuoteService quoteService = new QuoteService(quoteRepository);
        ReadingProgressService progressService = new ReadingProgressService(progressRepository);
        ReadingStatsService statsService = new ReadingStatsService(progressRepository);
        ReadingGoalService goalService = new ReadingGoalService(goalRepository, statsService);
        
        // Kontrolcüyü oluştur
        this.controller = new LibraryController(
            bookService, userService, quoteService, 
            progressService, statsService, goalService
        );
        
        this.currentUser = null;
        
        // GUI bileşenlerini başlat
        initializeUI();
        
        // Örnek veri oluştur
        createSampleData();
    }
    
    /**
     * GUI bileşenlerini başlatır.
     */
    private void initializeUI() {
        // Pencere özelliklerini ayarla
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setMinimumSize(new Dimension(800, 600));
        setLocationRelativeTo(null); // Ekranın ortasında göster
        
        // Ana container ve card layout oluştur
        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);
        
        // Panelleri oluştur
        loginPanel = new LoginPanel(this);
        registerPanel = new RegisterPanel(this);
        mainPanel = new MainPanel(this);
        
        // Panelleri container'a ekle
        mainContainer.add(loginPanel, "login");
        mainContainer.add(registerPanel, "register");
        mainContainer.add(mainPanel, "main");
        
        // İlk panel olarak login panelini göster
        cardLayout.show(mainContainer, "login");
        
        // Ana container'ı pencereye ekle
        add(mainContainer);
        
        // Look and Feel'i Windows'a uygun olarak ayarla
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Örnek veri oluşturur.
     */
    private void createSampleData() {
        // Örnek kullanıcı oluştur
        if (controller.getUserByEmail("admin@digilibrary.com").isEmpty()) {
            User admin = new User("Admin", "User", "admin@digilibrary.com", "admin123");
            admin.setRole(UserRole.ADMIN);
            controller.registerUser(admin);
            
            User user = new User("Test", "User", "user@digilibrary.com", "user123");
            user.setRole(UserRole.USER);
            controller.registerUser(user);
        }
        
        // Örnek kitaplar oluştur
        if (controller.getAllBooks().isEmpty()) {
            Book book1 = new Book(
                "1984", 
                "George Orwell", 
                LocalDate.of(1949, 6, 8), 
                328, 
                "Distopya", 
                "978-0451524935", 
                "Totaliter bir rejimin gözetimi altındaki distopik bir dünyayı anlatan klasik roman."
            );
            controller.addBook(book1);
            
            Book book2 = new Book(
                "Suç ve Ceza", 
                "Fyodor Dostoyevski", 
                LocalDate.of(1866, 1, 1), 
                671, 
                "Roman", 
                "978-0143058144", 
                "Bir öğrencinin işlediği cinayet ve sonrasında yaşadığı psikolojik çöküşü anlatan roman."
            );
            controller.addBook(book2);
            
            Book book3 = new Book(
                "Küçük Prens", 
                "Antoine de Saint-Exupéry", 
                LocalDate.of(1943, 4, 6), 
                96, 
                "Çocuk Kitabı", 
                "978-0156012195", 
                "Bir pilotun çölde karşılaştığı küçük prensin hikayesini anlatan felsefi masal."
            );
            controller.addBook(book3);
        }
    }
    
    /**
     * Kullanıcı girişi yapar.
     */
    public void login(String email, String password) {
        if (controller.authenticateUser(email, password)) {
            currentUser = controller.getUserByEmail(email).orElse(null);
            mainPanel.updateUserInfo(currentUser);
            cardLayout.show(mainContainer, "main");
            return;
        }
        
        JOptionPane.showMessageDialog(
            this,
            "Giriş başarısız! E-posta veya şifre hatalı.",
            "Giriş Hatası",
            JOptionPane.ERROR_MESSAGE
        );
    }
    
    /**
     * Kayıt ekranına geçer.
     */
    public void showRegisterPanel() {
        cardLayout.show(mainContainer, "register");
    }
    
    /**
     * Giriş ekranına geçer.
     */
    public void showLoginPanel() {
        cardLayout.show(mainContainer, "login");
    }
    
    /**
     * Kullanıcı kaydı yapar.
     */
    public void register(String firstName, String lastName, String email, String password) {
        // E-posta formatı kontrolü
        if (!isValidEmail(email)) {
            JOptionPane.showMessageDialog(
                this,
                "Geçersiz e-posta formatı!",
                "Kayıt Hatası",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        
        // Şifre uzunluğu kontrolü
        if (password.length() < 6) {
            JOptionPane.showMessageDialog(
                this,
                "Şifre en az 6 karakter olmalıdır!",
                "Kayıt Hatası",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        
        // E-posta kullanımda mı kontrolü
        if (controller.getUserByEmail(email).isPresent()) {
            JOptionPane.showMessageDialog(
                this,
                "Bu e-posta adresi zaten kullanımda!",
                "Kayıt Hatası",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        
        User user = new User(firstName, lastName, email, password);
        user.setRole(UserRole.USER);
        controller.registerUser(user);
        
        JOptionPane.showMessageDialog(
            this,
            "Kayıt başarılı! Giriş yapabilirsiniz.",
            "Kayıt Başarılı",
            JOptionPane.INFORMATION_MESSAGE
        );
        
        cardLayout.show(mainContainer, "login");
    }
    
    /**
     * Kullanıcı çıkışı yapar.
     */
    public void logout() {
        currentUser = null;
        cardLayout.show(mainContainer, "login");
    }
    
    /**
     * E-posta formatını kontrol eder.
     */
    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
    
    /**
     * Controller'a erişim sağlar.
     */
    public LibraryController getController() {
        return controller;
    }
    
    /**
     * Mevcut kullanıcıya erişim sağlar.
     */
    public User getCurrentUser() {
        return currentUser;
    }
    
    /**
     * Ana metod.
     */
    public static void main(String[] args) {
        // Event Dispatch Thread'de çalıştır
        SwingUtilities.invokeLater(() -> {
            DigiLibraryGUI app = new DigiLibraryGUI();
            app.setVisible(true);
        });
    }
}
