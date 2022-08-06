package com.bonsai.loansuggestionservice.wallet.controllers;

import com.bonsai.loansuggestionservice.wallet.dtos.WalletLoadRequest;
import com.bonsai.loansuggestionservice.wallet.services.WalletService;
import com.bonsai.loansuggestionservice.wallet.services.WalletTransactionService;
import com.bonsai.sharedservice.dtos.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
/**
 * @author Narendra
 * @version 1.0
 * @since 2022-08-06
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/wallet")
public class WalletController {

    private final WalletTransactionService walletTransactionService;
    private final WalletService walletService;

    @PostMapping("/makeTransaction")
    public ResponseEntity<SuccessResponse> loadAmount(@Valid @RequestBody WalletLoadRequest walletLoadRequest,
                                                      Authentication authentication) {
        String user = (String) authentication.getPrincipal();
        return ResponseEntity.ok(new SuccessResponse("Transaction done successFully",
                walletTransactionService.createTransaction(walletLoadRequest, user)));
    }
    @GetMapping("/getBalance")
    public ResponseEntity<SuccessResponse> getWalletBalance(Authentication authentication) {
        String user = (String) authentication.getPrincipal();
        return ResponseEntity.ok(new SuccessResponse("Balance fetched successfully",
                walletService.fetchBalanceFromWallet(user)));
    }
}
