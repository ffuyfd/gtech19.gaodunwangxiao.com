package com.example.gtech19.service;

import com.example.gtech19.model.ChatLog;

import java.util.Date;
import java.util.List;

/**
 * 日志服务接口
 */
public interface ChatLogService {
    
    /**
     * 创建日志
     * @param chatLog 日志信息
     * @return 是否创建成功
     */
    boolean createChatLog(ChatLog chatLog);

}