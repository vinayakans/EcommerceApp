package com.ecom.library.library.repository;

import com.ecom.library.library.models.WalletHistory;
import jdk.dynalink.linker.LinkerServices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WalletHistoryRepository extends JpaRepository<WalletHistory,Long>{
    WalletHistory save(WalletHistory walletHistory);
    @Query(value = "select * from wallet_history where wallet_id=:id",nativeQuery = true)
    List<WalletHistory> findAllById(@Param("id")Long id);

    WalletHistory getReferenceById(Long id);
}
