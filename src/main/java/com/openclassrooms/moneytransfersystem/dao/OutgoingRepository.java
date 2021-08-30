package com.openclassrooms.moneytransfersystem.dao;

import com.openclassrooms.moneytransfersystem.model.Ingoing;
import com.openclassrooms.moneytransfersystem.model.Outgoing;
import com.openclassrooms.moneytransfersystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OutgoingRepository extends JpaRepository<Outgoing, Long> {

    @Query("SELECT u FROM Ingoing u WHERE u.outgoing.id = ?1")
    User findByIngoing(Long ingoingId);
}
