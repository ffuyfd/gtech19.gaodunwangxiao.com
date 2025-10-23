package com.example.gtech19.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 日志表实体类
 */
@Data
public class ChatLog implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 主键ID
     */
    private Long id;
    
    /**
     * 用户id
     */
    private String userId;
    
    /**
     * 业务类型
     */
    private String bizType;
    
    /**
     * chat入参
     */
    private String chatRequest;
    
    /**
     * chat出参
     */
    private String chatResponse;
    
    /**
     * 耗时ms
     */
    private Long costMs;
    
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