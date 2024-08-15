package com.vk.recharge.repository;

import com.vk.recharge.entity.Successful;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SuccessfulRepository extends JpaRepository<Successful, Long> {
    Successful findTopByPhoneOrderByRechargeDateDesc(String phone);
}
