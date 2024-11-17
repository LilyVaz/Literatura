package com.alura.Literatura;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final RestTemplate restTemplate;

    @Value("${api.base-url}")
    private String apiUrl;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
        this.restTemplate = new RestTemplate();
    }

    public Book fetchBookFromApi(String title) {
        String url = apiUrl + "?title=" + title;
        try {
            ResponseEntity<JsonNode> response = restTemplate.getForEntity(url, JsonNode.class);
            JsonNode data = response.getBody().get("results").get(0);

            Book book = new Book();
            book.setTitle(data.get("title").asText());
            book.setAuthor(data.get("authors").get(0).get("name").asText());
            book.setLanguage(data.get("language").asText());
            book.setDownloads(data.get("download_count").asInt());

            return book;
        } catch (Exception e) {
            System.out.println("No se encontró el libro.");
            return null;
        }
    }

    public void saveBook(Book book) {
        if (!bookRepository.findByTitle(book.getTitle()).isPresent()) {
            bookRepository.save(book);
            System.out.println("Libro guardado exitosamente.");
        } else {
            System.out.println("El libro ya existe en la base de datos.");
        }
    }

    public List<Book> listAllBooks() {
        return bookRepository.findAll();
    }
}
