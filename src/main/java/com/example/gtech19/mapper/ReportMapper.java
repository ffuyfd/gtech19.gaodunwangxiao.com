package com.example.gtech19.mapper;

import com.example.gtech19.model.Report;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 报告Mapper接口
 */
@Mapper
public interface ReportMapper {
    
    /**
     * 新增报告
     */
    int insert(Report report);
    
    /**
     * 逻辑删除报告
     */
    int deleteById(@Param("id") Long id, @Param("updateTime") Date updateTime);

    /**
     * 根据用户ID和日期查询报告
     */
    Report selectByUserIdAndDate(@Param("userId") String userId, @Param("reportDate") Date reportDate);
}