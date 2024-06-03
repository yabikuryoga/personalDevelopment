package com.example.springwebtask.Form;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoginForm {

@NotEmpty(message = "{NotEmpty_message_id}")
private String loginId;

@NotEmpty(message = "{NotEmpty_message_pass}")
private String pass;
}
