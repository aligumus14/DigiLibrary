package com.digilibrary.ui;

import com.digilibrary.DigiLibraryGUI;
import com.digilibrary.model.Book;
import com.digilibrary.model.ReadingProgress;
import com.digilibrary.model.ReadingStatus;
import com.digilibrary.model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 * Okuma ilerlemesi paneli.
 */
public class ReadingProgressPanel extends JPanel {
    private final DigiLibraryGUI mainApp;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    private JTable progressTable;
    private DefaultTableModel progressTableModel;
    private JComboBox<BookItem> bookComboBox;
    
    /**
     * Yapıcı metod.
     * @param mainApp Ana uygulama referansı
     */
    public ReadingProgressPanel(DigiLibraryGUI mainApp) {
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
        
        JButton startReadingButton = new JButton("Okumaya Başla");
        startReadingButton.addActionListener(e -> showStartReadingDialog());
        
        JButton updateProgressButton = new JButton("İlerlemeyi Güncelle");
        updateProgressButton.addActionListener(e -> {
            int selectedRow = progressTable.getSelectedRow();
            if (selectedRow >= 0) {
                String progressId = (String) progressTableModel.getValueAt(selectedRow, 0);
                mainApp.getController().getProgressById(progressId).ifPresent(this::showUpdateProgressDialog);
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "Lütfen güncellenecek bir okuma ilerlemesi seçin.",
                    "Uyarı",
                    JOptionPane.WARNING_MESSAGE
                );
            }
        });
        
