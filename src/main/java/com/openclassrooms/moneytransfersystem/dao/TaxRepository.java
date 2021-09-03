package com.openclassrooms.moneytransfersystem.dao;

import com.openclassrooms.moneytransfersystem.model.Tax;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TaxRepository extends JpaRepository<Tax, Long> {

    @Query("SELECT u FROM Tax u WHERE u.name = ?1")
    Tax findByName(String name);
}
