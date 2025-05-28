package com.digilibrary.ui;

import com.digilibrary.DigiLibraryGUI;
import com.digilibrary.model.Book;
import com.digilibrary.model.Quote;
import com.digilibrary.model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Alıntı işlemleri paneli.
 */
public class QuotesPanel extends JPanel {
    private final DigiLibraryGUI mainApp;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    private JTable quotesTable;
    private DefaultTableModel quotesTableModel;
    private JComboBox<BookItem> bookComboBox;
    
    /**
     * Yapıcı metod.
     * @param mainApp Ana uygulama referansı
     */
    public QuotesPanel(DigiLibraryGUI mainApp) {
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
        
        JButton addQuoteButton = new JButton("Alıntı Ekle");
        addQuoteButton.addActionListener(e -> showAddQuoteDialog());
        
        JButton editQuoteButton = new JButton("Alıntı Düzenle");
        editQuoteButton.addActionListener(e -> {
            int selectedRow = quotesTable.getSelectedRow();
            if (selectedRow >= 0) {
                String quoteId = (String) quotesTableModel.getValueAt(selectedRow, 0);
                mainApp.getController().getQuoteById(quoteId).ifPresent(this::showEditQuoteDialog);
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "Lütfen düzenlenecek bir alıntı seçin.",
                    "Uyarı",
                    JOptionPane.WARNING_MESSAGE
                );
            }
        });
        
