package com.example.gtech19.service.impl.dto.response;

import lombok.Data;

@Data
public class UserResponse {
    private String userId;
    private String userName;
    private String nickName;
    private String school;
    private String grade;
    private String major;
    private String target;
}