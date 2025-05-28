package com.digilibrary.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * JSON veri yönetimi için yardımcı sınıf.
 */
public class JsonDataManager {
    private final String dataDirectory;
    private final ObjectMapper objectMapper;
    
    /**
     * Yapıcı metod.
     * 
     * @param dataDirectory Veri dizini
     */
    public JsonDataManager(String dataDirectory) {
        this.dataDirectory = dataDirectory;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        
        // Veri dizinini oluştur
        File dir = new File(dataDirectory);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }
    
    /**
     * Veriyi JSON dosyasına kaydeder.
     * 
     * @param data Kaydedilecek veri listesi
     * @param fileName Dosya adı
     * @param <T> Veri tipi
     */
    public <T> void saveData(List<T> data, String fileName) {
        try {
            File file = new File(dataDirectory + "/" + fileName);
            objectMapper.writeValue(file, data);
        } catch (IOException e) {
            System.err.println("Veri kaydedilirken hata oluştu: " + e.getMessage());
        }
    }
    
    /**
     * Veriyi JSON dosyasından yükler.
     * 
     * @param fileName Dosya adı
     * @param type Veri tipi sınıfı
     * @param <T> Veri tipi
     * @return Yüklenen veri listesi
     */
    public <T> List<T> loadData(String fileName, Class<T> type) {
        File file = new File(dataDirectory + "/" + fileName);
        if (file.exists()) {
            try {
                return objectMapper.readValue(file, 
                    objectMapper.getTypeFactory().constructCollectionType(List.class, type));
            } catch (IOException e) {
                System.err.println("Veri yüklenirken hata oluştu: " + e.getMessage());
            }
        }
        return List.of();
    }
    
    /**
     * Veri yedeklemesi yapar.
     * 
     * @param backupDirectory Yedekleme dizini
     */
    public void backup(String backupDirectory) {
        File sourceDir = new File(dataDirectory);
        File targetDir = new File(backupDirectory);
        
        if (!targetDir.exists()) {
            targetDir.mkdirs();
        }
        
        File[] files = sourceDir.listFiles((dir, name) -> name.endsWith(".json"));
        if (files != null) {
            for (File file : files) {
                try {
                    File targetFile = new File(backupDirectory + "/" + file.getName());
                    java.nio.file.Files.copy(
                        file.toPath(), 
                        targetFile.toPath(), 
                        java.nio.file.StandardCopyOption.REPLACE_EXISTING
                    );
                } catch (IOException e) {
                    System.err.println("Yedekleme sırasında hata oluştu: " + e.getMessage());
                }
            }
        }
    }
    
    /**
     * Yedekten geri yükleme yapar.
     * 
     * @param backupDirectory Yedekleme dizini
     */
    public void restore(String backupDirectory) {
        File sourceDir = new File(backupDirectory);
        File targetDir = new File(dataDirectory);
        
        if (!targetDir.exists()) {
            targetDir.mkdirs();
        }
        
        File[] files = sourceDir.listFiles((dir, name) -> name.endsWith(".json"));
        if (files != null) {
            for (File file : files) {
                try {
                    File targetFile = new File(dataDirectory + "/" + file.getName());
                    java.nio.file.Files.copy(
                        file.toPath(), 
                        targetFile.toPath(), 
                        java.nio.file.StandardCopyOption.REPLACE_EXISTING
                    );
                } catch (IOException e) {
                    System.err.println("Geri yükleme sırasında hata oluştu: " + e.getMessage());
                }
            }
        }
    }
}
