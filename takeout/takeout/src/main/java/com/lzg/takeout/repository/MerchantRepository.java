package com.lzg.takeout.repository;

import com.lzg.takeout.entity.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MerchantRepository extends JpaRepository<Merchant, Long> {
    Optional<Merchant> findByUserId(Long userId);
}
