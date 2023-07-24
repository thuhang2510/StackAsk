package com.hang.stackask.data;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAccountData {
    private String fullName;
    private String email;
    private String phoneNumber;
}
