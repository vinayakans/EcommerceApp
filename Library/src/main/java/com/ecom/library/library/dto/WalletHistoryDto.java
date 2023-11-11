package com.ecom.library.library.dto;

import com.ecom.library.library.enums.TransactionType;
import com.ecom.library.library.models.Wallet;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.time.LocalDate;

@Data
public class WalletHistoryDto {
    private Long id;

    @NotBlank
    @NotEmpty
    private double amount;

    private TransactionType type;

    @NotBlank
    @NotEmpty
    private String transactionStatus;

    private Wallet wallet;

    private LocalDate transactionDate;

    private Long orderId;
}
