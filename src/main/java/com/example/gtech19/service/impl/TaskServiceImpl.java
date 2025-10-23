package com.example.gtech19.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.example.gtech19.config.enums.TaskLibraryConfigEnum;
import com.example.gtech19.config.enums.TaskSourceEnum;
import com.example.gtech19.config.enums.TaskStatusEnum;
import com.example.gtech19.config.enums.TaskTypeEnum;
import com.example.gtech19.mapper.TaskMapper;
import com.example.gtech19.model.Task;
import com.example.gtech19.service.TaskService;
import com.example.gtech19.service.helper.TaskHelper;
import com.example.gtech19.service.impl.dto.request.TaskListRequest;
import com.example.gtech19.service.impl.dto.request.TaskUpdateRequest;
import com.example.gtech19.service.impl.dto.request.TaskUserCompleteRequest;
import com.example.gtech19.service.impl.dto.request.TaskUserCreateRequest;
import com.example.gtech19.service.impl.dto.response.TaskResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 任务服务实现类
 */
@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private TaskHelper taskHelper;

    @Override
    public Long userCreateTask(TaskUserCreateRequest request) {

        Task task = new Task();
        BeanUtils.copyProperties(request, task);
        task.setTaskType(TaskTypeEnum.OTHER.getCode());
        task.setTaskSource(TaskSourceEnum.USER_DEFINED.getCode());

        // 设置默认值
        if (task.getTaskPoints() == null) {
            task.setTaskPoints(0);
        }
        task.setTaskStatus(0); // 初始状态为未完成
        task.setTaskDate(new Date());
        task.setCreateTime(new Date());
        task.setUpdateTime(new Date());
        task.setTaskDate(DateUtil.parse(request.getTaskDate()));
        task.setIsDeleted(0);
        task.setTaskPoints(TaskTypeEnum.OTHER.getPoints());
        // 新增任务
        taskMapper.insert(task);
        return task.getId();
    }

    @Override
    public TaskResponse updateTask(TaskUpdateRequest request) {
        if (request == null || request.getId() == null) {
            return null;
        }

        Task task = taskMapper.selectById(request.getId());
        if (task == null) {
            return null;
        }

        // 复制更新字段
        BeanUtils.copyProperties(request, task, "id", "createTime", "updateTime");
        task.setUpdateTime(new Date());

        // 更新任务
        taskMapper.update(task);
        return getTaskById(request.getId());
    }

    @Override
    public TaskResponse getTaskById(Long id) {
        Task task = taskMapper.selectById(id);
        if (task == null) {
            return null;
        }
        return convertToResponse(task);
    }

    @Override
    public List<TaskResponse> getTasksByUserId(TaskListRequest request) {
        // 执行查询
        List<Task> tasks = taskMapper.selectByPage(request);

        // 转换为TaskResponse列表
        return tasks.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public TaskResponse completeTask(TaskUserCompleteRequest request) {
        Task task = taskMapper.selectById(request.getTaskId());
        if (task == null) {
            return null;
        }
        task.setTaskStatus(1); // 标记为已完成
        task.setFinishTime(new Date());
        task.setUpdateTime(new Date());
        taskMapper.update(task);
        return getTaskById(request.getTaskId());
    }


    /**
     * 转换Task对象为TaskResponse对象
     */
    private TaskResponse convertToResponse(Task task) {
        TaskResponse response = new TaskResponse();
        BeanUtils.copyProperties(task, response);
        response.setTaskSourceName(TaskSourceEnum.getByCode(response.getTaskSource()).getName());
        response.setTaskTypeName(TaskTypeEnum.getByCode(response.getTaskType()).getName());
        response.setTaskStatusName(TaskStatusEnum.getByCode(response.getTaskStatus()).getName());
        response.setTaskDate(DateUtil.formatDate(task.getTaskDate()));
        if (StrUtil.isNotBlank(task.getTaskCode())) {
            TaskLibraryConfigEnum taskLibraryConfigEnum = TaskLibraryConfigEnum.getByTaskId(Integer.parseInt(task.getTaskCode()));
            if (taskLibraryConfigEnum != null) {
                if ("AI".equals(taskLibraryConfigEnum.getTaskType())) {
                    response.setImageUrl(taskLibraryConfigEnum.getJumpUrl());
                }
                response.setJumpType(taskLibraryConfigEnum.getTaskType());
            }
        }
        return response;
    }

    @Override
    public Boolean resetInitTask(String userId) {
        // 删除用户的初始化任务
        int deleted = taskMapper.deleteUserTask(userId);
        taskHelper.firstInitTask(userId);

        return deleted > 0;
    }

}