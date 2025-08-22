package com.library.issues.repository;

import com.library.issues.entity.Issue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IssueRepository extends JpaRepository<Issue, Long> {

    /** number of books the user is still holding */
    int countByUserIdAndReturnedAtIsNull(Long userId);

    /** does the user already hold THIS book? */
    boolean existsByUserIdAndBookIdAndReturnedAtIsNull(Long userId, Long bookId);
    Optional<Issue> findByUserIdAndBookIdAndReturnedAtIsNull(Long userId, Long bookId);
    List<Issue> findByUserIdAndReturnedAtIsNull(Long userId);

    List<Issue> findByReturnedAtIsNull();
}
