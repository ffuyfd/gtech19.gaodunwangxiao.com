package com.example.gtech19.service.helper;

import cn.hutool.core.util.StrUtil;
import com.example.gtech19.service.UserService;
import com.example.gtech19.service.impl.dto.response.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserHelper {
    @Autowired
    private UserService userService;
    /**
     * 检查用户是否登录
     *
     * @param userId 用户ID
     * @return 如果用户已登录则返回true，否则返回false
     */
    public boolean checkUserLogin(String userId) {
        if (StrUtil.isBlank(userId)) {
            return false;
        }
        UserResponse userResponse = userService.getByUserId(userId);
        return userResponse != null;
    }
}
