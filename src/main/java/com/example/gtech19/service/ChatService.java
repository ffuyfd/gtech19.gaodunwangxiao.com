package com.example.gtech19.service;

import com.example.gtech19.common.Result;
import reactor.core.publisher.Flux;

import java.util.Map;

/**
 * 聊天服务接口
 * 定义不同业务场景的文本生成方法
 */
public interface ChatService {

    /**
     * 通用对话接口
     * @param request 请求参数，包含用户消息等
     * @return 对话结果
     */
    Result<Map<String, Object>> generalChat(Map<String, Object> request);

    /**
     * 流式通用对话接口
     * @param request 请求参数，包含用户消息等
     * @return 流式对话结果
     */
    Flux<String> generalChatStream(Map<String, Object> request);

    /**
     * 客服问答接口
     * @param request 请求参数，包含用户问题等
     * @return 问答结果
     */
    Result<Map<String, Object>> customerServiceChat(Map<String, Object> request);

    /**
     * 流式客服问答接口
     * @param request 请求参数，包含用户问题等
     * @return 流式问答结果
     */
    Flux<String> customerServiceChatStream(Map<String, Object> request);

    /**
     * 内容创作接口
     * @param request 请求参数，包含创作需求等
     * @return 创作结果
     */
    Result<Map<String, Object>> contentCreation(Map<String, Object> request);

    /**
     * 流式内容创作接口
     * @param request 请求参数，包含创作需求等
     * @return 流式创作结果
     */
    Flux<String> contentCreationStream(Map<String, Object> request);
}