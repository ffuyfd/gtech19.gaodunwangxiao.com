package com.example.gtech19.service;

import com.example.gtech19.model.Report;

import java.util.Date;
import java.util.List;

/**
 * 报告服务接口
 */
public interface ReportService {
    
    /**
     * 新增报告
     *
     * @param report 报告信息
     * @return 是否成功
     */
    boolean createReport(Report report);
    
    /**
     * 删除报告
     *
     * @param id 报告ID
     * @return 是否成功
     */
    boolean deleteReport(Long id);
    
    /**
     * 根据用户ID和日期查询报告
     *
     * @param userId 用户ID
     * @param reportDate 报告日期
     * @return 报告信息
     */
    Report getReportByUserIdAndDate(String userId, Date reportDate);
}