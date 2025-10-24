package com.example.gtech19.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 报告实体类
 */
@Data
public class Report implements Serializable {
    
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
     * 报告内容
     */
    private String reportText;
    
    /**
     * 报告日期
     */
    private Date reportDay;
    
    /**
     * 创建时间
     */
    private Date createTime;
    
    /**
     * 更新时间
     */
    private Date updateTime;
    
    /**
     * 是否删除：0-否 1-已删除
     */
    private Integer isDeleted;
}