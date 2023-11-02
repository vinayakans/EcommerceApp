package com.ecom.library.library.repository;

import com.ecom.library.library.models.OrderDetails;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface OrderDetailsRepository extends JpaRepository<OrderDetails,Long> {
    @Query(value ="SELECT * FROM order_detail where order_id =:id",nativeQuery = true)
    List<OrderDetails> findAllByOrderId(Long id);
    @Query("SELECT p.id, p.name, p.category.name, p.costPrice, SUM(od.quantity), SUM(od.totalPrice) " +
            "FROM OrderDetails od " +
            "INNER JOIN od.product p " +
            "INNER JOIN od.order o " +
            "WHERE o.orderDate >= :startDate " +
            "GROUP BY p.id, p.name, p.category, p.costPrice" +
            " ORDER BY p.id")
    List<Object[]> SalesReoprt(@Param("startDate")Date startDate);

}
