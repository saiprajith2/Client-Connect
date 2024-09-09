package com.sails.client_connect.service;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class OtpService {
    private static final int otp_length = 6;

    /**
     * Generates a random otp with length = 6
     *
     * @return otp
     */
    public String generateOtp() {
        SecureRandom random = new SecureRandom();
        StringBuilder otp = new StringBuilder(otp_length);
        for (int i = 0; i < otp_length; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }
}
