package com.example.gtech19.service.impl;

import com.example.gtech19.mapper.ReportMapper;
import com.example.gtech19.model.Report;
import com.example.gtech19.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 报告服务实现类
 */
@Service
public class ReportServiceImpl implements ReportService {
    
    @Autowired
    private ReportMapper reportMapper;
    
    @Override
    public boolean createReport(Report report) {
        // 设置创建时间和更新时间
        if (report.getCreateTime() == null) {
            report.setCreateTime(new Date());
        }
        if (report.getUpdateTime() == null) {
            report.setUpdateTime(new Date());
        }
        return reportMapper.insert(report) > 0;
    }
    
    @Override
    public boolean deleteReport(Long id) {
        return reportMapper.deleteById(id, new Date()) > 0;
    }

    @Override
    public Report getReportByUserIdAndDate(String userId, Date reportDate) {
        return reportMapper.selectByUserIdAndDate(userId, reportDate);
    }
}