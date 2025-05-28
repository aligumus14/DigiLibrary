package com.digilibrary.repository.impl;

import com.digilibrary.model.User;
import com.digilibrary.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * UserRepository arayüzünün JSON dosya tabanlı implementasyonu.
 */
public class JsonUserRepository implements UserRepository {
    private final String dataFile;
    private final ObjectMapper objectMapper;
    private List<User> users;

    /**
     * Yapıcı metod.
     * 
     * @param dataDirectory Veri dizini
     */
    public JsonUserRepository(String dataDirectory) {
        this.dataFile = dataDirectory + "/users.json";
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.users = loadUsers();
    }

    /**
     * Kullanıcıları JSON dosyasından yükler.
     */
    private List<User> loadUsers() {
        File file = new File(dataFile);
        if (file.exists()) {
            try {
                return objectMapper.readValue(file, 
                    objectMapper.getTypeFactory().constructCollectionType(List.class, User.class));
            } catch (IOException e) {
                System.err.println("Kullanıcılar yüklenirken hata oluştu: " + e.getMessage());
            }
        }
        return new ArrayList<>();
    }

    /**
     * Kullanıcıları JSON dosyasına kaydeder.
     */
    private void saveUsers() {
        try {
            File file = new File(dataFile);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            objectMapper.writeValue(file, users);
        } catch (IOException e) {
            System.err.println("Kullanıcılar kaydedilirken hata oluştu: " + e.getMessage());
        }
    }

    @Override
    public User save(User user) {
        Optional<User> existingUser = findById(user.getId());
        if (existingUser.isPresent()) {
            users.remove(existingUser.get());
        }
        users.add(user);
        saveUsers();
        return user;
    }

    @Override
    public Optional<User> findById(String id) {
        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return users.stream()
                .filter(user -> user.getEmail() != null && user.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users);
    }

    @Override
    public void delete(String id) {
        users.removeIf(user -> user.getId().equals(id));
        saveUsers();
    }
}
