package com.hang.stackask.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "User name not blank")
    private String userName;

    @NotBlank(message = "Full name not blank")
    private String fullName;

    @NotBlank(message = "Email not blank")
    @Email(message = "Email invalid")
    private String email;

    @NotBlank(message = "Password is not blank")
    private String password;

    @NotBlank(message = "Retype password is not blank")
    private String retypePassword;
}
