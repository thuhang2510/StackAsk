package com.hang.stackask.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_user")
public class User extends CommonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    @NotBlank(message = "User name is mandatory")
    private String userName;

    @Column(name = "fullname")
    private String fullName;

    @NotBlank(message = "Email is mandatory")
    @Email
    private String email;

    @Column(name = "phonenumber")
    private String phoneNumber;

    @NotBlank(message = "Password is mandatory")
    private String password;

    @Builder.Default
    private Boolean enabled = false;

    @Column(name = "reset_password_token")
    private String resetPasswordToken;

    @Lob
    @Column(name = "avatar", columnDefinition = "BLOB")
    private byte[] avatar;
}