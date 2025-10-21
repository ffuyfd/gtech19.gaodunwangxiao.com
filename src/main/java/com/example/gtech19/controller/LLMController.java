package com.example.gtech19.controller;

import com.example.gtech19.common.Result;
import com.example.gtech19.service.ChatService;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 大模型调用控制器
 * 提供文本推理相关的API接口
 */
@RestController
@RequestMapping("/api/v1/llm")
@Slf4j
public class LLMController {

    @Resource
    private ChatService chatService;

    private final Gson gson = new Gson();

    /**
     * 通用对话接口 - 非流式
     * @param request 请求体，包含content等参数
     * @return 对话结果
     */
    @PostMapping("/general-chat")
    public Result<Map<String, Object>> generalChat(@RequestBody Map<String, Object> request) {
        log.info("接收通用对话请求: {}", request);
        return chatService.generalChat(request);
    }

    /**
     * 通用对话接口 - 流式
     * @param request 请求体，包含content等参数
     * @return 流式对话结果
     */
    @PostMapping(value = "/general-chat-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> generalChatStream(@RequestBody Map<String, Object> request) {
        log.info("接收流式通用对话请求: {}", request);
        return chatService.generalChatStream(request);
    }

    /**
     * 客服问答接口 - 非流式
     * @param request 请求体，包含content等参数
     * @return 问答结果
     */
    @PostMapping("/customer-service-chat")
    public Result<Map<String, Object>> customerServiceChat(@RequestBody Map<String, Object> request) {
        log.info("接收客服问答请求: {}", request);
        return chatService.customerServiceChat(request);
    }

    /**
     * 客服问答接口 - 流式
     * @param request 请求体，包含content等参数
     * @return 流式问答结果
     */
    @PostMapping(value = "/customer-service-chat-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> customerServiceChatStream(@RequestBody Map<String, Object> request) {
        log.info("接收流式客服问答请求: {}", request);
        return chatService.customerServiceChatStream(request);
    }

    /**
     * 内容创作接口 - 非流式
     * @param request 请求体，包含content等参数
     * @return 创作结果
     */
    @PostMapping("/content-creation")
    public Result<Map<String, Object>> contentCreation(@RequestBody Map<String, Object> request) {
        log.info("接收内容创作请求: {}", request);
        return chatService.contentCreation(request);
    }

    /**
     * 内容创作接口 - 流式
     * @param request 请求体，包含content等参数
     * @return 流式创作结果
     */
    @PostMapping(value = "/content-creation-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> contentCreationStream(@RequestBody Map<String, Object> request) {
        log.info("接收流式内容创作请求: {}", request);
        return chatService.contentCreationStream(request);
    }
}