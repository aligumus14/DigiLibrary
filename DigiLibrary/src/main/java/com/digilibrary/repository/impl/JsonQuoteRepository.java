package com.digilibrary.repository.impl;

import com.digilibrary.model.Quote;
import com.digilibrary.repository.QuoteRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * QuoteRepository arayüzünün JSON dosya tabanlı implementasyonu.
 */
public class JsonQuoteRepository implements QuoteRepository {
    private final String dataFile;
    private final ObjectMapper objectMapper;
    private List<Quote> quotes;

    /**
     * Yapıcı metod.
     * 
     * @param dataDirectory Veri dizini
     */
    public JsonQuoteRepository(String dataDirectory) {
        this.dataFile = dataDirectory + "/quotes.json";
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.quotes = loadQuotes();
    }

    /**
     * Alıntıları JSON dosyasından yükler.
     */
    private List<Quote> loadQuotes() {
        File file = new File(dataFile);
        if (file.exists()) {
            try {
                return objectMapper.readValue(file, 
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Quote.class));
            } catch (IOException e) {
                System.err.println("Alıntılar yüklenirken hata oluştu: " + e.getMessage());
            }
        }
        return new ArrayList<>();
    }

    /**
     * Alıntıları JSON dosyasına kaydeder.
     */
    private void saveQuotes() {
        try {
            File file = new File(dataFile);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            objectMapper.writeValue(file, quotes);
        } catch (IOException e) {
            System.err.println("Alıntılar kaydedilirken hata oluştu: " + e.getMessage());
        }
    }

    @Override
    public Quote save(Quote quote) {
        Optional<Quote> existingQuote = findById(quote.getId());
        if (existingQuote.isPresent()) {
            quotes.remove(existingQuote.get());
        }
        quotes.add(quote);
        saveQuotes();
        return quote;
    }

    @Override
    public Optional<Quote> findById(String id) {
        return quotes.stream()
                .filter(quote -> quote.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Quote> findByUserId(String userId) {
        return quotes.stream()
                .filter(quote -> quote.getUserId() != null && quote.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Quote> findByBookId(String bookId) {
        return quotes.stream()
                .filter(quote -> quote.getBookId() != null && quote.getBookId().equals(bookId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Quote> findByTag(String tag) {
        return quotes.stream()
                .filter(quote -> quote.getTags() != null && quote.getTags().contains(tag))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(String id) {
        quotes.removeIf(quote -> quote.getId().equals(id));
        saveQuotes();
    }
}
