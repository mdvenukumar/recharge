package com.vk.recharge.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "successful_recharge")
public class Successful {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String phone;
    private int planId;
    private LocalDate rechargeDate;
    private LocalDate validityEndDate;
    private String email;

    public Successful() {
    }

    public Successful(String phone, int planId, LocalDate rechargeDate, LocalDate validityEndDate, String email) {
        this.phone = phone;
        this.planId = planId;
        this.rechargeDate = rechargeDate;
        this.validityEndDate = validityEndDate;
        this.email = email;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getPlanId() {
        return planId;
    }

    public void setPlanId(int planId) {
        this.planId = planId;
    }

    public LocalDate getRechargeDate() {
        return rechargeDate;
    }

    public void setRechargeDate(LocalDate rechargeDate) {
        this.rechargeDate = rechargeDate;
    }

    public LocalDate getValidityEndDate() {
        return validityEndDate;
    }

    public void setValidityEndDate(LocalDate validityEndDate) {
        this.validityEndDate = validityEndDate;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
