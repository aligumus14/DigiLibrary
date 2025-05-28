package com.digilibrary.ui;

import com.digilibrary.DigiLibraryGUI;
import com.digilibrary.model.GoalPeriod;
import com.digilibrary.model.ReadingGoal;
import com.digilibrary.model.User;
import com.digilibrary.service.ReadingGoalService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Okuma hedefleri paneli.
 */
public class ReadingGoalsPanel extends JPanel {
    private final DigiLibraryGUI mainApp;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    private JTable goalsTable;
    private DefaultTableModel goalsTableModel;
    
    /**
     * Yapıcı metod.
     * @param mainApp Ana uygulama referansı
     */
    public ReadingGoalsPanel(DigiLibraryGUI mainApp) {
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
        
        JButton addGoalButton = new JButton("Yeni Hedef Ekle");
        addGoalButton.addActionListener(e -> showAddGoalDialog());
        
        JButton editGoalButton = new JButton("Hedefi Düzenle");
        editGoalButton.addActionListener(e -> {
            int selectedRow = goalsTable.getSelectedRow();
            if (selectedRow >= 0) {
                String goalId = (String) goalsTableModel.getValueAt(selectedRow, 0);
                mainApp.getController().getGoalById(goalId).ifPresent(this::showEditGoalDialog);
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "Lütfen düzenlenecek bir hedef seçin.",
                    "Uyarı",
                    JOptionPane.WARNING_MESSAGE
                );
            }
        });
        
