package com.sails.client_connect.controller;

import com.sails.client_connect.dto.*;
import com.sails.client_connect.entity.RefreshToken;
import com.sails.client_connect.entity.RoleName;
import com.sails.client_connect.entity.User;
import com.sails.client_connect.repository.UserRepository;
import com.sails.client_connect.service.*;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final EmailService emailService;
    private final OtpService otpService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;


    private Map<String, String> otpStore = new HashMap<>();
    private Map<String, Long> otpExpiryStore = new HashMap<>();

    /**
     * @param userAuthRequest
     * @param session         Authenticates the username and password entered while login
     *                        If authenticated generate otp with expiry time and sends it to the users email using email service
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody UserAuthRequest userAuthRequest, HttpSession session) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userAuthRequest.getUsername(),
                        userAuthRequest.getPassword()
                )
        );

        if (authenticate.isAuthenticated()) {

            User authenticatedUser = userService.findByUsername(userAuthRequest.getUsername());
            session.setAttribute("userId", authenticatedUser.getUser_id());

        } else {
            throw new UsernameNotFoundException("invalid user request !");
        }

        String otp = otpService.generateOtp();
        otpStore.put(userAuthRequest.getUsername(), otp);
        otpExpiryStore.put(userAuthRequest.getUsername(), System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(5));

        User user = userService.findByUsername(userAuthRequest.getUsername());
        emailService.sendOtp(user.getEmail(), otp);

        return ResponseEntity.ok("OTP has been sent to your email.");
    }



    /**
     * @param otpRequestDto Verifies the otp stored and otp sent in body
     *                      If all conditions satisfy then it generates access token along with refresh token
     * @return message written in response entity according the conditions written
     */
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@Valid @RequestBody OtpRequestDTO otpRequestDto) {

        if (!otpStore.containsKey(otpRequestDto.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("OTP not found for this user.");
        }

        Long expiryTime = otpExpiryStore.get(otpRequestDto.getUsername());
        if (expiryTime < System.currentTimeMillis()) {
            otpStore.remove(otpRequestDto.getUsername());
            otpExpiryStore.remove(otpRequestDto.getUsername());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("OTP has expired.");
        }


        String storedOtp = otpStore.get(otpRequestDto.getUsername());
        if (!storedOtp.equals(otpRequestDto.getOtp())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid OTP.");
        }

        otpStore.remove(otpRequestDto.getUsername());
        otpExpiryStore.remove(otpRequestDto.getUsername());

        String jwt = jwtService.generateToken(otpRequestDto.getUsername());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(otpRequestDto.getUsername());

        JwtResponseDTO responseDto = JwtResponseDTO.builder()
                .accessToken(jwt)
                .refreshToken(refreshToken.getToken())
                .message("Access Token and Refresh Token are created")
                .build();

        return ResponseEntity.ok(responseDto);
    }


    /**
     * @param refreshTokenRequest Verifies if the refresh stored and sent in request are matched
     *                            If all conditions satisfy then generated a new access token with it
     * @return A new access token is given for continuing session
     */
    @PostMapping("/refreshToken")
    public ResponseEntity<JwtResponseDTO> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return refreshTokenService.findByToken(refreshTokenRequest.getToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {

                    //generate a new token with username from request
                    String accessToken = jwtService.generateToken(user.getUsername());

                    JwtResponseDTO responseDto = JwtResponseDTO.builder()
                            .accessToken(accessToken)
                            .refreshToken(refreshTokenRequest.getToken())
                            .message("New Access Token has been generated.")
                            .build();

                    return ResponseEntity.ok(responseDto);
                })
                .orElseThrow(() -> new RuntimeException("Refresh token is not in database!"));
    }

    /**
     * @param username
     * @param newPassword It is used to change the password
     * @return Password changed message
     */
    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestParam String username, @RequestParam String newPassword) {
        userService.updatePassword(username, newPassword);
        return ResponseEntity.ok("Password changed successfully");
    }

    /**
     * @param userAuth Checks if the role of user being added is admin and continues if it validates.
     *                 If role is not admin
     * @return A message that only admin has access to add a new user
     */
    @PostMapping("/addAdmin")
    public ResponseEntity<String> addUser(@Valid @RequestBody UserAuth userAuth) throws MessagingException {

        if (!userAuth.getRoleNames().contains(RoleName.ADMIN)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Only admins can add new users into system.");
        }
        userService.saveUser(userAuth);
        return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
    }


}
