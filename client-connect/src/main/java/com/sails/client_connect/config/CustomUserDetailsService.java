package com.sails.client_connect.config;

import com.sails.client_connect.entity.User;
import com.sails.client_connect.exception.PasswordExpiredException;
import com.sails.client_connect.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    private static final long PASSWORD_EXPIRY_DAYS = 90;


    /**
     * @param username While Logging into the System checks if the username entered and username in database are same
     * @return UserDetails of that user
     * If User not present in database then
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        //Call this method to verify expiry
        checkPasswordExpiry(user);

        return new CustomUserDetails(user);

    }

    /**
     * @param user Checks if the last updated password is still before the expiry time
     */
    private void checkPasswordExpiry(User user) {
        LocalDateTime passwordLastSet = user.getPasswordLastSet();
        if (passwordLastSet != null) {
            LocalDateTime expiryDate = passwordLastSet.plusDays(PASSWORD_EXPIRY_DAYS);

            //checks if present date is after the expiry date
            if (LocalDateTime.now().isAfter(expiryDate)) {
                throw new PasswordExpiredException("Password expired, please change your password.");
            }
        }
    }


}
