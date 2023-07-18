package com.hang.stackask.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForgotPasswordRequest {
    @NotBlank(message = "Email not blank")
    @Email(message = "Invalid email")
    private String email;
}
