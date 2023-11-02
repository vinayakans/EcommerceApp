package com.ecom.library.library.service.impl;

import com.ecom.library.library.repository.OrderRepository;
import com.ecom.library.library.service.AdminDashboardService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Date;

@Service
public class AdminDashboardImpl implements AdminDashboardService {
    private OrderRepository orderRepository;

    public AdminDashboardImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Double totalMonthlyIncome() {
        YearMonth currentMonth = YearMonth.now();
        LocalDate localStartDate = LocalDate.of(currentMonth.getYear(),currentMonth.getMonthValue(),1);
        LocalDate localEndDate = currentMonth.atEndOfMonth();
        Date startDate = java.sql.Date.valueOf(localStartDate);
        Date endDate = java.sql.Date.valueOf(localEndDate);
        return orderRepository.monthlyIncome(startDate,endDate);
    }

    @Override
    public Double DailyIncome() {
        LocalDate today = LocalDate.now();
        return orderRepository.DailyIncome(today);
    }
}
