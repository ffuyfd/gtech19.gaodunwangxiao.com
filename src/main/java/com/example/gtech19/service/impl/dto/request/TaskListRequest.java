package com.example.gtech19.service.impl.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 任务列表请求参数
 */
@Data
public class TaskListRequest {
    /**
     * 用户ID
     */
    private String userId;
    
    /**
     * 当前页码，默认1
     */
    @ApiModelProperty(value = "当前页码", example = "1", required = false)
    private Integer pageNum = 1;
    
    /**
     * 每页数量，默认10
     */
    @ApiModelProperty(value = "每页数量", example = "10", required = false)
    private Integer pageSize = 10;
    
    /**
     * 任务状态：0-未完成，1-已完成
     */
    @ApiModelProperty(value = "任务状态", example = "0", required = false)
    private Integer taskStatus;
    
    /**
     * 任务类型：1-直播，2-课程，3-其它
     */
    @ApiModelProperty(value = "任务类型", example = "1", required = false)
    private Integer taskType;
    
    /**
     * 任务来源：1-任务库，2-AI，3-用户自定义
     */
    @ApiModelProperty(value = "任务来源", example = "1", required = false)
    private Integer taskSource;
}