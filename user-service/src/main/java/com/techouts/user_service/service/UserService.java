package com.techouts.user_service.service;


import com.techouts.user_service.dto.UserDTO;
import com.techouts.user_service.model.User;
import com.techouts.user_service.repository.UserRepo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class UserService {

    UserRepo userRepoImpl;
    PasswordEncoder encoder;

    UserService(UserRepo userRepoImpl, PasswordEncoder encoder) {
        this.userRepoImpl = userRepoImpl;
        this.encoder = encoder;
    }

    @Transactional(readOnly = true)
    public User getUser(String email) {

        return userRepoImpl.findByEmail(email).orElse (null);

    }

    @Transactional(readOnly = true)
    public User isValidUser(String email, String password) {

        User userTryingToLogin = getUser (email);

        if(userTryingToLogin == null || !encoder.matches (password, userTryingToLogin.getPassword ())) {
            return null;
        }

        return userTryingToLogin;

    }

    @Transactional
    public boolean registerUser(User user) {
        user.setPassword(encoder.encode(user.getPassword()));

        if (userRepoImpl.findByEmail(user.getEmail()).isPresent()) {

            return false;
        }

        user.setJoinedDate(LocalDate.now());
        userRepoImpl.save(user);

        return true;

    }

    public UserDTO getUserById(int userId) {

        User user = userRepoImpl.findById (userId).orElse (null);

        if(user == null) {
            return new UserDTO ("User does not exists");
        }

        return new UserDTO (
                "User found",
                user.getId (),
                user.getName (),
                user.getEmail (),
                user.getFormattedJoinedDate ()
        );

    }

    @Transactional
    public User updateUserDetails(String emailAddress, String fullName, User currLoggedInUser) {

        User user = userRepoImpl.findByEmail(emailAddress).orElse(null);

        if (user != null) {
            if (user.getId() == currLoggedInUser.getId()) {
                currLoggedInUser.setName(fullName);
                userRepoImpl.save (currLoggedInUser);
                return currLoggedInUser;
            }

        } else {
            currLoggedInUser.setName(fullName);
            currLoggedInUser.setEmail(emailAddress);

            userRepoImpl.save (currLoggedInUser);
            return currLoggedInUser;
        }

        return null;

    }
}

