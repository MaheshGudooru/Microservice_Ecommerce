package com.techouts.user_service.controller;

import com.techouts.user_service.dto.UserDTO;
import com.techouts.user_service.model.User;
import com.techouts.user_service.service.UserService;
import com.techouts.user_service.utils.JwtUtil;
import jakarta.validation.Valid;
import org.bouncycastle.asn1.bc.ObjectData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    private final JwtUtil jwtUtil;

    UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping
    public ResponseEntity<UserDTO> getUserDetails(@RequestHeader("X-User-Id") Integer userId) {

        UserDTO foundUserDTO = userService.getUserById (userId);

        return ResponseEntity.ok (foundUserDTO);

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
    public ResponseEntity<Map<String, Object>> userLogin(@RequestParam("email") String email,
                                                         @RequestParam("password") String password) {

        User userInQuestion = userService.isValidUser (email, password);
        Map<String, Object> response = new HashMap<> ();

        if(userInQuestion == null) {
            response.put ("message", "Invalid credentials");
            return ResponseEntity.status (HttpStatus.BAD_REQUEST).body (response);
        }

        User user = userService.getUser (email);

        String JWTtoken = jwtUtil.generateToken (user);

        return ResponseEntity.ok (Map.of ("token", JWTtoken));

    }

}
