package com.digilibrary.ui;

import com.digilibrary.DigiLibraryGUI;
import com.digilibrary.model.Book;
import com.digilibrary.model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Ana uygulama paneli.
 */
public class MainPanel extends JPanel {
    private final DigiLibraryGUI mainApp;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    private JLabel userInfoLabel;
    private JTabbedPane tabbedPane;
    
    // Kitap işlemleri bileşenleri
    private JTable booksTable;
    private DefaultTableModel booksTableModel;
    
    /**
     * Yapıcı metod.
     * @param mainApp Ana uygulama referansı
     */
    public MainPanel(DigiLibraryGUI mainApp) {
        this.mainApp = mainApp;
        initializeUI();
    }
    
    /**
     * UI bileşenlerini başlatır.
     */
    private void initializeUI() {
        setLayout(new BorderLayout());
        
        // Üst panel (kullanıcı bilgisi ve çıkış butonu)
        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);
        
        // Ana içerik paneli (sekmeli panel)
        tabbedPane = new JTabbedPane();
        
        // Kitap işlemleri sekmesi
        JPanel booksPanel = createBooksPanel();
        tabbedPane.addTab("Kitaplar", booksPanel);
        
        // Alıntı işlemleri sekmesi
        JPanel quotesPanel = createQuotesPanel();
        tabbedPane.addTab("Alıntılar", quotesPanel);
        
        // Okuma ilerlemesi sekmesi
        JPanel progressPanel = createReadingProgressPanel();
        tabbedPane.addTab("Okuma İlerlemesi", progressPanel);
        
        // İstatistikler sekmesi
        JPanel statsPanel = createStatsPanel();
        tabbedPane.addTab("İstatistikler", statsPanel);
        
        // Hedefler sekmesi
        JPanel goalsPanel = createGoalsPanel();
        tabbedPane.addTab("Okuma Hedefleri", goalsPanel);
        
