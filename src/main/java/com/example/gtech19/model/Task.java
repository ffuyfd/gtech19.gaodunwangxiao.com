package com.example.gtech19.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 任务实体类
 */
@Data
public class Task implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 用户ID（关联用户表）
     */
    private String userId;
    
    /**
     * 任务编码
     */
    private String taskCode;
    
    /**
     * 任务名称
     */
    private String taskName;
    
    /**
     * 任务描述（详细说明）
     */
    private String taskDesc;
    
    /**
     * 任务类型：1-直播，2-课程，3-其它（控制图片显示逻辑）
     */
    private Integer taskType;
    
    /**
     * 任务日期（任务计划执行的日期）
     */
    private Date taskDate;
    
    /**
     * 图片URL（根据任务类型展示对应的图片）
     */
    private String imageUrl;
    
    /**
     * 任务积分（完成任务可获得的积分）
     */
    private Integer taskPoints;
    
    /**
     * 任务来源：1-任务库，2-AI，3-用户自定义（控制操作按钮显示，仅1有按钮）
     */
    private Integer taskSource;
    
    /**
     * 任务状态：0-未完成，1-已完成
     */
    private Integer taskStatus;
    
    /**
     * 完成时间（任务状态变为1时更新）
     */
    private Date finishTime;
    
    /**
     * 创建时间
     */
    private Date createTime;
    
    /**
     * 更新时间（自动更新）
     */
    private Date updateTime;

     /**
     * 删除状态：0-未删除，1-已删除
     */
    private Integer isDeleted;
}