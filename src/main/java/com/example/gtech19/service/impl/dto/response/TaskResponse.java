package com.example.gtech19.service.impl.dto.response;

import lombok.Data;

import java.util.Date;

/**
 * 任务响应数据
 */
@Data
public class TaskResponse {
    /**
     * 主键ID
     */
    private Long id;

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
     * 任务类型名称（根据类型值展示对应的名称）
     */
    private String taskTypeName;

    /**
     * 任务日期（任务计划执行的日期）
     */
    private String taskDate;

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
     * 任务来源名称
     */
    private String taskSourceName;

    /**
     * 任务状态：0-未完成，1-已完成
     */
    private Integer taskStatus;

    /**
     * 任务状态名称（根据状态值展示对应的名称）
     */
    private String taskStatusName;

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
}