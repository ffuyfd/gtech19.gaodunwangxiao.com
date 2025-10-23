package com.example.gtech19.service.impl.dto.request;

import lombok.Data;

import java.util.Date;

/**
 * 创建任务请求参数
 */
@Data
public class TaskUserCreateRequest {

    /**
     * 用户ID（任务创建者）
     */
    private String userId;

    
    /**
     * 任务名称
     */
    private String taskName;
    
    /**
     * 任务描述（详细说明）
     */
    private String taskDesc;

    /**
     * 任务日期
     */
    private String taskDate;


}