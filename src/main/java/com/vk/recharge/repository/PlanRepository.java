package com.vk.recharge.repository;

import com.vk.recharge.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {
    Optional<Plan> findById(Long id);
}

