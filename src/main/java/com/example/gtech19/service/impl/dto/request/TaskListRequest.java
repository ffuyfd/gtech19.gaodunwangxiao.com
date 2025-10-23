package com.example.gtech19.service.impl.dto.request;

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
     * 开始日期
     */
    private String startDay;

    /**
     * 结束日期
     */
    private String endDay;

}