package com.techouts.user_service.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private String message;

    private int id;

    private String name;

    private String email;

    private String joinedDate;

    public UserDTO(String message) {
        this.message = message;
    }

}
