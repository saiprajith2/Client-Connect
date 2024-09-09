package com.sails.client_connect.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class OtpServiceTest {
    //Initializer the OtpService
    private final OtpService otpService = new OtpService();

    @Test
    void testGenerateOtp() {
        // Act
        //A Dynamically generated otp
        String otp = otpService.generateOtp();
        //A static otp for comparison
        String otp2 = "12345";

        // Assert
        //To check the dynamically generated otp is 6 digits
        assertEquals(6, otp.length(), "OTP should be 6 digits long");
        //To check the static generated otp fails to meet the length condition
        assertNotSame(6, otp2.length());
        //To Check if otp contains digits only
        assertTrue(otp.chars().allMatch(Character::isDigit), "OTP should only contain digits");
    }
}
