package com.techouts.user_service.service;


import com.techouts.user_service.model.User;
import com.techouts.user_service.repository.UserRepo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

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

        Optional<User> user = userRepoImpl.findByEmail(email);
        return user.orElseGet(User::new);

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

