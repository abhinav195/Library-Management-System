package com.library.books.controller;

import com.library.books.dto.*;
import com.library.books.entity.Book;
import com.library.books.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService svc;

    /* ---------- Public ---------- */
    @GetMapping                 public List<Book> list()        { return svc.list(); }
    @GetMapping("/{id}")        public Book      get(@PathVariable Long id) { return svc.get(id); }

    /* ---------- Admin ---------- */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Book create(@Valid @RequestBody BookRequest req)     { return svc.create(req); }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Book update(@PathVariable Long id,
                       @Valid @RequestBody BookRequest req)     { return svc.update(id, req); }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id)                  { svc.delete(id); }

    @GetMapping("/{id}/available")
    public Boolean available(@PathVariable Long id) {
        return svc.get(id).getCopiesAvailable() > 0;
    }

    @PutMapping("/{id}/decrement")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public void dec(@PathVariable Long id)  { svc.decrement(id); }

    @PutMapping("/{id}/increment")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public void inc(@PathVariable Long id)  { svc.increment(id); }
}