        JButton completeButton = new JButton("Okumayı Tamamla");
        completeButton.addActionListener(e -> {
            int selectedRow = progressTable.getSelectedRow();
            if (selectedRow >= 0) {
                String progressId = (String) progressTableModel.getValueAt(selectedRow, 0);
                int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Bu kitabı okumayı tamamladınız mı?",
                    "Okuma Tamamlama",
                    JOptionPane.YES_NO_OPTION
                );
                
                if (confirm == JOptionPane.YES_OPTION) {
                    ReadingProgress progress = mainApp.getController().completeReading(progressId);
                    refreshProgressTable();
                    JOptionPane.showMessageDialog(
                        this,
                        "Tebrikler! Kitabı okumayı tamamladınız.",
                        "Okuma Tamamlandı",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                }
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "Lütfen tamamlanacak bir okuma ilerlemesi seçin.",
                    "Uyarı",
                    JOptionPane.WARNING_MESSAGE
                );
            }
        });
        
        JButton pauseButton = new JButton("Okumayı Duraklat");
        pauseButton.addActionListener(e -> {
            int selectedRow = progressTable.getSelectedRow();
            if (selectedRow >= 0) {
                String progressId = (String) progressTableModel.getValueAt(selectedRow, 0);
                String status = (String) progressTableModel.getValueAt(selectedRow, 5);
                
                if (status.equals("DEVAM EDİYOR")) {
                    mainApp.getController().pauseReading(progressId);
                    refreshProgressTable();
                } else if (status.equals("DURAKLATILDI")) {
                    mainApp.getController().resumeReading(progressId);
                    refreshProgressTable();
                } else {
                    JOptionPane.showMessageDialog(
                        this,
                        "Bu işlem sadece devam eden veya duraklatılmış okumalar için geçerlidir.",
                        "Uyarı",
                        JOptionPane.WARNING_MESSAGE
                    );
                }
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "Lütfen duraklatılacak veya devam edilecek bir okuma ilerlemesi seçin.",
                    "Uyarı",
                    JOptionPane.WARNING_MESSAGE
                );
            }
        });
        
        JButton refreshButton = new JButton("Yenile");
        refreshButton.addActionListener(e -> refreshProgressTable());
        
        topPanel.add(startReadingButton);
        topPanel.add(updateProgressButton);
        topPanel.add(completeButton);
        topPanel.add(pauseButton);
        topPanel.add(refreshButton);
        
        add(topPanel, BorderLayout.NORTH);
        
        // Orta kısım - İlerleme tablosu
        String[] columnNames = {"ID", "Kitap", "Başlangıç", "Bitiş", "Sayfa", "Durum"};
        progressTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        progressTable = new JTable(progressTableModel);
        progressTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        progressTable.getTableHeader().setReorderingAllowed(false);
        
        JScrollPane scrollPane = new JScrollPane(progressTable);
        add(scrollPane, BorderLayout.CENTER);
        
        // İlerleme verilerini yükle
        refreshProgressTable();
    }
    
    /**
     * İlerleme tablosunu yeniler.
     */
    public void refreshProgressTable() {
        progressTableModel.setRowCount(0);
        
        User currentUser = mainApp.getCurrentUser();
        if (currentUser != null) {
            List<ReadingProgress> progressList = mainApp.getController().getProgressByUserId(currentUser.getId());
            for (ReadingProgress progress : progressList) {
                String bookTitle = "Bilinmeyen";
                Optional<Book> book = mainApp.getController().getBookById(progress.getBookId());
                if (book.isPresent()) {
                    bookTitle = book.get().getTitle();
                }
                
                String startDate = progress.getStartDate() != null ? 
                    progress.getStartDate().format(dateFormatter) : "-";
                
                String endDate = progress.getEndDate() != null ? 
                    progress.getEndDate().format(dateFormatter) : "-";
                
                String status = getStatusText(progress.getStatus());
                
                Object[] row = {
                    progress.getId(),
                    bookTitle,
                    startDate,
                    endDate,
                    progress.getCurrentPage(),
                    status
                };
                progressTableModel.addRow(row);
            }
        }
    }
    
    /**
     * Durum kodunu Türkçe metne dönüştürür.
     */
    private String getStatusText(ReadingStatus status) {
        switch (status) {
            case NOT_STARTED:
                return "BAŞLANMADI";
            case IN_PROGRESS:
                return "DEVAM EDİYOR";
            case ON_HOLD:
                return "DURAKLATILDI";
            case COMPLETED:
                return "TAMAMLANDI";
            default:
                return "BİLİNMİYOR";
        }
    }
    
    /**
     * Okumaya başlama diyaloğunu gösterir.
     */
    private void showStartReadingDialog() {
        JDialog dialog = new JDialog((Frame)SwingUtilities.getWindowAncestor(this), "Okumaya Başla", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(this);
        
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        // Kitap seçimi
        JPanel bookPanel = new JPanel();
        bookPanel.setLayout(new BoxLayout(bookPanel, BoxLayout.Y_AXIS));
        bookPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel bookLabel = new JLabel("Kitap:");
        bookComboBox = new JComboBox<>();
        loadBooksIntoComboBox();
        
        bookPanel.add(bookLabel);
        bookPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        bookPanel.add(bookComboBox);
        
        formPanel.add(bookPanel);
        
        // Butonlar
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton startButton = new JButton("Başla");
        startButton.addActionListener(e -> {
            try {
                if (bookComboBox.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(
                        dialog,
                        "Lütfen bir kitap seçin.",
                        "Hata",
                        JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
                
                BookItem selectedBook = (BookItem) bookComboBox.getSelectedItem();
                
                // Aynı kitap için zaten devam eden bir ilerleme var mı kontrol et
                User currentUser = mainApp.getCurrentUser();
                List<ReadingProgress> existingProgress = mainApp.getController().getProgressByUserId(currentUser.getId());
                
                for (ReadingProgress progress : existingProgress) {
                    if (progress.getBookId().equals(selectedBook.getId()) && 
                        (progress.getStatus() == ReadingStatus.IN_PROGRESS || 
                         progress.getStatus() == ReadingStatus.ON_HOLD)) {
                        
                        JOptionPane.showMessageDialog(
                            dialog,
                            "Bu kitap için zaten devam eden bir okuma kaydınız var.",
                            "Uyarı",
                            JOptionPane.WARNING_MESSAGE
                        );
                        return;
                    }
                }
                
                // Yeni okuma ilerlemesi oluştur
                ReadingProgress progress = mainApp.getController().startReading(
                    currentUser.getId(), 
                    selectedBook.getId()
                );
                
                dialog.dispose();
                refreshProgressTable();
                
                JOptionPane.showMessageDialog(
                    this,
                    "Okumaya başladınız! İyi okumalar.",
                    "Okuma Başlatıldı",
                    JOptionPane.INFORMATION_MESSAGE
                );
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                    dialog,
                    "Okuma başlatılırken bir hata oluştu: " + ex.getMessage(),
                    "Hata",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        });
        
        JButton cancelButton = new JButton("İptal");
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(startButton);
        buttonPanel.add(cancelButton);
        
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.setVisible(true);
    }
    
    /**
     * İlerleme güncelleme diyaloğunu gösterir.
     */
    private void showUpdateProgressDialog(ReadingProgress progress) {
        JDialog dialog = new JDialog((Frame)SwingUtilities.getWindowAncestor(this), "İlerlemeyi Güncelle", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(this);
        
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        // Kitap bilgisi
        String bookTitle = "Bilinmeyen";
        Optional<Book> book = mainApp.getController().getBookById(progress.getBookId());
        if (book.isPresent()) {
            bookTitle = book.get().getTitle();
        }
        
        JLabel bookLabel = new JLabel("Kitap: " + bookTitle);
        bookLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(bookLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Sayfa numarası
        JPanel pagePanel = new JPanel();
        pagePanel.setLayout(new BoxLayout(pagePanel, BoxLayout.Y_AXIS));
        pagePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel pageLabel = new JLabel("Mevcut Sayfa:");
        JTextField pageField = new JTextField(String.valueOf(progress.getCurrentPage()), 10);
        pageField.setMaximumSize(new Dimension(Integer.MAX_VALUE, pageField.getPreferredSize().height));
        
        pagePanel.add(pageLabel);
        pagePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        pagePanel.add(pageField);
        
        formPanel.add(pagePanel);
        
        // Butonlar
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton updateButton = new JButton("Güncelle");
        updateButton.addActionListener(e -> {
            try {
                String pageStr = pageField.getText().trim();
                
                // Doğrulama
                if (pageStr.isEmpty()) {
                    JOptionPane.showMessageDialog(
                        dialog,
                        "Lütfen mevcut sayfa numarasını girin.",
                        "Hata",
                        JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
                
                int pageNumber;
                try {
                    pageNumber = Integer.parseInt(pageStr);
                    if (pageNumber < 0) {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(
                        dialog,
                        "Geçersiz sayfa numarası. Lütfen pozitif bir sayı girin.",
                        "Hata",
                        JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
                
                // Kitabın toplam sayfa sayısını kontrol et
                int totalPages = 0;
                if (book.isPresent()) {
                    totalPages = book.get().getPageCount();
                    if (pageNumber > totalPages) {
                        JOptionPane.showMessageDialog(
                            dialog,
                            "Girdiğiniz sayfa numarası kitabın toplam sayfa sayısından (" + totalPages + ") fazla.",
                            "Hata",
                            JOptionPane.ERROR_MESSAGE
                        );
                        return;
                    }
                }
                
                // İlerlemeyi güncelle
                progress.setCurrentPage(pageNumber);
                
                // Eğer son sayfaya geldiyse, tamamlanmış olarak işaretle
                if (totalPages > 0 && pageNumber == totalPages) {
                    int confirm = JOptionPane.showConfirmDialog(
                        dialog,
                        "Son sayfaya ulaştınız. Kitabı tamamlanmış olarak işaretlemek ister misiniz?",
                        "Kitap Tamamlandı",
                        JOptionPane.YES_NO_OPTION
                    );
                    
                    if (confirm == JOptionPane.YES_OPTION) {
                        progress.completeReading();
                    }
                }
                
                mainApp.getController().updateProgress(progress);
                
                dialog.dispose();
                refreshProgressTable();
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                    dialog,
                    "İlerleme güncellenirken bir hata oluştu: " + ex.getMessage(),
                    "Hata",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        });
        
        JButton cancelButton = new JButton("İptal");
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(updateButton);
        buttonPanel.add(cancelButton);
        
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.setVisible(true);
    }
    
    /**
     * Kitapları ComboBox'a yükler.
     */
    private void loadBooksIntoComboBox() {
        bookComboBox.removeAllItems();
        
        List<Book> books = mainApp.getController().getAllBooks();
        for (Book book : books) {
            bookComboBox.addItem(new BookItem(book.getId(), book.getTitle()));
        }
    }
    
    /**
     * ComboBox için kitap öğesi sınıfı.
     */
    private static class BookItem {
        private final String id;
        private final String title;
        
        public BookItem(String id, String title) {
            this.id = id;
            this.title = title;
        }
        
        public String getId() {
            return id;
        }
        
        @Override
        public String toString() {
            return title;
        }
    }
}
