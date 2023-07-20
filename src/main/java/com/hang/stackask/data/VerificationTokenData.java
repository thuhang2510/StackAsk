package com.hang.stackask.data;

import lombok.*;
import java.util.Date;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VerificationTokenData {
    private Long id;
    private String token;
    private Long userId;
    private Date expiryDate;
}
