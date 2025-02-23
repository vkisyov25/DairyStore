package com.springSecurity.JWT.Models.dtos;

import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class LoginUserDto {

    private String username;
    private String password;
}
