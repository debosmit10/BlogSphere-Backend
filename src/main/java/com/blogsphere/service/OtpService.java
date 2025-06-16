package com.blogsphere.service;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class OtpService {

    private static final int OTP_LENGTH = 6;
    private static final long EXPIRATION_TIME_MS = 5 * 60 * 1000; // 5 minutes

    private final ConcurrentHashMap<String, OtpEntry> otpStore = new ConcurrentHashMap<>();

    public String generateOtp(String email) {
        String otp = String.format("%0" + OTP_LENGTH + "d", new Random().nextInt((int) Math.pow(10, OTP_LENGTH)));
        otpStore.put(email, new OtpEntry(otp, System.currentTimeMillis() + EXPIRATION_TIME_MS));
        return otp;
    }

    public boolean validateOtp(String email, String otp) {
        OtpEntry otpEntry = otpStore.get(email);
        if (otpEntry == null) {
            return false;
        }
        if (System.currentTimeMillis() > otpEntry.getExpirationTime()) {
            otpStore.remove(email); // OTP expired
            return false;
        }
        return otpEntry.getOtp().equals(otp);
    }

    public void clearOtp(String email) {
        otpStore.remove(email);
    }

    private static class OtpEntry {
        private final String otp;
        private final long expirationTime;

        public OtpEntry(String otp, long expirationTime) {
            this.otp = otp;
            this.expirationTime = expirationTime;
        }

        public String getOtp() {
            return otp;
        }

        public long getExpirationTime() {
            return expirationTime;
        }
    }
}