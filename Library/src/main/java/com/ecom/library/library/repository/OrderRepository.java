package com.ecom.library.library.repository;

import com.ecom.library.library.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.Date;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    List<Order> findAll();
    @Query("select o from Order o where o.id=:id")
    Order getById(@Param("id")Long id);
    @Query("select o from Order o where o.customer.id = :id")
    List<Order> customerOrderList(@Param("id")Long id);
    @Query("select count(*) from Order o")
    int orderCount();
    @Query("select count(*) from Order o where o.orderStatus='pending'")
    int pendingCount();
//    @Query("select sum(o.totalPrice) from Order o where o.orderStatus = 'DELIVERED' and o.delivaryDate >= :date")
//    Double currentDayTotalIncome(Date date);
//    @Query(value = "SELECT COALESCE(SUM(o.totalPrice),0.0) from Order o where o.orderDate between :startDate and :endDate and o.orderStatus = 'DELIVERED'")
//    Double getTotalAmountInRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    @Query("select coalesce(sum(o.totalPrice),0.0) from Order o where o.orderDate between :startDate and :endDate and o.orderStatus = 'DELIVERED'")
    Double monthlyIncome(Date startDate,Date endDate);
    @Query("select coalesce(sum(o.totalPrice),0.0) from Order o where o.orderDate = :date and o.orderStatus = 'DELIVERED'")
    Double DailyIncome(Date date);

}
