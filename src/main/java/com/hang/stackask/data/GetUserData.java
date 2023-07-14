package com.hang.stackask.data;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetUserData {
    private String email;
    private String password;
}
