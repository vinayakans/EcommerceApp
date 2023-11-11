package com.ecom.library.library.service;

import com.ecom.library.library.dto.WalletHistoryDto;
import com.ecom.library.library.models.Customer;
import com.ecom.library.library.models.Order;
import com.ecom.library.library.models.Wallet;
import com.ecom.library.library.models.WalletHistory;

import java.util.List;

public interface WalletService {
    WalletHistory debit(Wallet wallet,double totalPrice);
    void saveOrderId(Order order,WalletHistory walletHistory);
    Wallet findByCustomer(Customer customer);
    WalletHistory save(double amount,Customer customer);
    List<WalletHistoryDto> findAllById(Long id);
    WalletHistory findById(Long id);
    void updateWallet(WalletHistory walletHistory,Customer customer,boolean status);
}