        add(tabbedPane, BorderLayout.CENTER);
    }
    
    /**
     * Üst panel oluşturur.
     */
    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 15, 10, 15));
        
        // Kullanıcı bilgisi
        userInfoLabel = new JLabel("Hoş geldiniz!");
        userInfoLabel.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(userInfoLabel, BorderLayout.WEST);
        
        // Çıkış butonu
        JButton logoutButton = new JButton("Çıkış Yap");
        logoutButton.addActionListener(e -> mainApp.logout());
        panel.add(logoutButton, BorderLayout.EAST);
        
        return panel;
    }
    
    /**
     * Kitap işlemleri paneli oluşturur.
     */
    private JPanel createBooksPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 15, 10, 15));
        
        // Üst kısım - Butonlar
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JButton addBookButton = new JButton("Kitap Ekle");
        addBookButton.addActionListener(e -> showAddBookDialog());
        
        JButton editBookButton = new JButton("Kitap Düzenle");
        editBookButton.addActionListener(e -> {
            int selectedRow = booksTable.getSelectedRow();
            if (selectedRow >= 0) {
                String bookId = (String) booksTableModel.getValueAt(selectedRow, 0);
                mainApp.getController().getBookById(bookId).ifPresent(this::showEditBookDialog);
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "Lütfen düzenlenecek bir kitap seçin.",
                    "Uyarı",
                    JOptionPane.WARNING_MESSAGE
                );
            }
        });
        
        JButton deleteBookButton = new JButton("Kitap Sil");
        deleteBookButton.addActionListener(e -> {
            int selectedRow = booksTable.getSelectedRow();
            if (selectedRow >= 0) {
                String bookId = (String) booksTableModel.getValueAt(selectedRow, 0);
                int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Bu kitabı silmek istediğinize emin misiniz?",
                    "Kitap Silme",
                    JOptionPane.YES_NO_OPTION
                );
                
                if (confirm == JOptionPane.YES_OPTION) {
                    mainApp.getController().deleteBook(bookId);
                    refreshBooksTable();
                }
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "Lütfen silinecek bir kitap seçin.",
                    "Uyarı",
                    JOptionPane.WARNING_MESSAGE
                );
            }
        });
        
        JButton refreshButton = new JButton("Yenile");
        refreshButton.addActionListener(e -> refreshBooksTable());
        
        topPanel.add(addBookButton);
        topPanel.add(editBookButton);
        topPanel.add(deleteBookButton);
        topPanel.add(refreshButton);
        
        panel.add(topPanel, BorderLayout.NORTH);
        
        // Orta kısım - Kitap tablosu
        String[] columnNames = {"ID", "Başlık", "Yazar", "Tür", "Sayfa Sayısı"};
        booksTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        booksTable = new JTable(booksTableModel);
        booksTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        booksTable.getTableHeader().setReorderingAllowed(false);
        
        JScrollPane scrollPane = new JScrollPane(booksTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Kitapları yükle
        refreshBooksTable();
        
        return panel;
    }
    
    /**
     * Kitap tablosunu yeniler.
     */
    private void refreshBooksTable() {
        booksTableModel.setRowCount(0);
        
        List<Book> books = mainApp.getController().getAllBooks();
        for (Book book : books) {
            Object[] row = {
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getGenre(),
                book.getPageCount()
            };
            booksTableModel.addRow(row);
        }
    }
    
    /**
     * Kitap ekleme diyaloğunu gösterir.
     */
    private void showAddBookDialog() {
        JDialog dialog = new JDialog((Frame)SwingUtilities.getWindowAncestor(this), "Kitap Ekle", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 450);
        dialog.setLocationRelativeTo(this);
        
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        // Form alanları
        JTextField titleField = new JTextField(20);
        JTextField authorField = new JTextField(20);
        JTextField dateField = new JTextField(20);
        JTextField pageCountField = new JTextField(20);
        JTextField genreField = new JTextField(20);
        JTextField isbnField = new JTextField(20);
        JTextArea descriptionArea = new JTextArea(5, 20);
        
        formPanel.add(createLabeledField("Başlık:", titleField));
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(createLabeledField("Yazar:", authorField));
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(createLabeledField("Yayın Tarihi (YYYY-MM-DD):", dateField));
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(createLabeledField("Sayfa Sayısı:", pageCountField));
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(createLabeledField("Tür:", genreField));
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(createLabeledField("ISBN:", isbnField));
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        JPanel descPanel = new JPanel();
        descPanel.setLayout(new BoxLayout(descPanel, BoxLayout.Y_AXIS));
        descPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel descLabel = new JLabel("Açıklama:");
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descScrollPane = new JScrollPane(descriptionArea);
        
        descPanel.add(descLabel);
        descPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        descPanel.add(descScrollPane);
        
        formPanel.add(descPanel);
        
        // Butonlar
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton saveButton = new JButton("Kaydet");
        saveButton.addActionListener(e -> {
            try {
                String title = titleField.getText().trim();
                String author = authorField.getText().trim();
                String dateStr = dateField.getText().trim();
                String pageCountStr = pageCountField.getText().trim();
                String genre = genreField.getText().trim();
                String isbn = isbnField.getText().trim();
                String description = descriptionArea.getText().trim();
                
                // Doğrulama
                if (title.isEmpty() || author.isEmpty() || dateStr.isEmpty() || pageCountStr.isEmpty()) {
                    JOptionPane.showMessageDialog(
                        dialog,
                        "Lütfen gerekli alanları doldurun.",
                        "Hata",
                        JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
                
                LocalDate publicationDate;
                try {
                    publicationDate = LocalDate.parse(dateStr, dateFormatter);
                } catch (DateTimeParseException ex) {
                    JOptionPane.showMessageDialog(
                        dialog,
                        "Geçersiz tarih formatı. Lütfen YYYY-MM-DD formatında girin.",
                        "Hata",
                        JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
                
                int pageCount;
                try {
                    pageCount = Integer.parseInt(pageCountStr);
                    if (pageCount <= 0) {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(
                        dialog,
                        "Geçersiz sayfa sayısı. Lütfen pozitif bir sayı girin.",
                        "Hata",
                        JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
                
                // Kitap oluştur ve ekle
                Book book = new Book(title, author, publicationDate, pageCount, genre, isbn, description);
                mainApp.getController().addBook(book);
                
                dialog.dispose();
                refreshBooksTable();
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                    dialog,
                    "Kitap eklenirken bir hata oluştu: " + ex.getMessage(),
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
     * Kitap düzenleme diyaloğunu gösterir.
     */
    private void showEditBookDialog(Book book) {
        JDialog dialog = new JDialog((Frame)SwingUtilities.getWindowAncestor(this), "Kitap Düzenle", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 450);
        dialog.setLocationRelativeTo(this);
        
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        // Form alanları
        JTextField titleField = new JTextField(book.getTitle(), 20);
        JTextField authorField = new JTextField(book.getAuthor(), 20);
        JTextField dateField = new JTextField(book.getPublicationDate().format(dateFormatter), 20);
        JTextField pageCountField = new JTextField(String.valueOf(book.getPageCount()), 20);
        JTextField genreField = new JTextField(book.getGenre(), 20);
        JTextField isbnField = new JTextField(book.getIsbn(), 20);
        JTextArea descriptionArea = new JTextArea(book.getDescription(), 5, 20);
        
        formPanel.add(createLabeledField("Başlık:", titleField));
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(createLabeledField("Yazar:", authorField));
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(createLabeledField("Yayın Tarihi (YYYY-MM-DD):", dateField));
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(createLabeledField("Sayfa Sayısı:", pageCountField));
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(createLabeledField("Tür:", genreField));
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(createLabeledField("ISBN:", isbnField));
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        JPanel descPanel = new JPanel();
        descPanel.setLayout(new BoxLayout(descPanel, BoxLayout.Y_AXIS));
        descPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel descLabel = new JLabel("Açıklama:");
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descScrollPane = new JScrollPane(descriptionArea);
        
        descPanel.add(descLabel);
        descPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        descPanel.add(descScrollPane);
        
        formPanel.add(descPanel);
        
        // Butonlar
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton saveButton = new JButton("Kaydet");
        saveButton.addActionListener(e -> {
            try {
                String title = titleField.getText().trim();
                String author = authorField.getText().trim();
                String dateStr = dateField.getText().trim();
                String pageCountStr = pageCountField.getText().trim();
                String genre = genreField.getText().trim();
                String isbn = isbnField.getText().trim();
                String description = descriptionArea.getText().trim();
                
                // Doğrulama
                if (title.isEmpty() || author.isEmpty() || dateStr.isEmpty() || pageCountStr.isEmpty()) {
                    JOptionPane.showMessageDialog(
                        dialog,
                        "Lütfen gerekli alanları doldurun.",
                        "Hata",
                        JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
                
                LocalDate publicationDate;
                try {
                    publicationDate = LocalDate.parse(dateStr, dateFormatter);
                } catch (DateTimeParseException ex) {
                    JOptionPane.showMessageDialog(
                        dialog,
                        "Geçersiz tarih formatı. Lütfen YYYY-MM-DD formatında girin.",
                        "Hata",
                        JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
                
                int pageCount;
                try {
                    pageCount = Integer.parseInt(pageCountStr);
                    if (pageCount <= 0) {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(
                        dialog,
                        "Geçersiz sayfa sayısı. Lütfen pozitif bir sayı girin.",
                        "Hata",
                        JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
                
                // Kitabı güncelle
                book.setTitle(title);
                book.setAuthor(author);
                book.setPublicationDate(publicationDate);
                book.setPageCount(pageCount);
                book.setGenre(genre);
                book.setIsbn(isbn);
                book.setDescription(description);
                
                mainApp.getController().updateBook(book);
                
                dialog.dispose();
                refreshBooksTable();
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                    dialog,
                    "Kitap güncellenirken bir hata oluştu: " + ex.getMessage(),
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
     * Alıntı işlemleri paneli oluşturur.
     */
    private JPanel createQuotesPanel() {
        return new QuotesPanel(mainApp);
    }
    
    /**
     * Okuma ilerlemesi paneli oluşturur.
     */
    private JPanel createReadingProgressPanel() {
        return new ReadingProgressPanel(mainApp);
    }
    
    /**
     * İstatistikler paneli oluşturur.
     */
    private JPanel createStatsPanel() {
        return new StatsPanel(mainApp);
    }
    
    /**
     * Okuma hedefleri paneli oluşturur.
     */
    private JPanel createGoalsPanel() {
        return new ReadingGoalsPanel(mainApp);
    }
    
    /**
     * Kullanıcı bilgisini günceller.
     */
    public void updateUserInfo(User user) {
        if (user != null) {
            userInfoLabel.setText("Hoş geldiniz, " + user.getFirstName() + " " + user.getLastName() + "!");
        } else {
            userInfoLabel.setText("Hoş geldiniz!");
        }
    }
}
