package com.vk.recharge.service;

import com.vk.recharge.entity.Plan;
import com.vk.recharge.repository.PlanRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class PlanService {

    @Autowired
    private PlanRepository planRepository;

    @PostConstruct
    public void init() {
        // Mock data
        List<Plan> plans = Arrays.asList(
                new Plan("Basic Plan", 99, 25, "Netflix"),
                new Plan("Data Beast", 499, 56, "HotStar"),
                new Plan("Music Mania", 345, 21, "Jio Saavan"),
                new Plan("Kid's Choice", 42, 699, "Sony LIV")

        );
        planRepository.saveAll(plans);
    }

    public List<Plan> getAllPlans() {
        return planRepository.findAll();
    }

    public Plan getPlanById(int id) {
        Optional<Plan> plan = planRepository.findById((long) id);
        return plan.orElse(null); // Return null if not found
    }

}
