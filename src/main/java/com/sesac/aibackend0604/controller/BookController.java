package com.sesac.aibackend0604.controller;

import com.sesac.aibackend0604.domain.Book;
import com.sesac.aibackend0604.dto.BookRequest;
import com.sesac.aibackend0604.dto.BookResponse;
import com.sesac.aibackend0604.error.NotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;


@RestController
@RequestMapping("/Books")
public class BookController {

    private final Map<Long, Book> storage = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(1);

    @GetMapping
    public List<BookResponse> list() { return storage.values().stream().map(BookResponse::from).toList(); }

    @GetMapping("/{id}")
    public BookResponse get(@PathVariable Long id) {
        Book book = storage.get(id);
        if (book == null) {
            throw NotFoundException.of("item", id);
        }
        return BookResponse.from(book);
    }

    @PostMapping
    public ResponseEntity<BookResponse> create(@Valid @RequestBody BookRequest req) {
        long id = sequence.getAndIncrement();
        Book saved = Book.builder().id(id).title(req.title()).author(req.author()).category(req.category()).description(req.description()).build();
        storage.put(id, saved);
        return ResponseEntity.created(URI.create("/books/" + id)).body(BookResponse.from(saved));
    }

    @PutMapping("/{id}")
    public BookResponse update(@PathVariable Long id, @Valid @RequestBody BookRequest req) {
        Book existing = storage.get(id);
        if (existing == null) {
            throw NotFoundException.of("book", id);
        }
        existing.setTitle(req.title());
        existing.setAuthor(req.author());
        existing.setCategory(req.category());
        existing.setDescription(req.description());
        return BookResponse.from(existing);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (storage.remove(id) == null) {
            throw NotFoundException.of("book", id);
        }
        return ResponseEntity.noContent().build();
    }
}
