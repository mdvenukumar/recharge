package com.vk.recharge;

import com.vk.recharge.entity.Plan;
import com.vk.recharge.entity.Successful;
import com.vk.recharge.entity.User;
import com.vk.recharge.repository.SuccessfulRepository;
import com.vk.recharge.repository.UserRepository;
import com.vk.recharge.service.EmailService;
import com.vk.recharge.service.PlanService;
import com.vk.recharge.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.temporal.ChronoUnit;

import java.time.LocalDate;
import java.util.List;

@SpringBootApplication
@RestController
public class RechargeApplication {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PlanService planService;
    private final UserService userService;
    private final SuccessfulRepository successfulRepository;

    @Autowired
    public RechargeApplication(UserRepository userRepository, EmailService emailService, PlanService planService, UserService userService, SuccessfulRepository successfulRepository) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.planService = planService;
        this.userService = userService;
        this.successfulRepository = successfulRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(RechargeApplication.class, args);
    }

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/getusers")
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public record NewUserRequest(String name, String email, String ph) {}

    @PostMapping("/adduser")
    public ResponseEntity<String> addUser(@RequestBody NewUserRequest request) {
        try {
            User user = new User();
            user.setEmail(request.email());
            user.setPhone(request.ph());
            userRepository.save(user);

            // Send registration success email
            emailService.sendRegistrationSuccessEmail(request.email(), request.ph());

            return ResponseEntity.ok("User added successfully and email sent");
        } catch (DataAccessException dae) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Database error: " + dae.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }

    @GetMapping("/plans")
    public List<Plan> getPlans() {
        return planService.getAllPlans();
    }

    @GetMapping("/check-phone")
    public ResponseEntity<Boolean> checkPhoneNumber(@RequestParam("phone") String phone) {
        boolean exists = userService.isPhoneNumberExists(phone);
        return ResponseEntity.ok(exists);
    }


    @PostMapping("/recharge")
    public ResponseEntity<String> recharge(@RequestParam String phone, @RequestParam int planId) {
        try {
            // Fetch the User by Phone
            User user = userRepository.findByPh(phone);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            // Fetch the Plan by ID
            Plan plan = planService.getPlanById(planId);
            if (plan == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Plan not found");
            }

            // Fetch the latest Successful recharge record for the user
            Successful latestRecharge = successfulRepository.findTopByPhoneOrderByRechargeDateDesc(phone);
            LocalDate currentDate = LocalDate.now();

            if (latestRecharge != null) {
                LocalDate previousRechargeDate = latestRecharge.getRechargeDate();
                LocalDate previousValidityEndDate = latestRecharge.getValidityEndDate();
                // Calculate the difference in days between the previous validity end date and current date
                long daysBetween = ChronoUnit.DAYS.between(previousRechargeDate, previousValidityEndDate);

                // Check if the difference is more than 3 days
                if (daysBetween > 3) {
                    return ResponseEntity.ok("Recharge not required now, validity exceeds the next 3 days.");
                }
            }

            // Calculate the new validity end date
            LocalDate rechargeDate = LocalDate.now();
            LocalDate newValidityEndDate = rechargeDate.plusDays(plan.getValidity());

            // Record the successful recharge with email
            Successful successfulRecharge = new Successful(phone, planId, rechargeDate, newValidityEndDate, user.getEmail());
            successfulRepository.save(successfulRecharge);

            // Send the confirmation email
            emailService.sendRechargeSuccessEmail(user.getEmail(), plan, newValidityEndDate);

            return ResponseEntity.ok("Recharge successful and email sent");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }



    @GetMapping("/successful-recharges")
    public List<Successful> getSuccessfulRecharges() {
        return successfulRepository.findAll();
    }


}
