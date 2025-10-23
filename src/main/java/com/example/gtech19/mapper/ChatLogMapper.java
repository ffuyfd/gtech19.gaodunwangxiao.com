package com.example.gtech19.mapper;

import com.example.gtech19.model.ChatLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 日志表Mapper接口
 */
@Mapper
public interface ChatLogMapper {
    

    /**
     * 新增日志
     */
    int insert(ChatLog chatLog);
    
    /**
     * 更新日志
     */
    int update(ChatLog chatLog);
    
    /**
     * 根据ID删除日志（逻辑删除）
     */
    int deleteById(@Param("id") Long id);

}