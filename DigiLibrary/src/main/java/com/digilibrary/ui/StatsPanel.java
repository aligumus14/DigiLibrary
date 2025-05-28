package com.digilibrary.ui;

import com.digilibrary.DigiLibraryGUI;
import com.digilibrary.model.ReadingProgress;
import com.digilibrary.model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * İstatistikler paneli.
 */
public class StatsPanel extends JPanel {
    private final DigiLibraryGUI mainApp;
    private JPanel statsContainer;
    
    /**
     * Yapıcı metod.
     * @param mainApp Ana uygulama referansı
     */
    public StatsPanel(DigiLibraryGUI mainApp) {
        this.mainApp = mainApp;
        initializeUI();
    }
    
    /**
     * UI bileşenlerini başlatır.
     */
    private void initializeUI() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 15, 10, 15));
        
        // Üst kısım - Butonlar
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JButton refreshButton = new JButton("İstatistikleri Yenile");
        refreshButton.addActionListener(e -> refreshStats());
        
        JComboBox<Integer> yearComboBox = new JComboBox<>();
        int currentYear = LocalDate.now().getYear();
        for (int year = currentYear; year >= currentYear - 5; year--) {
            yearComboBox.addItem(year);
        }
        yearComboBox.addActionListener(e -> refreshStats((Integer) yearComboBox.getSelectedItem()));
        
        JLabel yearLabel = new JLabel("Yıl: ");
        
        topPanel.add(refreshButton);
        topPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        topPanel.add(yearLabel);
        topPanel.add(yearComboBox);
        
        add(topPanel, BorderLayout.NORTH);
        
        // İstatistik içeriği
        statsContainer = new JPanel();
        statsContainer.setLayout(new BoxLayout(statsContainer, BoxLayout.Y_AXIS));
        
        JScrollPane scrollPane = new JScrollPane(statsContainer);
        add(scrollPane, BorderLayout.CENTER);
        
        // İstatistikleri yükle
        refreshStats();
    }
    
    /**
     * İstatistikleri yeniler.
     */
    public void refreshStats() {
        refreshStats(LocalDate.now().getYear());
    }
    
    /**
     * Belirli bir yıl için istatistikleri yeniler.
     */
    public void refreshStats(int year) {
        statsContainer.removeAll();
        
        User currentUser = mainApp.getCurrentUser();
        if (currentUser == null) {
            return;
        }
        
        // Genel istatistikler
        JPanel generalStatsPanel = createGeneralStatsPanel(currentUser);
        statsContainer.add(generalStatsPanel);
        statsContainer.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Aylık kitap istatistikleri
        JPanel monthlyBooksPanel = createMonthlyBooksPanel(currentUser, year);
        statsContainer.add(monthlyBooksPanel);
        statsContainer.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Aylık sayfa istatistikleri
        JPanel monthlyPagesPanel = createMonthlyPagesPanel(currentUser, year);
        statsContainer.add(monthlyPagesPanel);
        
        statsContainer.revalidate();
        statsContainer.repaint();
    }
    
    /**
     * Genel istatistikler paneli oluşturur.
     */
    private JPanel createGeneralStatsPanel(User user) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder("Genel Okuma İstatistikleri"));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Toplam okunan kitap sayısı
        int totalBooks = mainApp.getController().getTotalBooksRead(user.getId());
        JLabel booksLabel = new JLabel("Toplam Okunan Kitap: " + totalBooks);
        booksLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Toplam okunan sayfa sayısı
        int totalPages = mainApp.getController().getTotalPagesRead(user.getId());
        JLabel pagesLabel = new JLabel("Toplam Okunan Sayfa: " + totalPages);
        pagesLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Ortalama okuma hızı
        double readingSpeed = mainApp.getController().getAverageReadingSpeed(user.getId());
        String speedText = readingSpeed > 0 ? 
            String.format("%.1f sayfa/gün", readingSpeed) : 
            "Henüz yeterli veri yok";
        JLabel speedLabel = new JLabel("Ortalama Okuma Hızı: " + speedText);
        speedLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panel.add(booksLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(pagesLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(speedLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        return panel;
    }
    
    /**
     * Aylık kitap istatistikleri paneli oluşturur.
     */
    private JPanel createMonthlyBooksPanel(User user, int year) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(year + " Yılı Aylık Okunan Kitap Sayısı"));
        
        // Aylık kitap verilerini al
        Map<String, Integer> monthlyBooks = mainApp.getController().getBooksReadByMonth(user.getId(), year);
        
        // Grafik paneli
        JPanel graphPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawBarGraph(g, monthlyBooks, "Kitap");
            }
        };
        graphPanel.setPreferredSize(new Dimension(600, 300));
        
        panel.add(graphPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Aylık sayfa istatistikleri paneli oluşturur.
     */
    private JPanel createMonthlyPagesPanel(User user, int year) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(year + " Yılı Aylık Okunan Sayfa Sayısı"));
        
        // Aylık sayfa verilerini al
        Map<String, Integer> monthlyPages = mainApp.getController().getPagesReadByMonth(user.getId(), year);
        
        // Grafik paneli
        JPanel graphPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawBarGraph(g, monthlyPages, "Sayfa");
            }
        };
        graphPanel.setPreferredSize(new Dimension(600, 300));
        
        panel.add(graphPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Çubuk grafik çizer.
     */
    private void drawBarGraph(Graphics g, Map<String, Integer> data, String unit) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int width = getWidth();
        int height = getHeight();
        int padding = 50;
        int barWidth = (width - 2 * padding) / 12;
        int maxBarHeight = height - 2 * padding;
        
        // Maksimum değeri bul
        int maxValue = 0;
        for (Integer value : data.values()) {
            if (value > maxValue) {
                maxValue = value;
            }
        }
        
        // Ölçek faktörü
        double scale = maxValue > 0 ? (double) maxBarHeight / maxValue : 0;
        
        // Eksenleri çiz
        g2d.setColor(Color.BLACK);
        g2d.drawLine(padding, height - padding, width - padding, height - padding); // X ekseni
        g2d.drawLine(padding, height - padding, padding, padding); // Y ekseni
        
        // Ay isimlerini ve çubukları çiz
        Map<String, Integer> monthOrder = new HashMap<>();
        for (int i = 1; i <= 12; i++) {
            Month month = Month.of(i);
            String monthName = month.getDisplayName(TextStyle.SHORT, new Locale("tr"));
            monthOrder.put(monthName, i - 1);
            
            int x = padding + (i - 1) * barWidth;
            
            // Ay isimlerini çiz
            g2d.drawString(monthName, x, height - padding + 15);
            
            // Çubukları çiz
            Integer value = data.getOrDefault(monthName, 0);
            int barHeight = (int) (value * scale);
            
            if (barHeight > 0) {
                g2d.setColor(new Color(70, 130, 180));
                g2d.fillRect(x, height - padding - barHeight, barWidth - 4, barHeight);
                g2d.setColor(Color.BLACK);
                g2d.drawRect(x, height - padding - barHeight, barWidth - 4, barHeight);
                
                // Değeri çiz
                g2d.drawString(String.valueOf(value), x, height - padding - barHeight - 5);
            }
        }
        
        // Y ekseni etiketleri
        if (maxValue > 0) {
            int step = Math.max(1, maxValue / 5);
            for (int i = 0; i <= maxValue; i += step) {
                int y = height - padding - (int) (i * scale);
                g2d.drawLine(padding - 5, y, padding, y);
                g2d.drawString(String.valueOf(i), padding - 30, y + 5);
            }
        }
        
        // Başlık
        g2d.drawString("Aylık Okunan " + unit + " Sayısı", width / 2 - 70, 20);
    }
}
