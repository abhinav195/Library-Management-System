package com.library.issues.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "bookClient", url = "${books.base-url}", configuration = com.library.issues.config.FeignConfig.class)
public interface BookClient {

    @GetMapping("/api/books/{id}/available")
    Boolean isAvailable(@PathVariable Long id);

    @PutMapping("/api/books/{id}/decrement")  // copiesAvailable--
    void decrement(@PathVariable Long id);

    @PutMapping("/api/books/{id}/increment")  // copiesAvailable++
    void increment(@PathVariable Long id);
}