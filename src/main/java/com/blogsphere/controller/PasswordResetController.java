package com.blogsphere.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blogsphere.dto.ForgotPasswordRequest;
import com.blogsphere.dto.ResetPasswordRequest;
import com.blogsphere.dto.VerifyOtpRequest;
import com.blogsphere.exception.ResourceNotFoundException;
import com.blogsphere.service.EmailService;
import com.blogsphere.service.OtpService;
import com.blogsphere.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth" )
@RequiredArgsConstructor
public class PasswordResetController {

    private final UserService userService;
    private final OtpService otpService;
    private final EmailService emailService;

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        try {
            userService.findByEmail(request.getEmail());
            String otp = otpService.generateOtp(request.getEmail());
            emailService.sendSimpleMessage(request.getEmail(), "Password Reset OTP", "Your OTP for password reset is: " + otp);
            return ResponseEntity.ok("OTP sent to email.");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body("User with this email does not exist.");
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody VerifyOtpRequest request) {
        if (otpService.validateOtp(request.getEmail(), request.getOtp())) {
            otpService.clearOtp(request.getEmail());
            return ResponseEntity.ok("OTP verified successfully.");
        } else {
            return ResponseEntity.badRequest().body("Invalid OTP.");
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request) {
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            return ResponseEntity.badRequest().body("New password and confirm password do not match.");
        }
        userService.updatePassword(request.getEmail(), request.getNewPassword());
        return ResponseEntity.ok("Password reset successfully.");
    }
}