        JButton deleteQuoteButton = new JButton("Alıntı Sil");
        deleteQuoteButton.addActionListener(e -> {
            int selectedRow = quotesTable.getSelectedRow();
            if (selectedRow >= 0) {
                String quoteId = (String) quotesTableModel.getValueAt(selectedRow, 0);
                int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Bu alıntıyı silmek istediğinize emin misiniz?",
                    "Alıntı Silme",
                    JOptionPane.YES_NO_OPTION
                );
                
                if (confirm == JOptionPane.YES_OPTION) {
                    mainApp.getController().deleteQuote(quoteId);
                    refreshQuotesTable();
                }
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "Lütfen silinecek bir alıntı seçin.",
                    "Uyarı",
                    JOptionPane.WARNING_MESSAGE
                );
            }
        });
        
        JButton refreshButton = new JButton("Yenile");
        refreshButton.addActionListener(e -> refreshQuotesTable());
        
        topPanel.add(addQuoteButton);
        topPanel.add(editQuoteButton);
        topPanel.add(deleteQuoteButton);
        topPanel.add(refreshButton);
        
        add(topPanel, BorderLayout.NORTH);
        
        // Orta kısım - Alıntı tablosu
        String[] columnNames = {"ID", "Kitap", "İçerik", "Sayfa", "Tarih", "Etiketler"};
        quotesTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        quotesTable = new JTable(quotesTableModel);
        quotesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        quotesTable.getTableHeader().setReorderingAllowed(false);
        
        JScrollPane scrollPane = new JScrollPane(quotesTable);
        add(scrollPane, BorderLayout.CENTER);
        
        // Alıntıları yükle
        refreshQuotesTable();
    }
    
    /**
     * Alıntı tablosunu yeniler.
     */
    public void refreshQuotesTable() {
        quotesTableModel.setRowCount(0);
        
        User currentUser = mainApp.getCurrentUser();
        if (currentUser != null) {
            List<Quote> quotes = mainApp.getController().getQuotesByUserId(currentUser.getId());
            for (Quote quote : quotes) {
                String bookTitle = "Bilinmeyen";
                Optional<Book> book = mainApp.getController().getBookById(quote.getBookId());
                if (book.isPresent()) {
                    bookTitle = book.get().getTitle();
                }
                
                String tagsStr = quote.getTags() != null ? 
                    String.join(", ", quote.getTags()) : "";
                
                // İçeriği kısalt
                String content = quote.getContent();
                if (content.length() > 50) {
                    content = content.substring(0, 47) + "...";
                }
                
                Object[] row = {
                    quote.getId(),
                    bookTitle,
                    content,
                    quote.getPageNumber(),
                    quote.getCreationDate().format(dateFormatter),
                    tagsStr
                };
                quotesTableModel.addRow(row);
            }
        }
    }
    
    /**
     * Alıntı ekleme diyaloğunu gösterir.
     */
    private void showAddQuoteDialog() {
        JDialog dialog = new JDialog((Frame)SwingUtilities.getWindowAncestor(this), "Alıntı Ekle", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(500, 500);
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
        
        // Sayfa numarası
        JTextField pageNumberField = new JTextField(10);
        JPanel pagePanel = createLabeledField("Sayfa Numarası:", pageNumberField);
        
        // Alıntı içeriği
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel contentLabel = new JLabel("Alıntı İçeriği:");
        JTextArea contentArea = new JTextArea(8, 30);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        JScrollPane contentScrollPane = new JScrollPane(contentArea);
        
        contentPanel.add(contentLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        contentPanel.add(contentScrollPane);
        
        // Etiketler
        JTextField tagsField = new JTextField(20);
        JPanel tagsPanel = createLabeledField("Etiketler (virgülle ayırın):", tagsField);
        
        formPanel.add(bookPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(pagePanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(contentPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(tagsPanel);
        
        // Butonlar
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton saveButton = new JButton("Kaydet");
        saveButton.addActionListener(e -> {
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
                String content = contentArea.getText().trim();
                String pageNumberStr = pageNumberField.getText().trim();
                String tagsStr = tagsField.getText().trim();
                
                // Doğrulama
                if (content.isEmpty() || pageNumberStr.isEmpty()) {
                    JOptionPane.showMessageDialog(
                        dialog,
                        "Lütfen gerekli alanları doldurun.",
                        "Hata",
                        JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
                
                int pageNumber;
                try {
                    pageNumber = Integer.parseInt(pageNumberStr);
                    if (pageNumber <= 0) {
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
                
                // Etiketleri ayır
                List<String> tags = null;
                if (!tagsStr.isEmpty()) {
                    tags = Arrays.stream(tagsStr.split(","))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .collect(Collectors.toList());
                }
                
                // Alıntı oluştur ve ekle
                Quote quote = new Quote(
                    content, 
                    pageNumber, 
                    selectedBook.getId(), 
                    mainApp.getCurrentUser().getId()
                );
                
                if (tags != null) {
                    quote.setTags(tags);
                }
                
                mainApp.getController().addQuote(quote);
                
                dialog.dispose();
                refreshQuotesTable();
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                    dialog,
                    "Alıntı eklenirken bir hata oluştu: " + ex.getMessage(),
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
     * Alıntı düzenleme diyaloğunu gösterir.
     */
    private void showEditQuoteDialog(Quote quote) {
        JDialog dialog = new JDialog((Frame)SwingUtilities.getWindowAncestor(this), "Alıntı Düzenle", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(500, 500);
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
        
        // Mevcut kitabı seç
        for (int i = 0; i < bookComboBox.getItemCount(); i++) {
            BookItem item = bookComboBox.getItemAt(i);
            if (item.getId().equals(quote.getBookId())) {
                bookComboBox.setSelectedIndex(i);
                break;
            }
        }
        
        bookPanel.add(bookLabel);
        bookPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        bookPanel.add(bookComboBox);
        
        // Sayfa numarası
        JTextField pageNumberField = new JTextField(String.valueOf(quote.getPageNumber()), 10);
        JPanel pagePanel = createLabeledField("Sayfa Numarası:", pageNumberField);
        
        // Alıntı içeriği
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel contentLabel = new JLabel("Alıntı İçeriği:");
        JTextArea contentArea = new JTextArea(quote.getContent(), 8, 30);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        JScrollPane contentScrollPane = new JScrollPane(contentArea);
        
        contentPanel.add(contentLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        contentPanel.add(contentScrollPane);
        
        // Etiketler
        String tagsStr = quote.getTags() != null ? String.join(", ", quote.getTags()) : "";
        JTextField tagsField = new JTextField(tagsStr, 20);
        JPanel tagsPanel = createLabeledField("Etiketler (virgülle ayırın):", tagsField);
        
        formPanel.add(bookPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(pagePanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(contentPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(tagsPanel);
        
        // Butonlar
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton saveButton = new JButton("Kaydet");
        saveButton.addActionListener(e -> {
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
                String content = contentArea.getText().trim();
                String pageNumberStr = pageNumberField.getText().trim();
                String newTagsStr = tagsField.getText().trim();
                
                // Doğrulama
                if (content.isEmpty() || pageNumberStr.isEmpty()) {
                    JOptionPane.showMessageDialog(
                        dialog,
                        "Lütfen gerekli alanları doldurun.",
                        "Hata",
                        JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
                
                int pageNumber;
                try {
                    pageNumber = Integer.parseInt(pageNumberStr);
                    if (pageNumber <= 0) {
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
                
                // Etiketleri ayır
                List<String> tags = null;
                if (!newTagsStr.isEmpty()) {
                    tags = Arrays.stream(newTagsStr.split(","))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .collect(Collectors.toList());
                }
                
                // Alıntıyı güncelle
                quote.setContent(content);
                quote.setPageNumber(pageNumber);
                quote.setBookId(selectedBook.getId());
                
                if (tags != null) {
                    quote.setTags(tags);
                } else {
                    quote.setTags(null);
                }
                
                mainApp.getController().updateQuote(quote);
                
                dialog.dispose();
                refreshQuotesTable();
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                    dialog,
                    "Alıntı güncellenirken bir hata oluştu: " + ex.getMessage(),
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
