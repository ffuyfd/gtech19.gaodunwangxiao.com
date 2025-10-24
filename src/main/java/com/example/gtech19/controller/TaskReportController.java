package com.example.gtech19.controller;

import com.example.gtech19.common.Result;
import com.example.gtech19.service.TaskService;
import com.example.gtech19.service.helper.UserHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

/**
 * 任务报告控制器
 * 提供RESTful API接口
 */
@RestController
@RequestMapping("/api/v1/task-report")
@Api(tags = "任务报告管理接口", description = "提供任务报告管理相关的API接口")
public class TaskReportController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserHelper userHelper;

    /**
     * 分页查询任务列表
     */
    @GetMapping("/is-show")
    @ApiOperation(value = "是否展示报告", notes = "是否展示报告")
    public Result<Boolean> isShow(@Param("userId") String userId) {
        if (!userHelper.checkUserLogin(userId)) {
            return Result.error(500, "请先登录");
        }
        boolean pageResponse = taskService.isShowReport(userId);
        return Result.success(pageResponse);
    }

    /**
     * 创建任务AI详情
     * 流式输出任务AI详情内容
     */
    @GetMapping(value = "/create-report", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ApiOperation(value = "创建任务AI详情", notes = "根据任务ID生成或获取任务AI详情，流式输出")
    public Flux<String> createReport(
            @ApiParam(name = "userId", value = "用户ID", required = true, example = "1")
            @RequestParam String userId) {
        return taskService.createReport(userId);
    }

}