package com.ecom.library.library.repository;

import com.ecom.library.library.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    List<Order> findAll();
    @Query("select o from Order o where o.id=:id")
    Order getById(@Param("id")Long id);
    @Query("select o from Order o where o.customer.id = :id")
    List<Order> customerOrderList(@Param("id")Long id);
    @Query("select count(*) from Order o ")
    int orderCount();
    @Query("select count(*) from Order o where o.orderStatus='pending'")
    int pendingCount();
    @Query("select sum(od.totalPrice) from Order od where month(od.orderDate) = :month and od.paymentStatus ='paid'")
    int monthlyIncome(@Param("month") int month);
    @Query("select sum(o.totalPrice) from Order o where o.orderDate = :date and o.paymentStatus = 'paid'")
    int dailyEarnings(Date date);

}
