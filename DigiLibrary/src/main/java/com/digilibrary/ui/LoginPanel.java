package com.digilibrary.ui;

import com.digilibrary.DigiLibraryGUI;

import javax.swing.*;
import java.awt.*;

/**
 * Kullanıcı giriş paneli.
 */
public class LoginPanel extends JPanel {
    private final DigiLibraryGUI mainApp;
    
    private JTextField emailField;
    private JPasswordField passwordField;
    
    /**
     * Yapıcı metod.
     * @param mainApp Ana uygulama referansı
     */
    public LoginPanel(DigiLibraryGUI mainApp) {
        this.mainApp = mainApp;
        initializeUI();
    }
    
    /**
     * UI bileşenlerini başlatır.
     */
    private void initializeUI() {
        setLayout(new BorderLayout());
        
        // Logo ve başlık paneli
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // Giriş formu paneli
        JPanel formPanel = createFormPanel();
        add(formPanel, BorderLayout.CENTER);
    }
    
    /**
     * Başlık panelini oluşturur.
     */
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        
        // Logo (ileride eklenebilir)
        // JLabel logoLabel = new JLabel(new ImageIcon("path/to/logo.png"));
        // logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        // panel.add(logoLabel);
        // panel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Başlık
        JLabel titleLabel = new JLabel("DigiLibrary");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);
        
        // Alt başlık
        JLabel subtitleLabel = new JLabel("Dijital Kütüphane ve Alıntı Takip Sistemi");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(subtitleLabel);
        
        return panel;
    }
    
    /**
     * Giriş formu panelini oluşturur.
     */
    private JPanel createFormPanel() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        formPanel.setMaximumSize(new Dimension(400, 300));
        
        // Form başlığı
        JLabel formTitleLabel = new JLabel("Kullanıcı Girişi");
        formTitleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        formTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(formTitleLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // E-posta alanı
        JPanel emailPanel = new JPanel();
        emailPanel.setLayout(new BoxLayout(emailPanel, BoxLayout.Y_AXIS));
        emailPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        emailPanel.setMaximumSize(new Dimension(400, 60));
        
        JLabel emailLabel = new JLabel("E-posta:");
        emailField = new JTextField(20);
        emailField.setMaximumSize(new Dimension(400, 30));
        
        emailPanel.add(emailLabel);
        emailPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        emailPanel.add(emailField);
        
        formPanel.add(emailPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Şifre alanı
        JPanel passwordPanel = new JPanel();
        passwordPanel.setLayout(new BoxLayout(passwordPanel, BoxLayout.Y_AXIS));
        passwordPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        passwordPanel.setMaximumSize(new Dimension(400, 60));
        
        JLabel passwordLabel = new JLabel("Şifre:");
        passwordField = new JPasswordField(20);
        passwordField.setMaximumSize(new Dimension(400, 30));
        
        passwordPanel.add(passwordLabel);
        passwordPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        passwordPanel.add(passwordField);
        
        formPanel.add(passwordPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        
        // Butonlar
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JButton loginButton = new JButton("Giriş Yap");
        loginButton.addActionListener(e -> {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            mainApp.login(email, password);
        });
        
        JButton registerButton = new JButton("Kayıt Ol");
        registerButton.addActionListener(e -> mainApp.showRegisterPanel());
        
        buttonPanel.add(loginButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(registerButton);
        
        formPanel.add(buttonPanel);
        
        // Demo kullanıcı bilgileri
        formPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        JLabel demoLabel = new JLabel("Demo Kullanıcı: user@digilibrary.com / user123");
        demoLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        demoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(demoLabel);
        
        mainPanel.add(formPanel);
        return mainPanel;
    }
}
