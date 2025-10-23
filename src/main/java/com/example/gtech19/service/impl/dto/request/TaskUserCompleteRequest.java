package com.example.gtech19.service.impl.dto.request;

import lombok.Data;

/**
 * 任务完成请求参数
 */
@Data
public class TaskUserCompleteRequest {

    /**
     * 用户ID（任务创建者）
     */
    private String userId;
    
    /**
     * 任务id
     */
    private Long taskId;

}