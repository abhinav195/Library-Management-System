package com.library.issues.service;

import com.library.issues.client.BookClient;
import com.library.issues.client.UserClient;
import com.library.issues.dto.*;
import com.library.issues.entity.Issue;
import com.library.issues.repository.IssueRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IssueService {

    private final IssueRepository repo;
    private final UserClient userClient;
    private final BookClient bookClient;

    @Transactional
    public IssueResponse borrow(String username, IssueRequest req) {

        Long userId = userClient.getUserId(username);

        // 1) already holding this exact book?
        if (repo.existsByUserIdAndBookIdAndReturnedAtIsNull(userId, req.getBookId()))
            throw new IllegalStateException("User already has this book");

        // 2) limit: max 5 active books
        int active = repo.countByUserIdAndReturnedAtIsNull(userId);
        if (active >= 5)
            throw new IllegalStateException("User already has 5 books");

        // 3) book must be in stock
        if (!bookClient.isAvailable(req.getBookId()))
            throw new IllegalStateException("Book not available");

        int points = userClient.getPenalty(username);     // see client below
        if (points >= 5)
            throw new IllegalStateException("User has 5 penalty points");

        // 4) go ahead and lend it
        bookClient.decrement(req.getBookId());

        Issue issue = repo.save(Issue.builder()
                .userId(userId)
                .bookId(req.getBookId())
                .issuedAt(LocalDateTime.now())
                .dueAt(LocalDateTime.now().plusDays(30))
                .build());

        return map(issue);
    }

    @Transactional
    public IssueResponse returnBook(String username, Long bookId) {

        Long userId = userClient.getUserId(username);

        Issue issue = repo.findByUserIdAndBookIdAndReturnedAtIsNull(userId, bookId)
                .orElseThrow(() -> new IllegalStateException("User is not holding this book"));

        // IssueService.returnBook
        if (issue.getDueAt().isBefore(LocalDateTime.now()))
            userClient.addPenalty(username);
        issue.setReturnedAt(LocalDateTime.now());
        bookClient.increment(bookId);

        return map(issue);
    }

    private IssueResponse map(Issue i) {
        return IssueResponse.builder()
                .id(i.getId())
                .bookId(i.getBookId())
                .userId(i.getUserId())
                .issuedAt(i.getIssuedAt().toString())
                .dueAt(i.getDueAt().toString())
                .returnedAt(i.getReturnedAt() == null ? null : i.getReturnedAt().toString())
                .build();
    }

    public List<IssueResponse> listMyActive(String username) {
        Long uid = userClient.getUserId(username);
        return repo.findByUserIdAndReturnedAtIsNull(uid)
                .stream().map(this::map).toList();
    }

    public List<IssueResponse> listAllActive() {
        return repo.findByReturnedAtIsNull()
                .stream().map(this::map)
                .toList();
    }
}
