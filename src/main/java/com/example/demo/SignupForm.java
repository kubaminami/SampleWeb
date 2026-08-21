package com.example.demo;
import jakarta.validation.constraints.Max;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SignupForm {

    @Max(50)
    private String name;

    @Max(255)
    private String email;

    @Max(64)
    private String password;

}