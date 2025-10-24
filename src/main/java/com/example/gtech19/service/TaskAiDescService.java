package com.example.gtech19.service;

import com.example.gtech19.model.TaskAiDesc;

/**
 * 任务AI详情服务接口
 */
public interface TaskAiDescService {
    
    /**
     * 根据任务ID查询详情
     * @param taskId 任务ID
     * @return 任务AI详情
     */
    TaskAiDesc getTaskAiDescByTaskId(Long taskId);
    
    /**
     * 创建任务AI详情
     * @param taskAiDesc 任务AI详情
     * @return 是否创建成功
     */
    boolean createTaskAiDesc(TaskAiDesc taskAiDesc);
}