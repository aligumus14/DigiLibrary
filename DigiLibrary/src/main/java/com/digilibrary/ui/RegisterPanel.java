package com.digilibrary.ui;

import com.digilibrary.DigiLibraryGUI;

import javax.swing.*;
import java.awt.*;

/**
 * Kullanıcı kayıt paneli.
 */
public class RegisterPanel extends JPanel {
    private final DigiLibraryGUI mainApp;
    
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    
    /**
     * Yapıcı metod.
     * @param mainApp Ana uygulama referansı
     */
    public RegisterPanel(DigiLibraryGUI mainApp) {
        this.mainApp = mainApp;
        initializeUI();
    }
    
    /**
     * UI bileşenlerini başlatır.
     */
    private void initializeUI() {
        setLayout(new BorderLayout());
        
        // Başlık paneli
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // Kayıt formu paneli
        JPanel formPanel = createFormPanel();
        add(formPanel, BorderLayout.CENTER);
    }
    
    /**
     * Başlık panelini oluşturur.
     */
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        // Başlık
        JLabel titleLabel = new JLabel("Yeni Kullanıcı Kaydı");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);
        
        return panel;
    }
    
    /**
     * Kayıt formu panelini oluşturur.
     */
    private JPanel createFormPanel() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 20, 40));
        formPanel.setMaximumSize(new Dimension(500, 500));
        
        // Ad alanı
        JPanel firstNamePanel = createFieldPanel("Ad:", firstNameField = new JTextField(20));
        formPanel.add(firstNamePanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Soyad alanı
        JPanel lastNamePanel = createFieldPanel("Soyad:", lastNameField = new JTextField(20));
        formPanel.add(lastNamePanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // E-posta alanı
        JPanel emailPanel = createFieldPanel("E-posta:", emailField = new JTextField(20));
        formPanel.add(emailPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Şifre alanı
        JPanel passwordPanel = createFieldPanel("Şifre:", passwordField = new JPasswordField(20));
        formPanel.add(passwordPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Şifre tekrar alanı
        JPanel confirmPasswordPanel = createFieldPanel("Şifre (Tekrar):", confirmPasswordField = new JPasswordField(20));
        formPanel.add(confirmPasswordPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Butonlar
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JButton registerButton = new JButton("Kayıt Ol");
        registerButton.addActionListener(e -> {
            if (validateForm()) {
                String firstName = firstNameField.getText();
                String lastName = lastNameField.getText();
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());
                
                mainApp.register(firstName, lastName, email, password);
            }
        });
        
        JButton cancelButton = new JButton("İptal");
        cancelButton.addActionListener(e -> mainApp.showLoginPanel());
        
        buttonPanel.add(registerButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(cancelButton);
        
        formPanel.add(buttonPanel);
        
        mainPanel.add(formPanel);
        return mainPanel;
    }
    
    /**
     * Form alanı paneli oluşturur.
     */
    private JPanel createFieldPanel(String labelText, JTextField textField) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(400, 60));
        
        JLabel label = new JLabel(labelText);
        textField.setMaximumSize(new Dimension(400, 30));
        
        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(textField);
        
        return panel;
    }
    
    /**
     * Form alanlarını doğrular.
     */
    private boolean validateForm() {
        // Ad kontrolü
        if (firstNameField.getText().trim().isEmpty()) {
            showError("Ad alanı boş olamaz!");
            return false;
        }
        
        // Soyad kontrolü
        if (lastNameField.getText().trim().isEmpty()) {
            showError("Soyad alanı boş olamaz!");
            return false;
        }
        
        // E-posta kontrolü
        String email = emailField.getText().trim();
        if (email.isEmpty()) {
            showError("E-posta alanı boş olamaz!");
            return false;
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            showError("Geçersiz e-posta formatı!");
            return false;
        }
        
        // Şifre kontrolü
        String password = new String(passwordField.getPassword());
        if (password.isEmpty()) {
            showError("Şifre alanı boş olamaz!");
            return false;
        }
        if (password.length() < 6) {
            showError("Şifre en az 6 karakter olmalıdır!");
            return false;
        }
        
        // Şifre tekrar kontrolü
        String confirmPassword = new String(confirmPasswordField.getPassword());
        if (!password.equals(confirmPassword)) {
            showError("Şifreler eşleşmiyor!");
            return false;
        }
        
        return true;
    }
    
    /**
     * Hata mesajı gösterir.
     */
    private void showError(String message) {
        JOptionPane.showMessageDialog(
            this,
            message,
            "Kayıt Hatası",
            JOptionPane.ERROR_MESSAGE
        );
    }
}
