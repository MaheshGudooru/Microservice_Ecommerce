package com.techouts.user_service.controller;

import com.techouts.user_service.model.User;
import com.techouts.user_service.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("register")
    public ResponseEntity<Map<String, Object>> registerUser(@Valid @ModelAttribute("user") User user, BindingResult result) {


        if (result.hasErrors()) {

            Map<String, Object> validationErrors = new HashMap<> ();

            result.getFieldErrors().forEach (fieldError -> {
                validationErrors.put (fieldError.getField (), fieldError.getDefaultMessage ());
            });

            return ResponseEntity.status (HttpStatus.BAD_REQUEST).body (validationErrors);

        }

        boolean userRegistrationStatus = userService.registerUser(user);

        Map<String, Object> response = new HashMap<> ();

        if(userRegistrationStatus) {
            response.put("message", "successfully registered");
            return ResponseEntity.status (HttpStatus.CREATED).body (response);
        }

        response.put ("message", "user with same email already exists");
        return ResponseEntity.status (HttpStatus.CONFLICT).body (response);

    }

    @PostMapping("login")
    public ResponseEntity<?> userLogin() {
        // TODO

        return null;
    }

}
