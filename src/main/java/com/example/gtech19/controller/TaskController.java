package com.example.gtech19.controller;

import com.example.gtech19.common.PageResponse;
import com.example.gtech19.common.Result;
import com.example.gtech19.model.Task;
import com.example.gtech19.service.TaskService;
import com.example.gtech19.service.helper.UserHelper;
import com.example.gtech19.service.impl.dto.request.TaskListRequest;
import com.example.gtech19.service.impl.dto.request.TaskUpdateRequest;
import com.example.gtech19.service.impl.dto.request.TaskUserCreateRequest;
import com.example.gtech19.service.impl.dto.response.TaskResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 任务控制器
 * 提供RESTful API接口
 */
@RestController
@RequestMapping("/api/v1/task")
@Api(tags = "任务管理接口", description = "提供任务管理相关的API接口")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserHelper userHelper;

    /**
     * 分页查询任务列表
     */
    @PostMapping("/list")
    @ApiOperation(value = "分页查询任务列表", notes = "根据条件分页查询用户的任务信息")
    public Result<PageResponse<Task>> getAllTasks(@RequestBody TaskListRequest request) {
        if (request == null || !userHelper.checkUserLogin(request.getUserId())) {
            return Result.error(500, "请先登录");
        }
        PageResponse<Task> pageResponse = taskService.getTasksByPage(request);
        return Result.success(pageResponse);
    }

    /**
     * 创建任务
     */
    @PostMapping("/create")
    @ApiOperation(value = "创建任务", notes = "创建新的任务信息")
    public Result<Long> userCreateTask(
            @ApiParam(name = "request", value = "创建任务请求参数", required = true)
            @RequestBody TaskUserCreateRequest request) {
        if (request == null) {
            return Result.error(500, "请求参数不能为空");
        }
        Long taskId = taskService.userCreateTask(request);
        if (taskId != null) {
            return Result.success(taskId);
        } else {
            return Result.error(500, "创建任务失败，任务编码可能已存在");
        }
    }

    /**
     * 完成任务
     */
    @PostMapping("/complete/{id}")
    @ApiOperation(value = "完成任务", notes = "标记任务为已完成状态")
    public Result<TaskResponse> completeTask(
            @ApiParam(name = "id", value = "任务ID", required = true, example = "1")
            @PathVariable Long id) {
        TaskResponse response = taskService.completeTask(id);
        if (response != null) {
            return Result.success(response);
        } else {
            return Result.error(404, "任务不存在");
        }
    }

    /**
     * 更新任务信息
     */
    @PostMapping("/update")
    @ApiOperation(value = "更新任务信息", notes = "更新已存在的任务信息")
    public Result<TaskResponse> updateTask(
            @ApiParam(name = "request", value = "更新任务请求参数", required = true)
            @RequestBody TaskUpdateRequest request) {
        if (request == null || request.getId() == null) {
            return Result.error(500, "任务ID不能为空");
        }
        TaskResponse response = taskService.updateTask(request);
        if (response != null) {
            return Result.success(response);
        } else {
            return Result.error(404, "任务不存在");
        }
    }

    /**
     * 根据ID查询任务详情
     */
    @GetMapping("/detail/{id}")
    @ApiOperation(value = "查询任务详情", notes = "根据任务ID查询任务详细信息")
    public Result<TaskResponse> getTaskById(
            @ApiParam(name = "id", value = "任务ID", required = true, example = "1")
            @PathVariable Long id) {
        TaskResponse task = taskService.getTaskById(id);
        if (task != null) {
            return Result.success(task);
        } else {
            return Result.error(404, "任务不存在");
        }
    }


    /**
     * 根据任务日期查询任务
     */
    @GetMapping("/list/date/{taskDate}")
    @ApiOperation(value = "按日期查询任务", notes = "根据任务日期查询任务信息")
    public Result<List<Task>> getTasksByTaskDate(
            @ApiParam(name = "taskDate", value = "任务日期", required = true, example = "2023-12-25")
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date taskDate) {
        List<Task> tasks = taskService.getTasksByTaskDate(taskDate);
        return Result.success(tasks);
    }


    /**
     * 删除任务
     */
    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "删除任务", notes = "根据任务ID删除任务")
    public Result<Boolean> deleteTaskById(
            @ApiParam(name = "id", value = "任务ID", required = true, example = "1")
            @PathVariable Long id) {
        boolean result = taskService.deleteTaskById(id);
        if (result) {
            return Result.success(true);
        } else {
            return Result.error(404, "任务不存在");
        }
    }


}