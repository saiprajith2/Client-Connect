package com.sails.client_connect.service;

import com.sails.client_connect.dto.UserAuth;
import com.sails.client_connect.dto.UserDTO;
import com.sails.client_connect.entity.Role;
import com.sails.client_connect.entity.RoleName;
import com.sails.client_connect.entity.User;
import com.sails.client_connect.repository.RoleRepository;
import com.sails.client_connect.repository.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {


    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    /**
     * @param userAuth Check if Admin is already present in database if not only then add Admin
     *                 Create a User object and set the details in it
     *                 Save the user in database and send the required details to email service
     * @throws MessagingException
     */
    public void saveUser(UserAuth userAuth) throws MessagingException {

        //checks if a admin already present in database
        if (userAuth.getRoleNames().contains(RoleName.ADMIN)) {
            if (userRepository.findByRoles_Name(RoleName.ADMIN).isPresent()) {
                throw new IllegalStateException("An admin already exists. Cannot add another admin.");
            }
        }

        String dummyPassword = userAuth.getPassword();
        String encodedPassword = passwordEncoder.encode(dummyPassword); //hashing the password
        String fromEmail = "dummy.rip69@gmail.com";


        User user = new User();
        user.setUsername(userAuth.getUsername());
        user.setEmail(userAuth.getEmail());
        user.setPassword(encodedPassword);

        Set<Role> roles = userAuth.getRoleNames().stream()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleName)))
                .collect(Collectors.toSet());

        user.setRoles(roles);
        user.setPasswordLastSet(LocalDateTime.now());


        userRepository.save(user);

        //send the details required to send email
        emailService.sendDynamicEmail(fromEmail,
                user.getEmail(),
                "Your Account Credentials",
                "Username: " + user.getUsername() + "\nPassword: " + dummyPassword);
    }

    /**
     * Fetch all the users from the database
     *
     * @return List of users
     */
    public List<UserDTO> findAllUsers() {

        return userRepository.findAllWithRoles().stream()
                .map(user -> new UserDTO(
                        user.getUser_id(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getRoles().stream().map(role -> role.getName()).collect(Collectors.toSet())
                ))
                .collect(Collectors.toList());
    }

    /**
     * @param username Fetch the user details using username
     * @return User details that are fetched
     */
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }

    /**
     * @param username
     * @param newPassword Check if username is present in database and update the password
     */
    public void updatePassword(String username, String newPassword) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setPasswordLastSet(LocalDateTime.now());
        userRepository.save(user);
    }


}
