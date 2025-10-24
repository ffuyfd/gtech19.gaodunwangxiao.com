package com.example.gtech19.service.impl;

import com.example.gtech19.mapper.TaskAiDescMapper;
import com.example.gtech19.model.TaskAiDesc;
import com.example.gtech19.service.TaskAiDescService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 任务AI详情服务实现类
 */
@Service
public class TaskAiDescServiceImpl implements TaskAiDescService {
    
    @Autowired
    private TaskAiDescMapper taskAiDescMapper;
    
    @Override
    public TaskAiDesc getTaskAiDescByTaskId(Long taskId) {
        if (taskId == null) {
            return null;
        }
        return taskAiDescMapper.selectByTaskId(taskId);
    }
    
    @Override
    public boolean createTaskAiDesc(TaskAiDesc taskAiDesc) {
        if (taskAiDesc == null || taskAiDesc.getTaskId() == null) {
            return false;
        }
        return taskAiDescMapper.insert(taskAiDesc) > 0;
    }
}