        JButton deleteGoalButton = new JButton("Hedefi Sil");
        deleteGoalButton.addActionListener(e -> {
            int selectedRow = goalsTable.getSelectedRow();
            if (selectedRow >= 0) {
                String goalId = (String) goalsTableModel.getValueAt(selectedRow, 0);
                int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Bu hedefi silmek istediğinize emin misiniz?",
                    "Hedef Silme",
                    JOptionPane.YES_NO_OPTION
                );
                
                if (confirm == JOptionPane.YES_OPTION) {
                    mainApp.getController().deleteGoal(goalId);
                    refreshGoalsTable();
                }
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "Lütfen silinecek bir hedef seçin.",
                    "Uyarı",
                    JOptionPane.WARNING_MESSAGE
                );
            }
        });
        
        JButton trackProgressButton = new JButton("İlerlemeyi Görüntüle");
        trackProgressButton.addActionListener(e -> {
            int selectedRow = goalsTable.getSelectedRow();
            if (selectedRow >= 0) {
                String goalId = (String) goalsTableModel.getValueAt(selectedRow, 0);
                showGoalProgressDialog(goalId);
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "Lütfen ilerleme durumunu görmek istediğiniz bir hedef seçin.",
                    "Uyarı",
                    JOptionPane.WARNING_MESSAGE
                );
            }
        });
        
        JButton refreshButton = new JButton("Yenile");
        refreshButton.addActionListener(e -> refreshGoalsTable());
        
        topPanel.add(addGoalButton);
        topPanel.add(editGoalButton);
        topPanel.add(deleteGoalButton);
        topPanel.add(trackProgressButton);
        topPanel.add(refreshButton);
        
        add(topPanel, BorderLayout.NORTH);
        
        // Orta kısım - Hedefler tablosu
        String[] columnNames = {"ID", "Periyot", "Kitap Hedefi", "Sayfa Hedefi", "Başlangıç", "Bitiş", "Durum"};
        goalsTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        goalsTable = new JTable(goalsTableModel);
        goalsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        goalsTable.getTableHeader().setReorderingAllowed(false);
        
        JScrollPane scrollPane = new JScrollPane(goalsTable);
        add(scrollPane, BorderLayout.CENTER);
        
        // Hedefleri yükle
        refreshGoalsTable();
    }
    
    /**
     * Hedefler tablosunu yeniler.
     */
    public void refreshGoalsTable() {
        goalsTableModel.setRowCount(0);
        
        User currentUser = mainApp.getCurrentUser();
        if (currentUser != null) {
            List<ReadingGoal> goals = mainApp.getController().getGoalsByUserId(currentUser.getId());
            for (ReadingGoal goal : goals) {
                String periodText = getPeriodText(goal.getPeriod());
                String startDate = goal.getStartDate() != null ? 
                    goal.getStartDate().format(dateFormatter) : "-";
                String endDate = goal.getEndDate() != null ? 
                    goal.getEndDate().format(dateFormatter) : "-";
                String status = getGoalStatusText(goal);
                
                Object[] row = {
                    goal.getId(),
                    periodText,
                    goal.getTargetBooks(),
                    goal.getTargetPages(),
                    startDate,
                    endDate,
                    status
                };
                goalsTableModel.addRow(row);
            }
        }
    }
    
    /**
     * Periyot kodunu Türkçe metne dönüştürür.
     */
    private String getPeriodText(GoalPeriod period) {
        switch (period) {
            case WEEKLY:
                return "HAFTALIK";
            case MONTHLY:
                return "AYLIK";
            case YEARLY:
                return "YILLIK";
            default:
                return "BİLİNMİYOR";
        }
    }
    
    /**
     * Hedef durumunu Türkçe metne dönüştürür.
     */
    private String getGoalStatusText(ReadingGoal goal) {
        if (goal.isExpired()) {
            return "SONA ERDİ";
        } else if (goal.isActive()) {
            return "AKTİF";
        } else {
            return "GELECEK";
        }
    }
    
    /**
     * Hedef ekleme diyaloğunu gösterir.
     */
    private void showAddGoalDialog() {
        JDialog dialog = new JDialog((Frame)SwingUtilities.getWindowAncestor(this), "Yeni Hedef Ekle", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);
        
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        // Periyot seçimi
        JPanel periodPanel = new JPanel();
        periodPanel.setLayout(new BoxLayout(periodPanel, BoxLayout.Y_AXIS));
        periodPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel periodLabel = new JLabel("Periyot:");
        JComboBox<PeriodItem> periodComboBox = new JComboBox<>();
        periodComboBox.addItem(new PeriodItem(GoalPeriod.WEEKLY, "Haftalık"));
        periodComboBox.addItem(new PeriodItem(GoalPeriod.MONTHLY, "Aylık"));
        periodComboBox.addItem(new PeriodItem(GoalPeriod.YEARLY, "Yıllık"));
        periodComboBox.setSelectedIndex(1); // Varsayılan olarak aylık
        
        periodPanel.add(periodLabel);
        periodPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        periodPanel.add(periodComboBox);
        
        // Kitap hedefi
        JTextField booksField = new JTextField("5", 10);
        JPanel booksPanel = createLabeledField("Kitap Hedefi:", booksField);
        
        // Sayfa hedefi
        JTextField pagesField = new JTextField("500", 10);
        JPanel pagesPanel = createLabeledField("Sayfa Hedefi:", pagesField);
        
        // Başlangıç tarihi
        JTextField startDateField = new JTextField(LocalDate.now().format(dateFormatter), 10);
        JPanel startDatePanel = createLabeledField("Başlangıç Tarihi (YYYY-MM-DD):", startDateField);
        
        formPanel.add(periodPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(booksPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(pagesPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(startDatePanel);
        
        // Butonlar
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton saveButton = new JButton("Kaydet");
        saveButton.addActionListener(e -> {
            try {
                PeriodItem selectedPeriod = (PeriodItem) periodComboBox.getSelectedItem();
                String booksStr = booksField.getText().trim();
                String pagesStr = pagesField.getText().trim();
                String startDateStr = startDateField.getText().trim();
                
                // Doğrulama
                if (booksStr.isEmpty() || pagesStr.isEmpty() || startDateStr.isEmpty()) {
                    JOptionPane.showMessageDialog(
                        dialog,
                        "Lütfen tüm alanları doldurun.",
                        "Hata",
                        JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
                
                int targetBooks;
                try {
                    targetBooks = Integer.parseInt(booksStr);
                    if (targetBooks <= 0) {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(
                        dialog,
                        "Geçersiz kitap hedefi. Lütfen pozitif bir sayı girin.",
                        "Hata",
                        JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
                
                int targetPages;
                try {
                    targetPages = Integer.parseInt(pagesStr);
                    if (targetPages <= 0) {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(
                        dialog,
                        "Geçersiz sayfa hedefi. Lütfen pozitif bir sayı girin.",
                        "Hata",
                        JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
                
                LocalDate startDate;
                try {
                    startDate = LocalDate.parse(startDateStr, dateFormatter);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(
                        dialog,
                        "Geçersiz tarih formatı. Lütfen YYYY-MM-DD formatında girin.",
                        "Hata",
                        JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
                
                // Bitiş tarihini hesapla
                LocalDate endDate;
                switch (selectedPeriod.getPeriod()) {
                    case WEEKLY:
                        endDate = startDate.plusWeeks(1);
                        break;
                    case MONTHLY:
                        endDate = startDate.plusMonths(1);
                        break;
                    case YEARLY:
                        endDate = startDate.plusYears(1);
                        break;
                    default:
                        endDate = startDate.plusMonths(1);
                }
                
                // Hedef oluştur ve ekle
                ReadingGoal goal = new ReadingGoal(
                    mainApp.getCurrentUser().getId(),
                    targetBooks,
                    targetPages,
                    startDate,
                    endDate,
                    selectedPeriod.getPeriod()
                );
                
                mainApp.getController().createGoal(goal);
                
                dialog.dispose();
                refreshGoalsTable();
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                    dialog,
                    "Hedef eklenirken bir hata oluştu: " + ex.getMessage(),
                    "Hata",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        });
        
        JButton cancelButton = new JButton("İptal");
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.setVisible(true);
    }
    
    /**
     * Hedef düzenleme diyaloğunu gösterir.
     */
    private void showEditGoalDialog(ReadingGoal goal) {
        JDialog dialog = new JDialog((Frame)SwingUtilities.getWindowAncestor(this), "Hedefi Düzenle", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);
        
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        // Periyot seçimi
        JPanel periodPanel = new JPanel();
        periodPanel.setLayout(new BoxLayout(periodPanel, BoxLayout.Y_AXIS));
        periodPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel periodLabel = new JLabel("Periyot:");
        JComboBox<PeriodItem> periodComboBox = new JComboBox<>();
        periodComboBox.addItem(new PeriodItem(GoalPeriod.WEEKLY, "Haftalık"));
        periodComboBox.addItem(new PeriodItem(GoalPeriod.MONTHLY, "Aylık"));
        periodComboBox.addItem(new PeriodItem(GoalPeriod.YEARLY, "Yıllık"));
        
        // Mevcut periyodu seç
        for (int i = 0; i < periodComboBox.getItemCount(); i++) {
            PeriodItem item = periodComboBox.getItemAt(i);
            if (item.getPeriod() == goal.getPeriod()) {
                periodComboBox.setSelectedIndex(i);
                break;
            }
        }
        
        periodPanel.add(periodLabel);
        periodPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        periodPanel.add(periodComboBox);
        
        // Kitap hedefi
        JTextField booksField = new JTextField(String.valueOf(goal.getTargetBooks()), 10);
        JPanel booksPanel = createLabeledField("Kitap Hedefi:", booksField);
        
        // Sayfa hedefi
        JTextField pagesField = new JTextField(String.valueOf(goal.getTargetPages()), 10);
        JPanel pagesPanel = createLabeledField("Sayfa Hedefi:", pagesField);
        
        // Başlangıç tarihi
        JTextField startDateField = new JTextField(goal.getStartDate().format(dateFormatter), 10);
        JPanel startDatePanel = createLabeledField("Başlangıç Tarihi (YYYY-MM-DD):", startDateField);
        
        formPanel.add(periodPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(booksPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(pagesPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(startDatePanel);
        
        // Butonlar
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton saveButton = new JButton("Kaydet");
        saveButton.addActionListener(e -> {
            try {
                PeriodItem selectedPeriod = (PeriodItem) periodComboBox.getSelectedItem();
                String booksStr = booksField.getText().trim();
                String pagesStr = pagesField.getText().trim();
                String startDateStr = startDateField.getText().trim();
                
                // Doğrulama
                if (booksStr.isEmpty() || pagesStr.isEmpty() || startDateStr.isEmpty()) {
                    JOptionPane.showMessageDialog(
                        dialog,
                        "Lütfen tüm alanları doldurun.",
                        "Hata",
                        JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
                
                int targetBooks;
                try {
                    targetBooks = Integer.parseInt(booksStr);
                    if (targetBooks <= 0) {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(
                        dialog,
                        "Geçersiz kitap hedefi. Lütfen pozitif bir sayı girin.",
                        "Hata",
                        JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
                
                int targetPages;
                try {
                    targetPages = Integer.parseInt(pagesStr);
                    if (targetPages <= 0) {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(
                        dialog,
                        "Geçersiz sayfa hedefi. Lütfen pozitif bir sayı girin.",
                        "Hata",
                        JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
                
                LocalDate startDate;
                try {
                    startDate = LocalDate.parse(startDateStr, dateFormatter);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(
                        dialog,
                        "Geçersiz tarih formatı. Lütfen YYYY-MM-DD formatında girin.",
                        "Hata",
                        JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
                
                // Bitiş tarihini hesapla
                LocalDate endDate;
                switch (selectedPeriod.getPeriod()) {
                    case WEEKLY:
                        endDate = startDate.plusWeeks(1);
                        break;
                    case MONTHLY:
                        endDate = startDate.plusMonths(1);
                        break;
                    case YEARLY:
                        endDate = startDate.plusYears(1);
                        break;
                    default:
                        endDate = startDate.plusMonths(1);
                }
                
                // Hedefi güncelle
                goal.setTargetBooks(targetBooks);
                goal.setTargetPages(targetPages);
                goal.setStartDate(startDate);
                goal.setEndDate(endDate);
                goal.setPeriod(selectedPeriod.getPeriod());
                
                mainApp.getController().updateGoal(goal);
                
                dialog.dispose();
                refreshGoalsTable();
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                    dialog,
                    "Hedef güncellenirken bir hata oluştu: " + ex.getMessage(),
                    "Hata",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        });
        
        JButton cancelButton = new JButton("İptal");
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.setVisible(true);
    }
    
    /**
     * Hedef ilerleme durumunu gösteren diyaloğu gösterir.
     */
    private void showGoalProgressDialog(String goalId) {
        ReadingGoalService.GoalProgress progress = mainApp.getController().trackGoalProgress(goalId);
        
        if (progress == null) {
            JOptionPane.showMessageDialog(
                this,
                "Hedef ilerleme bilgisi alınamadı.",
                "Hata",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        
        JDialog dialog = new JDialog((Frame)SwingUtilities.getWindowAncestor(this), "Hedef İlerleme Durumu", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        // Hedef bilgileri
        JLabel titleLabel = new JLabel("Hedef İlerleme Durumu");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Kitap hedefi ilerleme
        JPanel booksPanel = new JPanel();
        booksPanel.setLayout(new BoxLayout(booksPanel, BoxLayout.Y_AXIS));
        booksPanel.setBorder(BorderFactory.createTitledBorder("Kitap Hedefi"));
        booksPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel booksTargetLabel = new JLabel("Hedef: " + progress.getTargetBooks() + " kitap");
        JLabel booksCompletedLabel = new JLabel("Tamamlanan: " + progress.getCompletedBooks() + " kitap");
        JLabel booksRemainingLabel = new JLabel("Kalan: " + (progress.getTargetBooks() - progress.getCompletedBooks()) + " kitap");
        
        int booksPercentage = (int) (progress.getCompletedBooks() * 100.0 / progress.getTargetBooks());
        JProgressBar booksProgressBar = new JProgressBar(0, 100);
        booksProgressBar.setValue(booksPercentage);
        booksProgressBar.setStringPainted(true);
        booksProgressBar.setString(booksPercentage + "%");
        
        booksPanel.add(booksTargetLabel);
        booksPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        booksPanel.add(booksCompletedLabel);
        booksPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        booksPanel.add(booksRemainingLabel);
        booksPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        booksPanel.add(booksProgressBar);
        
        // Sayfa hedefi ilerleme
        JPanel pagesPanel = new JPanel();
        pagesPanel.setLayout(new BoxLayout(pagesPanel, BoxLayout.Y_AXIS));
        pagesPanel.setBorder(BorderFactory.createTitledBorder("Sayfa Hedefi"));
        pagesPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel pagesTargetLabel = new JLabel("Hedef: " + progress.getTargetPages() + " sayfa");
        JLabel pagesCompletedLabel = new JLabel("Tamamlanan: " + progress.getCompletedPages() + " sayfa");
        JLabel pagesRemainingLabel = new JLabel("Kalan: " + (progress.getTargetPages() - progress.getCompletedPages()) + " sayfa");
        
        int pagesPercentage = (int) (progress.getCompletedPages() * 100.0 / progress.getTargetPages());
        JProgressBar pagesProgressBar = new JProgressBar(0, 100);
        pagesProgressBar.setValue(pagesPercentage);
        pagesProgressBar.setStringPainted(true);
        pagesProgressBar.setString(pagesPercentage + "%");
        
        pagesPanel.add(pagesTargetLabel);
        pagesPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        pagesPanel.add(pagesCompletedLabel);
        pagesPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        pagesPanel.add(pagesRemainingLabel);
        pagesPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        pagesPanel.add(pagesProgressBar);
        
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        contentPanel.add(booksPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        contentPanel.add(pagesPanel);
        
        // Butonlar
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton closeButton = new JButton("Kapat");
        closeButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(closeButton);
        
        dialog.add(contentPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.setVisible(true);
    }
    
    /**
     * Etiketli alan oluşturur.
     */
    private JPanel createLabeledField(String labelText, JTextField textField) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel label = new JLabel(labelText);
        textField.setMaximumSize(new Dimension(Integer.MAX_VALUE, textField.getPreferredSize().height));
        
        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(textField);
        
        return panel;
    }
    
    /**
     * ComboBox için periyot öğesi sınıfı.
     */
    private static class PeriodItem {
        private final GoalPeriod period;
        private final String displayName;
        
        public PeriodItem(GoalPeriod period, String displayName) {
            this.period = period;
            this.displayName = displayName;
        }
        
        public GoalPeriod getPeriod() {
            return period;
        }
        
        @Override
        public String toString() {
            return displayName;
        }
    }
}
