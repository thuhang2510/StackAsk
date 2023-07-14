package com.hang.stackask.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @NotBlank(message = "Email not blank")
    @Email(message = "Invalid email")
    private String email;

    @NotBlank(message = "Password not blank")
    private String password;
}
