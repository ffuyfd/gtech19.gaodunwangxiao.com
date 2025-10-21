package com.example.gtech19.controller;

import com.example.gtech19.common.Result;
import com.example.gtech19.service.ChatService;
import com.google.gson.Gson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
@Api(tags = "大模型调用接口", description = "提供大模型文本生成相关的API接口")
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
    @ApiOperation(value = "通用对话接口", notes = "提供通用的非流式对话功能，返回完整的对话结果")
    public Result<Map<String, Object>> generalChat(
            @ApiParam(name = "request", value = "对话请求参数", required = true, example = "{\"content\":\"你好\",\"temperature\":0.7}")
            @RequestBody Map<String, Object> request) {
        log.info("接收通用对话请求: {}", request);
        return chatService.generalChat(request);
    }

    /**
     * 通用对话接口 - 流式
     * @param request 请求体，包含content等参数
     * @return 流式对话结果
     */
    @PostMapping(value = "/general-chat-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ApiOperation(value = "通用对话流式接口", notes = "提供通用的流式对话功能，实时返回对话结果")
    public Flux<String> generalChatStream(
            @ApiParam(name = "request", value = "对话请求参数", required = true, example = "{\"content\":\"你好\",\"temperature\":0.7}")
            @RequestBody Map<String, Object> request) {
        log.info("接收流式通用对话请求: {}", request);
        return chatService.generalChatStream(request);
    }

    /**
     * 客服问答接口 - 非流式
     * @param request 请求体，包含content等参数
     * @return 问答结果
     */
    @PostMapping("/customer-service-chat")
    @ApiOperation(value = "客服问答接口", notes = "提供专业的客服问答功能，返回完整的问答结果")
    public Result<Map<String, Object>> customerServiceChat(
            @ApiParam(name = "request", value = "问答请求参数", required = true, example = "{\"content\":\"如何使用系统？\",\"temperature\":0.7}")
            @RequestBody Map<String, Object> request) {
        log.info("接收客服问答请求: {}", request);
        return chatService.customerServiceChat(request);
    }

    /**
     * 客服问答接口 - 流式
     * @param request 请求体，包含content等参数
     * @return 流式问答结果
     */
    @PostMapping(value = "/customer-service-chat-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ApiOperation(value = "客服问答流式接口", notes = "提供专业的客服问答流式功能，实时返回问答结果")
    public Flux<String> customerServiceChatStream(
            @ApiParam(name = "request", value = "问答请求参数", required = true, example = "{\"content\":\"如何使用系统？\",\"temperature\":0.7}")
            @RequestBody Map<String, Object> request) {
        log.info("接收流式客服问答请求: {}", request);
        return chatService.customerServiceChatStream(request);
    }

    /**
     * 内容创作接口 - 非流式
     * @param request 请求体，包含content等参数
     * @return 创作结果
     */
    @PostMapping("/content-creation")
    @ApiOperation(value = "内容创作接口", notes = "提供内容创作功能，返回完整的创作结果")
    public Result<Map<String, Object>> contentCreation(
            @ApiParam(name = "request", value = "创作请求参数", required = true, example = "{\"content\":\"写一篇关于春天的短文\",\"temperature\":0.7}")
            @RequestBody Map<String, Object> request) {
        log.info("接收内容创作请求: {}", request);
        return chatService.contentCreation(request);
    }

    /**
     * 内容创作接口 - 流式
     * @param request 请求体，包含content等参数
     * @return 流式创作结果
     */
    @PostMapping(value = "/content-creation-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ApiOperation(value = "内容创作流式接口", notes = "提供内容创作流式功能，实时返回创作结果")
    public Flux<String> contentCreationStream(
            @ApiParam(name = "request", value = "创作请求参数", required = true, example = "{\"content\":\"写一篇关于春天的短文\",\"temperature\":0.7}")
            @RequestBody Map<String, Object> request) {
        log.info("接收流式内容创作请求: {}", request);
        return chatService.contentCreationStream(request);
    }
}