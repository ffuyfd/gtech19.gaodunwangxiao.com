package com.example.gtech19.controller;

import com.example.gtech19.common.Result;
import com.example.gtech19.model.User;
import com.example.gtech19.service.UserService;
import com.example.gtech19.service.impl.dto.request.LoginRequest;
import com.example.gtech19.service.impl.dto.response.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户控制器
 * 提供RESTful API接口
 */
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 根据ID查询用户
     */
    @PostMapping("/login")
    public Result<String> login(@RequestBody LoginRequest request) {
        String userId = userService.insertOrUpdate(request);
        if (userId != null) {
            return Result.success(userId);
        } else {
            return Result.error(404, "用户不存在");
        }
    }

    /**
     * 根据ID查询用户
     */
    @GetMapping("/detail/{userId}")
    public Result<UserResponse> detail(@PathVariable String userId) {
        UserResponse user = userService.getByUserId(userId);
        if (user != null) {
            return Result.success(user);
        } else {
            return Result.error(404, "用户不存在");
        }
    }

    /**
     * 查询所有用户
     */
    @GetMapping("/list")
    public Result<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return Result.success(users);
    }


    /**
     * 健康检查接口
     */
    @GetMapping("/health")
    public Result<String> healthCheck() {
        return Result.success("服务正常运行");
    }
}