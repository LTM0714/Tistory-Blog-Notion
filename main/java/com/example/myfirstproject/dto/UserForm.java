package com.example.myfirstproject.dto;

import com.example.myfirstproject.entity.User;
import com.example.myfirstproject.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class UserForm {
    private Long id;
    private String loginId;
    private String password;
    private String nickname;
    private UserRole role;

    public User toEntity(){
        return new User(id, loginId, password, nickname, role);
    }
}
