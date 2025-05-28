package com.digilibrary.repository.impl;

import com.digilibrary.model.ReadingProgress;
import com.digilibrary.model.ReadingStatus;
import com.digilibrary.repository.ReadingProgressRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * ReadingProgressRepository arayüzünün JSON dosya tabanlı implementasyonu.
 */
public class JsonReadingProgressRepository implements ReadingProgressRepository {
    private final String dataFile;
    private final ObjectMapper objectMapper;
    private List<ReadingProgress> progresses;

    /**
     * Yapıcı metod.
     * 
     * @param dataDirectory Veri dizini
     */
    public JsonReadingProgressRepository(String dataDirectory) {
        this.dataFile = dataDirectory + "/reading_progresses.json";
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.progresses = loadProgresses();
    }

    /**
     * Okuma ilerlemelerini JSON dosyasından yükler.
     */
    private List<ReadingProgress> loadProgresses() {
        File file = new File(dataFile);
        if (file.exists()) {
            try {
                return objectMapper.readValue(file, 
                    objectMapper.getTypeFactory().constructCollectionType(List.class, ReadingProgress.class));
            } catch (IOException e) {
                System.err.println("Okuma ilerlemeleri yüklenirken hata oluştu: " + e.getMessage());
            }
        }
        return new ArrayList<>();
    }

    /**
     * Okuma ilerlemelerini JSON dosyasına kaydeder.
     */
    private void saveProgresses() {
        try {
            File file = new File(dataFile);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            objectMapper.writeValue(file, progresses);
        } catch (IOException e) {
            System.err.println("Okuma ilerlemeleri kaydedilirken hata oluştu: " + e.getMessage());
        }
    }

    @Override
    public ReadingProgress save(ReadingProgress progress) {
        Optional<ReadingProgress> existingProgress = findById(progress.getId());
        if (existingProgress.isPresent()) {
            progresses.remove(existingProgress.get());
        }
        progresses.add(progress);
        saveProgresses();
        return progress;
    }

    @Override
    public Optional<ReadingProgress> findById(String id) {
        return progresses.stream()
                .filter(progress -> progress.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<ReadingProgress> findByUserId(String userId) {
        return progresses.stream()
                .filter(progress -> progress.getUserId() != null && progress.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public List<ReadingProgress> findByBookId(String bookId) {
        return progresses.stream()
                .filter(progress -> progress.getBookId() != null && progress.getBookId().equals(bookId))
                .collect(Collectors.toList());
    }

    @Override
    public List<ReadingProgress> findByStatus(ReadingStatus status) {
        return progresses.stream()
                .filter(progress -> progress.getStatus() == status)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(String id) {
        progresses.removeIf(progress -> progress.getId().equals(id));
        saveProgresses();
    }
}
