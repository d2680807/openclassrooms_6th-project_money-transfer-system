package com.openclassrooms.moneytransfersystem.dao;

import com.openclassrooms.moneytransfersystem.model.Transfer;
import com.openclassrooms.moneytransfersystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {

    @Query("SELECT t FROM Transfer t WHERE t.user.id = ?1")
    Transfer findByUserId(Long userId);
}
