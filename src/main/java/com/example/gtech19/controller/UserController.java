package com.example.gtech19.controller;

import com.example.gtech19.common.Result;
import com.example.gtech19.model.User;
import com.example.gtech19.service.UserService;
import com.example.gtech19.service.impl.dto.request.LoginRequest;
import com.example.gtech19.service.impl.dto.request.UserUpdateRequest;
import com.example.gtech19.service.impl.dto.response.UserResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户控制器
 * 提供RESTful API接口
 */
@RestController
@RequestMapping("/api/v1/user")
@Api(tags = "用户管理接口", description = "提供用户管理相关的API接口")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 登录接口
     */
    @PostMapping("/login")
    @ApiOperation(value = "用户登录", notes = "用户登录接口，返回用户ID")
    public Result<String> login(
            @ApiParam(name = "request", value = "登录请求参数", required = true)
            @RequestBody LoginRequest request) {
        String userId = userService.insertOrUpdate(request);
        if (userId != null) {
            return Result.success(userId);
        } else {
            return Result.error(404, "用户不存在");
        }
    }

    /**
     * 更新用户信息
     */
    @PostMapping("/update")
    @ApiOperation(value = "更新用户信息", notes = "更新用户信息接口")
    public Result<UserResponse> update(
            @ApiParam(name = "request", value = "用户更新请求参数", required = true)
            @RequestBody UserUpdateRequest request) {
        if (request == null || request.getUserId() == null) {
            return Result.error(500, "用户ID不能为空");
        }
        UserResponse response = userService.update(request);
        if (response != null) {
            return Result.success(response);
        } else {
            return Result.error(404, "用户不存在");
        }
    }

    /**
     * 根据ID查询用户
     */
    @GetMapping("/detail/{userId}")
    @ApiOperation(value = "查询用户详情", notes = "根据用户ID查询用户详细信息")
    public Result<UserResponse> detail(
            @ApiParam(name = "userId", value = "用户ID", required = true, example = "user123")
            @PathVariable String userId) {
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
    @ApiOperation(value = "查询所有用户", notes = "查询系统中的所有用户信息")
    public Result<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return Result.success(users);
    }


    /**
     * 健康检查接口
     */
    @GetMapping("/health")
    @ApiOperation(value = "服务健康检查", notes = "检查系统服务是否正常运行")
    public Result<String> healthCheck() {
        return Result.success("服务正常运行");
    }
}