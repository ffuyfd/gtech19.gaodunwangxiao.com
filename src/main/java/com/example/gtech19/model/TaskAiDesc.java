package com.example.gtech19.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 任务AI详情表实体类
 */
@Data
public class TaskAiDesc implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 主键ID
     */
    private Long id;
    
    /**
     * 任务id
     */
    private Long taskId;
    
    /**
     * 任务名称
     */
    private String taskName;
    
    /**
     * 任务命令
     */
    private String taskCommand;
    
    /**
     * 任务描述
     */
    private String taskAiDesc;
    
    /**
     * 创建时间
     */
    private Date createTime;
    
    /**
     * 更新时间（自动更新）
     */
    private Date updateTime;
    
    /**
     * 是否删除：0-否 1-已删除
     */
    private Integer isDeleted;
}