package com.example.gtech19.helper;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * 大模型服务类
 * 封装火山引擎大模型API调用的核心逻辑
 */
@Service
@Slf4j
public class LLMService {

    @Value("${volcano.api.token}")
    private String apiToken;

    @Value("${volcano.api.url}")
    private String apiUrl;

    private final WebClient webClient;
    private final Gson gson;

    public LLMService(WebClient webClient, Gson gson) {
        this.webClient = webClient;
        this.gson = gson;
    }

    /**
     * 文本生成 - 非流式
     *
     * @param request 请求参数
     * @return 文本生成结果
     */
    public Map<String, Object> generateText(Map<String, Object> request) {
        try {
            log.debug("准备文本生成请求");

            // 构建请求参数
            Map<String, Object> params = buildRequestParams(request, false);

            // 生成认证头
            String authToken = generateAuthToken();

            // 发送请求，使用简化的处理方式
            return webClient.post()
                    .uri(apiUrl)
                    .header("Authorization", authToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(params))
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                            response -> response.bodyToMono(String.class)
                                    .flatMap(errorBody -> {
                                        log.error("API请求失败，状态码: {}, 错误信息: {}", response.statusCode(), errorBody);
                                        return Mono.error(new RuntimeException("火山引擎API请求失败: " + response.statusCode() + ", " + errorBody));
                                    }))
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .timeout(java.time.Duration.ofSeconds(600)) // 设置600秒超时
                    .block();
        } catch (Exception e) {
            log.error("文本生成失败", e);
            throw new RuntimeException("文本生成失败：" + e.getMessage(), e);
        }
    }


    /**
     * 文本生成 - 流式
     *
     * @param request 请求参数
     * @return 流式文本生成结果
     */
    public Flux<String> generateTextStream(Map<String, Object> request) {
        try {
            log.info("准备流式文本生成请求: {}", request);

            // 构建请求参数
            Map<String, Object> params = buildRequestParams(request, true);
            log.debug("构建的流式请求参数: {}", params);

            // 生成认证头
            String authToken = generateAuthToken();
            log.info("生成认证Token成功");
            log.debug("API URL: {}", apiUrl);

            // 发送请求，使用exchange()代替retrieve()以更好地处理重定向和内容类型
            log.info("发送流式文本生成请求到火山引擎API");
            return webClient.post()
                    .uri(apiUrl)
                    .header("Authorization", authToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(params))
                    .exchange()
                    .flatMapMany(this::handleStreamResponse);
        } catch (Exception e) {
            log.error("流式文本生成初始化失败", e);
            return Flux.just("data: {\"error\":\"" + e.getMessage() + "\"}\n\n")
                    .concatWith(Flux.just("event: done\ndata: [DONE]\n\n"));
        }
    }

    /**
     * 处理流式HTTP响应，包括重定向和不同内容类型
     */
    private Flux<String> handleStreamResponse(ClientResponse response) {
        log.info("接收到流式响应，状态码: {}", response.statusCode());

        // 处理重定向
        if (response.statusCode().is3xxRedirection()) {
            String location = response.headers().asHttpHeaders().getFirst("Location");
            log.info("检测到流式响应重定向，目标地址: {}", location);

            // 重新发起请求到重定向地址
            return webClient.post()
                    .uri(location)
                    .header("Authorization", generateAuthToken())
                    .contentType(MediaType.APPLICATION_JSON)
                    .exchange()
                    .flatMapMany(this::handleNonRedirectedStreamResponse);
        }

        // 处理非重定向响应
        return handleNonRedirectedStreamResponse(response);
    }

    /**
     * 处理非重定向的流式HTTP响应
     */
    private Flux<String> handleNonRedirectedStreamResponse(ClientResponse response) {
        // 检查响应状态是否成功
        if (response.statusCode().is2xxSuccessful()) {
            // 检查内容类型
            String contentType = response.headers().contentType().map(MediaType::toString).orElse("");
            log.debug("流式响应内容类型: {}", contentType);

            // 处理流式数据，转换为SSE格式
            return response.bodyToFlux(String.class)
                    .doOnNext(chunk -> log.debug("接收到流数据块: {}", chunk.length() > 100 ? chunk.substring(0, 100) + "..." : chunk))
                    .map(chunk -> "data: " + chunk + "\n\n")
                    .doFinally(signal -> log.info("流式文本生成请求完成"))
                    .onErrorResume(error -> {
                        log.error("流式文本生成失败", error);
                        return Flux.just("data: {\"error\":\"" + error.getMessage() + "\"}\n\n");
                    })
                    .concatWith(Flux.just("event: done\ndata: [DONE]\n\n"));
        } else {
            // 处理错误响应
            return response.bodyToMono(String.class)
                    .defaultIfEmpty("No error body")
                    .flatMapMany(errorBody -> {
                        log.error("流式请求失败，状态码: {}, 错误信息: {}", response.statusCode(), errorBody);
                        return Flux.just("data: {\"error\":\"" + errorBody + "\"}\n\n")
                                .concatWith(Flux.just("event: done\ndata: [DONE]\n\n"));
                    });
        }
    }

    /**
     * 构建请求参数
     */
    private Map<String, Object> buildRequestParams(Map<String, Object> request, boolean isStream) {
        Map<String, Object> params = new HashMap<>();
        params.put("model", request.getOrDefault("model", "ep-20251015101857-wc8xz")); // 使用用户提供的模型ID作为默认值
        params.put("messages", request.get("messages"));
       /* params.put("temperature", request.getOrDefault("temperature", 0.7));
        params.put("top_p", request.getOrDefault("top_p", 0.95));
        params.put("max_tokens", request.getOrDefault("max_tokens", 2048));*/
        params.put("stream", isStream);
        return params;
    }

    /**
     * 生成认证Token
     * 根据火山引擎API文档实现认证逻辑
     */
    private String generateAuthToken() {
        try {
            // 使用Bearer Token认证
            return "Bearer " + apiToken;
        } catch (Exception e) {
            log.error("生成认证Token失败", e);
            throw new RuntimeException("生成认证Token失败", e);
        }
    }
}