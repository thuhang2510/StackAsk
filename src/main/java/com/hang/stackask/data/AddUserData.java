package com.hang.stackask.data;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddUserData {
    private String userName;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String password;
}
