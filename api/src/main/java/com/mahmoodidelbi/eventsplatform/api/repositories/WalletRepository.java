package com.mahmoodidelbi.eventsplatform.api.repositories;

import com.mahmoodidelbi.eventsplatform.api.entites.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {
    
    Optional<Wallet> findByUserId(Long userId);
}
