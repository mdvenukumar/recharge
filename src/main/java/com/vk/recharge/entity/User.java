package com.vk.recharge.entity;

import jakarta.persistence.*;

@Entity
@Table(name="app_user")
public class User {
    @Id
    @SequenceGenerator(name = "customer_id_sequence", sequenceName = "customer_id_sequence", allocationSize = 1)
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE, generator = "customer_id_sequence")
    private int id;
    private String email;
    private String ph;

    public User() {}
    public User(int id, String email, String ph) {
        this.id = id;
        this.email = email;
        this.ph = ph;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhone() {
        return ph;
    }
    public void setPhone(String ph) {
        this.ph = ph;
    }


}
