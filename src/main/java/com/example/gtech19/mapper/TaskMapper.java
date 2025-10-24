package com.example.gtech19.mapper;

import com.example.gtech19.model.Task;
import com.example.gtech19.service.impl.dto.request.TaskListRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 任务Mapper接口
 */
@Mapper
public interface TaskMapper {
    
    /**
     * 根据ID查询任务
     */
    Task selectById(Long id);
    
    /**
     * 根据任务编码查询任务
     */
    Task selectByTaskCode(@Param("taskCode") String taskCode);
    
    /**
     * 查询所有任务
     */
    List<Task> selectAll();

     /**
     * 根据用户ID查询任务
     */
    List<Task> selectByUserId(@Param("userId") String userId);
    
    /**
     * 根据任务日期查询任务
     */
    List<Task> selectByTaskDate(@Param("taskDate") Date taskDate);
    
    /**
     * 根据任务类型查询任务
     */
    List<Task> selectByTaskType(@Param("taskType") Integer taskType);
    
    /**
     * 根据任务状态查询任务
     */
    List<Task> selectByTaskStatus(@Param("taskStatus") Integer taskStatus);
    
    /**
     * 根据任务来源查询任务
     */
    List<Task> selectByTaskSource(@Param("taskSource") Integer taskSource);
    
    /**
     * 分页查询任务列表
     */
    List<Task> selectByPage(@Param("request") TaskListRequest request);
    
    /**
     * 新增任务
     */
    int insert(Task task);
    
    /**
     * 更新任务
     */
    int update(Task task);

     /**
     * 根据用户ID删除任务
     */
    int deleteUserTask(@Param("userId") String userId, @Param("updateTime") Date updateTime);
}