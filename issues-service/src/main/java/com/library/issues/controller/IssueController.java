package com.library.issues.controller;

import com.library.issues.dto.*;
import com.library.issues.service.IssueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/issues")
@RequiredArgsConstructor
public class IssueController {

    private final IssueService svc;

    @PostMapping("/borrow")
    public IssueResponse borrow(Authentication auth,
                                @Valid @RequestBody IssueRequest req) {
        return svc.borrow(auth.getName(), req);
    }

    @PostMapping("/return/{bookId}")
    public IssueResponse giveBack(Authentication auth,
                                  @PathVariable Long bookId) {
        return svc.returnBook(auth.getName(), bookId);
    }

    @GetMapping("/my")
    public List<IssueResponse> myBooks(Authentication auth) {
        return svc.listMyActive(auth.getName());
    }

    @GetMapping("/admin/active")
    @PreAuthorize("hasRole('ADMIN')")
    public List<IssueResponse> allActive() {
        return svc.listAllActive();   // use the service, not repo
    }
}
