package com.example.gtech19.service.impl;

import cn.hutool.core.util.StrUtil;
import com.example.gtech19.mapper.UserMapper;
import com.example.gtech19.model.User;
import com.example.gtech19.service.UserService;
import com.example.gtech19.service.impl.dto.request.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 用户服务实现类
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public String insertOrUpdate(LoginRequest request) {
        User user = userMapper.selectByUsername(request.getUserName());
        if (user == null) {
            user = new User();
            String userId = createUserId();
            // 设置默认值
            user.setUserid(userId);
            user.setUsername(request.getUserName());
            user.setNickname(request.getNickName());
            user.setPassword("");
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
        return user.getUserid();
    }

    private String createUserId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    @Override
    public User getUserById(Long id) {
        return userMapper.selectById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userMapper.selectAll();
    }

}