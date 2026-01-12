package org.webapp.walletservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.webapp.walletservice.dto.WalletDto;
import org.webapp.walletservice.model.Wallet;
import org.webapp.walletservice.service.WalletService;

@RestController
@RequestMapping("/wallet")
public class WalletController {

    @Autowired
    WalletService walletService;

    // ✅ SECURED: Check if requested userId matches the token
    @GetMapping("/{userId}")
    public WalletDto getDetails(@PathVariable String userId, @RequestHeader("X-User-Id") String authenticatedId) {
        if (!userId.equals(authenticatedId)) {
            throw new RuntimeException("Unauthorized access: You can only view your own wallet.");
        }
        return walletService.toDto(userId);
    }

    // ✅ SECURED: Force the wallet to be created for the logged-in user
    @PostMapping("/create")
    public WalletDto create(@RequestBody Wallet wallet, @RequestHeader("X-User-Id") String authenticatedId) {
        wallet.setUserId(authenticatedId); // Overwrite any ID sent in body
        return walletService.createWallet(wallet);
    }

    // ✅ SECURED: Check if requested userId matches the token
    @GetMapping("/balance/{userId}")
    public WalletDto getBalance(@PathVariable String userId, @RequestHeader("X-User-Id") String authenticatedId) {
        if (!userId.equals(authenticatedId)) {
            throw new RuntimeException("Unauthorized access");
        }
        return walletService.getBalanceDto(userId);
    }

    // NOTE: block, unblock, debit, and credit are often called by other services (like Transaction/Fraud) 
    // or Admins. If you add @RequestHeader to them, you must update the Feign Clients to pass the header.
    // For now, we leave them accessible for inter-service communication.

    @PutMapping("/block/{userId}")
    public WalletDto block(@PathVariable String userId) {
        return walletService.updateStatus(userId, "BLOCKED");
    }

    @PutMapping("/unblock/{userId}")
    public WalletDto unblock(@PathVariable String userId) {
        return walletService.updateStatus(userId, "ACTIVATED");
    }

    @PutMapping("/debit")
    public WalletDto debit(@RequestBody WalletDto req) {
        return walletService.debit(req.getUserId(), req.getAmount());
    }

    @PutMapping("/credit")
    public WalletDto credit(@RequestBody WalletDto req) {
        return walletService.credit(req.getUserId(), req.getAmount());
    }
}