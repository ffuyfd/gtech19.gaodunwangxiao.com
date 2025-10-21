package com.example.gtech19.service.impl.dto.request;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private String grade;
    private String major;
    private String target;
    private String userId;
}