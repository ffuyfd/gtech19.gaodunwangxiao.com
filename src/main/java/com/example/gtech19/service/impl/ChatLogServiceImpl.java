package com.example.gtech19.service.impl;

import com.example.gtech19.mapper.ChatLogMapper;
import com.example.gtech19.model.ChatLog;
import com.example.gtech19.service.ChatLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 日志服务实现类
 */
@Service
public class ChatLogServiceImpl implements ChatLogService {
    
    @Autowired
    private ChatLogMapper chatLogMapper;
    
    @Override
    public boolean createChatLog(ChatLog chatLog) {
        if (chatLog == null) {
            return false;
        }
        // 设置创建时间和更新时间
        Date now = new Date();
        if (chatLog.getCreateTime() == null) {
            chatLog.setCreateTime(now);
        }
        if (chatLog.getUpdateTime() == null) {
            chatLog.setUpdateTime(now);
        }
        return chatLogMapper.insert(chatLog) > 0;
    }

}