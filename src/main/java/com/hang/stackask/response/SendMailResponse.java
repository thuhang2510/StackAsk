package com.hang.stackask.response;

import lombok.*;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendMailResponse {
    private String resetPasswordToken;
}
