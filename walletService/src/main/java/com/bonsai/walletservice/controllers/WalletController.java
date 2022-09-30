package com.bonsai.walletservice.controllers;

import com.bonsai.sharedservice.dtos.response.SuccessResponse;
import com.bonsai.walletservice.dtos.LoadWalletDto;
import com.bonsai.walletservice.dtos.LoadWalletRequest;
import com.bonsai.walletservice.services.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-08-06
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/wallet")
@CrossOrigin("*")
public class WalletController {

    private final WalletService walletService;

    @PostMapping("/loadBalance")
    public ResponseEntity<SuccessResponse> loadAmount(@Valid @RequestBody LoadWalletRequest loadWalletRequest,
                                                      Authentication authentication) {
        String user = (String) authentication.getPrincipal();
        LoadWalletDto loadWalletDto = walletService.loadWallet(BigDecimal.valueOf(loadWalletRequest.amount()), user,
                "Rs. " + loadWalletRequest.amount() + " credited into wallet");
        return ResponseEntity.ok(new SuccessResponse("Balance load successful",
                loadWalletDto.amount()
                ));
    }

    @GetMapping("/getBalance")
    public ResponseEntity<SuccessResponse> getWalletBalance(Authentication authentication) {
        String user = (String) authentication.getPrincipal();
        return ResponseEntity.ok(new SuccessResponse("Balance fetched successfully",
                walletService.fetchBalanceFromWallet(user)));
    }

    @GetMapping("/getAllTransactions")
    public ResponseEntity<SuccessResponse> getAllTransactions(Authentication authentication) {
        String user = (String) authentication.getPrincipal();
        return ResponseEntity.ok(new SuccessResponse("Wallet Transaction List fetched successfully",
                walletService.findAllTransactionsByUserEmail(user)));
    }
}
