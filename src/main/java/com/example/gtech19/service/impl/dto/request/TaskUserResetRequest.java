package com.example.gtech19.service.impl.dto.request;

import lombok.Data;

/**
 * 任务重置请求参数
 */
@Data
public class TaskUserResetRequest {

    /**
     * 用户ID（任务创建者）
     */
    private String userId;
}