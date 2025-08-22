package com.library.books.service;

import com.library.books.dto.*;
import com.library.books.entity.Book;
import com.library.books.repository.BookRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository repo;

    public List<Book> list()              { return repo.findAll(); }
    public Book      get(Long id)         { return repo.findById(id).orElseThrow(); }

    @Transactional
    public Book create(BookRequest req) {
        Book b = Book.builder()
                .title(req.getTitle())
                .author(req.getAuthor())
                .isbn(req.getIsbn())
                .totalCopies(req.getTotalCopies())
                .copiesAvailable(req.getTotalCopies())
                .publishedDate(req.getPublishedDate())
                .category(req.getCategory())
                .build();
        return repo.save(b);
    }

    @Transactional
    public Book update(Long id, BookRequest req) {
        Book b = get(id);
        b.setTitle(req.getTitle());
        b.setAuthor(req.getAuthor());
        b.setIsbn(req.getIsbn());
        b.setTotalCopies(req.getTotalCopies());
        b.setPublishedDate(req.getPublishedDate());
        b.setCategory(req.getCategory());
        // copiesAvailable left unchanged here
        return b;
    }

    public void delete(Long id) { repo.deleteById(id); }

    @Transactional
    public void decrement(Long id) {
        Book b = get(id);
        if (b.getCopiesAvailable() == 0) throw new IllegalStateException("none left");
        b.setCopiesAvailable(b.getCopiesAvailable() - 1);
    }
    @Transactional
    public void increment(Long id) {
        Book b = get(id);
        b.setCopiesAvailable(b.getCopiesAvailable() + 1);
    }
}
