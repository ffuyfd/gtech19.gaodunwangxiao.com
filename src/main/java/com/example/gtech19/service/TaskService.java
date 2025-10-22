package com.example.gtech19.service;

import com.example.gtech19.common.PageResponse;
import com.example.gtech19.model.Task;
import com.example.gtech19.service.impl.dto.request.TaskListRequest;
import com.example.gtech19.service.impl.dto.request.TaskUserCreateRequest;
import com.example.gtech19.service.impl.dto.request.TaskUpdateRequest;
import com.example.gtech19.service.impl.dto.response.TaskResponse;

import java.util.Date;
import java.util.List;

/**
 * 任务服务接口
 */
public interface TaskService {

    /**
     * 创建任务
     * 
     * @param request 创建任务请求参数
     * @return 任务ID
     */
    Long userCreateTask(TaskUserCreateRequest request);

    /**
     * 更新任务信息
     * 
     * @param request 更新任务请求参数
     * @return 任务响应数据
     */
    TaskResponse updateTask(TaskUpdateRequest request);

    /**
     * 根据ID查询任务
     * 
     * @param id 任务ID
     * @return 任务响应数据
     */
    TaskResponse getTaskById(Long id);


    /**
     * 根据用户ID查询任务
     *
     * @param userId 用户ID
     * @return 任务列表
     */
    List<Task> getTasksByUserId(String userId);

    /**
     * 根据任务日期查询任务
     * 
     * @param taskDate 任务日期
     * @return 任务列表
     */
    List<Task> getTasksByTaskDate(Date taskDate);

    /**
     * 分页查询任务列表
     * 
     * @param request 任务列表请求参数，包含分页和筛选条件
     * @return 分页响应数据
     */
    PageResponse<Task> getTasksByPage(TaskListRequest request);

    /**
     * 删除任务
     * 
     * @param id 任务ID
     * @return 删除结果
     */
    boolean deleteTaskById(Long id);

    /**
     * 完成任务
     * 
     * @param id 任务ID
     * @return 更新后的任务响应数据
     */
    TaskResponse completeTask(Long id);
}