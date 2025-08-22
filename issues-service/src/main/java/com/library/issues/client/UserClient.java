package com.library.issues.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "userClient", url = "${users.base-url}", configuration = com.library.issues.config.FeignConfig.class)
public interface UserClient {

    @GetMapping("/api/users/{username}/active-issue")
    Boolean hasActiveIssue(@PathVariable String username);   // implement later in users svc

    @GetMapping("/api/users/{username}/id")
    Long getUserId(@PathVariable String username);

    @GetMapping("/api/users/{username}/penalty")
    int getPenalty(@PathVariable String username);

    @PutMapping("/api/users/{username}/penalty/increment")
    void addPenalty(@PathVariable String username);
}

