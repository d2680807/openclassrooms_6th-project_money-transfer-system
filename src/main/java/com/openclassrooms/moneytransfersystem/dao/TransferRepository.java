package com.openclassrooms.moneytransfersystem.dao;

import com.openclassrooms.moneytransfersystem.model.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {


}
