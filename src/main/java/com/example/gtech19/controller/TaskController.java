package com.example.gtech19.controller;

import com.example.gtech19.common.Result;
import com.example.gtech19.service.TaskService;
import com.example.gtech19.service.helper.UserHelper;
import com.example.gtech19.service.impl.dto.request.TaskListRequest;
import com.example.gtech19.service.impl.dto.request.TaskUserCompleteRequest;
import com.example.gtech19.service.impl.dto.request.TaskUserCreateRequest;
import com.example.gtech19.service.impl.dto.response.TaskResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public Result<List<TaskResponse>> getAllTasks(@RequestBody TaskListRequest request) {
        if (request == null || !userHelper.checkUserLogin(request.getUserId())) {
            return Result.error(500, "请先登录");
        }
        List<TaskResponse> pageResponse = taskService.getTasksByUserId(request);
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
        if (request == null || !userHelper.checkUserLogin(request.getUserId())) {
            return Result.error(500, "请先登录");
        }
        Long taskId = taskService.userCreateTask(request);
        if (taskId != null) {
            return Result.success(taskId);
        } else {
            return Result.error(500, "创建任务失败");
        }
    }

    /**
     * 完成任务
     */
    @PostMapping("/complete")
    @ApiOperation(value = "完成任务", notes = "标记任务为已完成状态")
    public Result<TaskResponse> completeTask(
            @ApiParam(name = "request", value = "更新任务状态请求参数", required = true)
            @RequestBody TaskUserCompleteRequest request) {
        if (!userHelper.checkUserLogin(request.getUserId())) {
            return Result.error(500, "请先登录");
        }
        if (request.getTaskId() == null || request.getTaskId() == 0) {
            return Result.error(500, "参数错误");
        }
        TaskResponse response = taskService.completeTask(request);
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
     * 完成任务
     */
    @PostMapping("/reset-init")
    @ApiOperation(value = "重置初始化任务", notes = "重置初始化任务")
    public Result<Boolean> resetInitTask(
            @ApiParam(name = "request", value = "更新任务状态请求参数", required = true)
            @RequestBody TaskUserCompleteRequest request) {
        if (!userHelper.checkUserLogin(request.getUserId())) {
            return Result.error(500, "请先登录");
        }
        Boolean response = taskService.resetInitTask(request.getUserId());
        if (response != null) {
            return Result.success(response);
        } else {
            return Result.error(404, "任务不存在");
        }
    }


}