package com.example.gtech19.service;

import com.example.gtech19.service.impl.dto.request.TaskListRequest;
import com.example.gtech19.service.impl.dto.request.TaskUpdateRequest;
import com.example.gtech19.service.impl.dto.request.TaskUserCompleteRequest;
import com.example.gtech19.service.impl.dto.request.TaskUserCreateRequest;
import com.example.gtech19.service.impl.dto.response.TaskResponse;
import reactor.core.publisher.Flux;

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
     * 分页查询任务列表
     *
     * @param request 任务列表请求参数，包含分页和筛选条件
     * @return 分页响应数据
     */
    List<TaskResponse> getTasksByUserId(TaskListRequest request);

    /**
     * 完成任务
     *
     * @param request
     * @return 更新后的任务响应数据
     */
    TaskResponse completeTask(TaskUserCompleteRequest request);
    /**
     * 是否展示报告
     *
     * @param request
     * @return 更新后的任务响应数据
     */
    boolean isShowReport(String userId);



    /**
     * 创建任务AI详情
     * 根据任务ID生成或获取任务AI详情，流式输出
     *
     * @param taskId 任务ID
     * @return 流式输出的任务AI详情内容
     */
    Flux<String> createTaskAiDetail(Long taskId);

     /**
      * 创建任务报告
      * 根据任务ID生成或获取任务报告，流式输出
      *
      * @param userId 用户id
      * @return 流式输出的任务报告内容
      */
    Flux<String> createReport(String userId);

}