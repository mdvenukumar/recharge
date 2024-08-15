package com.vk.recharge.repository;



import com.vk.recharge.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    boolean existsByPh(String ph);
    User findByPh(String phone);
}
