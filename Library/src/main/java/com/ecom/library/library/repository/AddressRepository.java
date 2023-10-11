package com.ecom.library.library.repository;

import com.ecom.library.library.models.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address,Long> {
    @Query("select c from Address c where c.customer.id =:id and c.isDeleted = false")
    List<Address> findAllById(@Param("id") Long id);
}
