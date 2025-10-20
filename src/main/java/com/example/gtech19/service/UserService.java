package com.example.gtech19.service;

import com.example.gtech19.model.User;
import com.example.gtech19.service.impl.dto.request.LoginRequest;
import com.example.gtech19.service.impl.dto.response.UserResponse;

import java.util.List;

/**
 * 用户服务接口
 */
public interface UserService {

    /**
     * 新增或更新用户
     *
     * @param request
     * @return
     */
    String insertOrUpdate(LoginRequest request);

    /**
     * 根据用户ID查询用户
     *
     * @param userId
     * @return
     */
    UserResponse getByUserId(String userId);

    /**
     * 查询所有用户
     */
    List<User> getAllUsers();


}