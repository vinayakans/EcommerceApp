package com.ecom.library.library.service.impl;
import com.ecom.library.library.dto.WalletHistoryDto;
import com.ecom.library.library.enums.TransactionType;
import com.ecom.library.library.models.Customer;
import com.ecom.library.library.models.Order;
import com.ecom.library.library.models.Wallet;
import com.ecom.library.library.models.WalletHistory;
import com.ecom.library.library.repository.WalletHistoryRepository;
import com.ecom.library.library.repository.WalletRepository;
import com.ecom.library.library.service.WalletService;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class WalletServiceImpl implements WalletService {
    private WalletRepository walletRepository;
    private WalletHistoryRepository walletHistoryRepository;

    public WalletServiceImpl(WalletRepository walletRepository, WalletHistoryRepository walletHistoryRepository) {
        this.walletRepository = walletRepository;
        this.walletHistoryRepository = walletHistoryRepository;
    }

    @Override
    public WalletHistory debit(Wallet wallet, double totalPrice) {

        double newBalance = wallet.getBalance()-totalPrice;
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        String formattedBalance = decimalFormat.format(newBalance);
        double formattedDoubleBalance = Double.parseDouble(formattedBalance);
        wallet.setBalance(formattedDoubleBalance);
        walletRepository.save(wallet);

        WalletHistory walletHistory = new WalletHistory();
        walletHistory.setWallet(wallet);
        walletHistory.setTransactionType(TransactionType.DEBITED);
        walletHistory.setAmount(totalPrice);
        walletHistory.setTransactionStatus("success");
        walletHistory.setTransactionDate(LocalDate.now());

        return walletHistoryRepository.save(walletHistory);
    }



    @Override
    public void saveOrderId(Order order, WalletHistory walletHistory) {
        walletHistory.setOrder(order);
        walletHistoryRepository.save(walletHistory);
    }

    @Override
    public Wallet findByCustomer(Customer customer) {
             Wallet wallet;
        if(customer.getWallet() == null)
        {
            wallet = new Wallet();
            wallet.setBalance(0.0);
            wallet.setCustomer(customer);
            walletRepository.save(wallet);
        }
        else {
            wallet = customer.getWallet();
        }
        return wallet;
    }

    @Override
    public WalletHistory save(double amount, Customer customer) {
        Wallet wallet = customer.getWallet();
        WalletHistory walletHistory = new WalletHistory();
        walletHistory.setWallet(wallet);
        walletHistory.setTransactionType(TransactionType.TOPUP);
        walletHistory.setAmount(amount);
        walletHistory.setTransactionDate(LocalDate.now());
        walletHistoryRepository.save(walletHistory);
        return walletHistory;
    }

    @Override
    public List<WalletHistoryDto> findAllById(Long id) {

        List<WalletHistory> walletHistoryList =walletHistoryRepository.findAllById(id);
        List<WalletHistoryDto> walletHistoryDtoList = transferData(walletHistoryList);

        return walletHistoryDtoList;
    }

    @Override
    public WalletHistory findById(Long id) {
        WalletHistory walletHistory = walletHistoryRepository.getReferenceById(id);
        return walletHistory;
    }

    @Override
    public void updateWallet(WalletHistory walletHistory, Customer customer, boolean status) {
        Wallet wallet = customer.getWallet();
        if (status){
            walletHistory.setTransactionStatus("success");
            walletHistoryRepository.save(walletHistory);
            double newBalance = wallet.getBalance()+walletHistory.getAmount();
            DecimalFormat decimalFormat = new DecimalFormat("#0.0");
            String formattedBalance = decimalFormat.format(newBalance);
            double formattedDoubleBalance = Double.parseDouble(formattedBalance);
            wallet.setBalance(formattedDoubleBalance);
            walletRepository.save(wallet);
        }else {
            walletHistory.setTransactionStatus("failed");
            walletHistoryRepository.save(walletHistory);
        }
    }

    @Override
    public void returnCredit(Order order, Customer customer) {
        Wallet wallet = customer.getWallet();
        wallet.setBalance(wallet.getBalance() + order.getTotalPrice());
        walletRepository.save(wallet);
        WalletHistory walletHistory = new WalletHistory();
        walletHistory.setWallet(wallet);
        walletHistory.setTransactionDate(LocalDate.now());
        walletHistory.setTransactionType(TransactionType.CREDITED);
        walletHistory.setOrder(order);
        walletHistory.setAmount(order.getTotalPrice());
        walletHistoryRepository.save(walletHistory);
    }
    private List<WalletHistoryDto> transferData(List<WalletHistory> walletHistoryList){

        List<WalletHistoryDto>walletHistoryDtoList=new ArrayList<>();

        for(WalletHistory walletHistory : walletHistoryList){
            WalletHistoryDto walletHistoryDto=new WalletHistoryDto();
            walletHistoryDto.setId(walletHistory.getId());
            walletHistoryDto.setType(walletHistory.getTransactionType());
            walletHistoryDto.setAmount(walletHistory.getAmount());
            walletHistoryDto.setWallet(walletHistory.getWallet());
            walletHistoryDto.setTransactionStatus(walletHistory.getTransactionStatus());
            walletHistoryDto.setTransactionDate(walletHistory.getTransactionDate());

            Order order = walletHistory.getOrder();
            if (order != null) {
                walletHistoryDto.setOrderId(order.getId());
            }
            walletHistoryDtoList.add(walletHistoryDto);

        }
        return walletHistoryDtoList;
    }
}
