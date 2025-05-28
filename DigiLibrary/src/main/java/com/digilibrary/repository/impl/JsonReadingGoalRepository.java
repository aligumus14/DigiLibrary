package com.digilibrary.repository.impl;

import com.digilibrary.model.ReadingGoal;
import com.digilibrary.repository.ReadingGoalRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * ReadingGoalRepository arayüzünün JSON dosya tabanlı implementasyonu.
 */
public class JsonReadingGoalRepository implements ReadingGoalRepository {
    private final String dataFile;
    private final ObjectMapper objectMapper;
    private List<ReadingGoal> goals;

    /**
     * Yapıcı metod.
     * 
     * @param dataDirectory Veri dizini
     */
    public JsonReadingGoalRepository(String dataDirectory) {
        this.dataFile = dataDirectory + "/reading_goals.json";
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.goals = loadGoals();
    }

    /**
     * Okuma hedeflerini JSON dosyasından yükler.
     */
    private List<ReadingGoal> loadGoals() {
        File file = new File(dataFile);
        if (file.exists()) {
            try {
                return objectMapper.readValue(file, 
                    objectMapper.getTypeFactory().constructCollectionType(List.class, ReadingGoal.class));
            } catch (IOException e) {
                System.err.println("Okuma hedefleri yüklenirken hata oluştu: " + e.getMessage());
            }
        }
        return new ArrayList<>();
    }

    /**
     * Okuma hedeflerini JSON dosyasına kaydeder.
     */
    private void saveGoals() {
        try {
            File file = new File(dataFile);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            objectMapper.writeValue(file, goals);
        } catch (IOException e) {
            System.err.println("Okuma hedefleri kaydedilirken hata oluştu: " + e.getMessage());
        }
    }

    @Override
    public ReadingGoal save(ReadingGoal goal) {
        Optional<ReadingGoal> existingGoal = findById(goal.getId());
        if (existingGoal.isPresent()) {
            goals.remove(existingGoal.get());
        }
        goals.add(goal);
        saveGoals();
        return goal;
    }

    @Override
    public Optional<ReadingGoal> findById(String id) {
        return goals.stream()
                .filter(goal -> goal.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<ReadingGoal> findByUserId(String userId) {
        return goals.stream()
                .filter(goal -> goal.getUserId() != null && goal.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(String id) {
        goals.removeIf(goal -> goal.getId().equals(id));
        saveGoals();
    }
}
