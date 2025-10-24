package com.example.gtech19.mapper;

import com.example.gtech19.model.TaskAiDesc;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 任务AI详情表Mapper接口
 */
@Mapper
public interface TaskAiDescMapper {
    
    /**
     * 根据任务ID查询详情
     */
    TaskAiDesc selectByTaskId(@Param("taskId") Long taskId);
    
    /**
     * 新增任务AI详情
     */
    int insert(TaskAiDesc taskAiDesc);
}