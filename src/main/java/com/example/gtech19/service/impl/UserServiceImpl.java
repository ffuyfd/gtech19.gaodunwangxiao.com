package com.example.gtech19.service.impl;

import cn.hutool.core.util.StrUtil;
import com.example.gtech19.mapper.UserMapper;
import com.example.gtech19.model.User;
import com.example.gtech19.service.UserService;
import com.example.gtech19.service.helper.TaskHelper;
import com.example.gtech19.service.impl.dto.request.LoginRequest;
import com.example.gtech19.service.impl.dto.request.UserUpdateRequest;
import com.example.gtech19.service.impl.dto.response.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * 用户服务实现类
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TaskHelper taskHelper;

    @Override
    public UserResponse insertOrUpdate(LoginRequest request) {
        User user = userMapper.selectByUsername(request.getUserName());
        if (user == null) {
            user = new User();
            String userId = createUserId();
            // 设置默认值
            user.setUserid(userId);
            user.setUsername(request.getUserName());
            user.setNickname(request.getNickName());
            user.setSchool("");
            user.setPassword("");
            user.setGrade("");
            user.setMajor("");
            user.setTarget("");
            user.setPoints(0);
            user.setCreateTime(new Date());
            user.setUpdateTime(new Date());
            // 新增用户
            userMapper.insert(user);
        } else {
            // 更新用户
            if (StrUtil.isBlank(user.getUserid())) {
                user.setUserid(createUserId());
            }
            user.setNickname(request.getNickName());
            user.setUpdateTime(new Date());
            userMapper.update(user);
        }
        return getByUserId(user.getUserid());
    }

    @Override
    public UserResponse update(UserUpdateRequest request) {
        User user = userMapper.selectByUserId(request.getUserId());
        if (user == null) {
            return null;
        }
        user.setGrade(request.getGrade());
        user.setMajor(request.getMajor());
        user.setTarget(request.getTarget());
        user.setSchool(request.getSchool());
        user.setUpdateTime(new Date());
        userMapper.update(user);

        //异步调用
        CompletableFuture.runAsync(() -> taskHelper.firstInitTask(request.getUserId()));
        return getByUserId(request.getUserId());
    }

    private String createUserId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    @Override
    public UserResponse getByUserId(String userId) {
        User user = userMapper.selectByUserId(userId);
        if (user == null) {
            return null;
        }
        UserResponse response = new UserResponse();
        response.setUserId(user.getUserid());
        response.setUserName(user.getUsername());
        response.setNickName(user.getNickname());
        response.setSchool(user.getSchool());
        response.setGrade(user.getGrade());
        response.setMajor(user.getMajor());
        response.setTarget(user.getTarget());
        response.setPoints(user.getPoints());
        return response;
    }

    @Override
    public List<User> getAllUsers() {
        return userMapper.selectAll();
    }

    @Override
    public void updatePoints(Integer points, String userId) {
        userMapper.updatePoints(points, userId);
    }

}