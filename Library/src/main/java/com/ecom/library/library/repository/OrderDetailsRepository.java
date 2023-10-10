package com.ecom.library.library.repository;

import com.ecom.library.library.models.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailsRepository extends JpaRepository<OrderDetails,Long> {
    @Query(value ="SELECT * FROM order_detail where order_id =:id",nativeQuery = true)
    List<OrderDetails> findAllByOrderId(Long id);
}
