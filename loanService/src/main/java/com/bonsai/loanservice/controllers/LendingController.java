package com.bonsai.loanservice.controllers;

import com.bonsai.loanservice.services.LendingService;
import com.bonsai.sharedservice.dtos.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/")
@CrossOrigin("*")
@RequiredArgsConstructor
public class LendingController {

    private final LendingService lendingService;

    @GetMapping("/myLendings")
    public ResponseEntity<SuccessResponse> fetchAllLendings(Authentication authentication) {
        String user = (String) authentication.getPrincipal();
        return ResponseEntity.ok(
                new SuccessResponse("Lendings fetched successfully", lendingService.fetchLendings(user))
        );
    }
}
