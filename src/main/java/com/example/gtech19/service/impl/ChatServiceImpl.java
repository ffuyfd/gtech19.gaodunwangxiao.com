package com.example.gtech19.service.impl;

import com.example.gtech19.common.Result;
import com.example.gtech19.helper.LLMService;
import com.example.gtech19.service.ChatService;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 聊天服务实现类
 * 实现不同业务场景的文本生成方法
 */
@Service
@Slf4j
public class ChatServiceImpl implements ChatService {

    @Resource
    private LLMService llmService;

    private final Gson gson = new Gson();

    /**
     * 构建messages参数
     * @param userContent 用户内容
     * @param systemContent 系统指令
     * @return messages列表
     */
    private List<Map<String, String>> buildMessages(String userContent, String systemContent) {
        List<Map<String, String>> messages = new ArrayList<>();
        
        // 添加系统指令
        if (systemContent != null && !systemContent.isEmpty()) {
            Map<String, String> systemMessage = new HashMap<>();
            systemMessage.put("role", "system");
            systemMessage.put("content", systemContent);
            messages.add(systemMessage);
        }
        
        // 添加用户消息
        Map<String, String> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", userContent);
        messages.add(userMessage);
        
        return messages;
    }

    @Override
    public Result<Map<String, Object>> generalChat(Map<String, Object> request) {
        try {
            log.info("接收通用对话请求: {}", request);
            
            // 验证必要参数
            if (!request.containsKey("content")) {
                return Result.error(400, "缺少必要参数: content");
            }
            
            String userContent = (String) request.get("content");
            String systemContent = (String) request.get("system");
            
            // 构建请求参数
            Map<String, Object> llmRequest = new HashMap<>(request);
            llmRequest.put("messages", buildMessages(userContent, systemContent));
            
            // 调用底层服务
            long now = System.currentTimeMillis();
            Map<String, Object> response = llmService.generateText(llmRequest);
            long end = System.currentTimeMillis();
            log.info("通用对话请求完成，响应结果: {}, 请求耗时: {}ms", response, end - now);
            return Result.success(response);
        } catch (Exception e) {
            log.error("通用对话失败", e);
            return Result.error(500, "通用对话失败：" + e.getMessage());
        }
    }

    @Override
    public Flux<String> generalChatStream(Map<String, Object> request) {
        try {
            log.info("接收流式通用对话请求: {}", request);
            
            // 验证必要参数
            if (!request.containsKey("content")) {
                return Flux.just("data: " + gson.toJson(Result.error(400, "缺少必要参数: content")) + "\n\n")
                        .concatWith(Flux.just("event: done\ndata: [DONE]\n\n"));
            }
            
            String userContent = (String) request.get("content");
            String systemContent = "你是一个有帮助的AI助手";
            
            // 构建请求参数
            Map<String, Object> llmRequest = new HashMap<>(request);
            llmRequest.put("messages", buildMessages(userContent, systemContent));
            
            // 调用底层服务
            return llmService.generateTextStream(llmRequest);
        } catch (Exception e) {
            log.error("流式通用对话初始化失败", e);
            return Flux.just("data: " + gson.toJson(Result.error(500, "流式通用对话初始化失败：" + e.getMessage())) + "\n\n")
                    .concatWith(Flux.just("event: done\ndata: [DONE]\n\n"));
        }
    }

    @Override
    public Result<Map<String, Object>> customerServiceChat(Map<String, Object> request) {
        try {
            log.info("接收客服问答请求: {}", request);
            
            // 验证必要参数
            if (!request.containsKey("content")) {
                return Result.error(400, "缺少必要参数: content");
            }
            
            String userContent = (String) request.get("content");
            String systemContent = "你是一个专业的客服代表，负责解答用户问题，请使用友好、专业的语气进行回复。";
            
            // 构建请求参数
            Map<String, Object> llmRequest = new HashMap<>(request);
            llmRequest.put("messages", buildMessages(userContent, systemContent));
            
            // 调用底层服务
            Map<String, Object> response = llmService.generateText(llmRequest);
            
            log.info("客服问答请求完成，响应结果: {}", response);
            return Result.success(response);
        } catch (Exception e) {
            log.error("客服问答失败", e);
            return Result.error(500, "客服问答失败：" + e.getMessage());
        }
    }

    @Override
    public Flux<String> customerServiceChatStream(Map<String, Object> request) {
        try {
            log.info("接收流式客服问答请求: {}", request);
            
            // 验证必要参数
            if (!request.containsKey("content")) {
                return Flux.just("data: " + gson.toJson(Result.error(400, "缺少必要参数: content")) + "\n\n")
                        .concatWith(Flux.just("event: done\ndata: [DONE]\n\n"));
            }
            
            String userContent = (String) request.get("content");
            String systemContent = "你是一个专业的客服代表，负责解答用户问题，请使用友好、专业的语气进行回复。";
            
            // 构建请求参数
            Map<String, Object> llmRequest = new HashMap<>(request);
            llmRequest.put("messages", buildMessages(userContent, systemContent));
            
            // 调用底层服务
            return llmService.generateTextStream(llmRequest);
        } catch (Exception e) {
            log.error("流式客服问答初始化失败", e);
            return Flux.just("data: " + gson.toJson(Result.error(500, "流式客服问答初始化失败：" + e.getMessage())) + "\n\n")
                    .concatWith(Flux.just("event: done\ndata: [DONE]\n\n"));
        }
    }

    @Override
    public Result<Map<String, Object>> contentCreation(Map<String, Object> request) {
        try {
            log.info("接收内容创作请求: {}", request);
            
            // 验证必要参数
            if (!request.containsKey("content")) {
                return Result.error(400, "缺少必要参数: content");
            }
            
            String userContent = (String) request.get("content");
            String systemContent = "你是一个专业的内容创作者，能够根据用户需求生成高质量的文章、故事、文案等内容。";
            
            // 构建请求参数
            Map<String, Object> llmRequest = new HashMap<>(request);
            llmRequest.put("messages", buildMessages(userContent, systemContent));
            
            // 调用底层服务
            Map<String, Object> response = llmService.generateText(llmRequest);
            
            log.info("内容创作请求完成，响应结果: {}", response);
            return Result.success(response);
        } catch (Exception e) {
            log.error("内容创作失败", e);
            return Result.error(500, "内容创作失败：" + e.getMessage());
        }
    }

    @Override
    public Flux<String> contentCreationStream(Map<String, Object> request) {
        try {
            log.info("接收流式内容创作请求: {}", request);
            
            // 验证必要参数
            if (!request.containsKey("content")) {
                return Flux.just("data: " + gson.toJson(Result.error(400, "缺少必要参数: content")) + "\n\n")
                        .concatWith(Flux.just("event: done\ndata: [DONE]\n\n"));
            }
            
            String userContent = (String) request.get("content");
            String systemContent = "你是一个专业的内容创作者，能够根据用户需求生成高质量的文章、故事、文案等内容。";
            
            // 构建请求参数
            Map<String, Object> llmRequest = new HashMap<>(request);
            llmRequest.put("messages", buildMessages(userContent, systemContent));
            
            // 调用底层服务
            return llmService.generateTextStream(llmRequest);
        } catch (Exception e) {
            log.error("流式内容创作初始化失败", e);
            return Flux.just("data: " + gson.toJson(Result.error(500, "流式内容创作初始化失败：" + e.getMessage())) + "\n\n")
                    .concatWith(Flux.just("event: done\ndata: [DONE]\n\n"));
        }
    }
}