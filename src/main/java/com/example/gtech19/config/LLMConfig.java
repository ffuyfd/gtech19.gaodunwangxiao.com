package com.example.gtech19.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * 大模型调用相关配置类
 * 配置RestTemplate和WebClient等组件
 */
@Configuration
public class LLMConfig {

    /**
     * 配置RestTemplate用于非流式HTTP请求
     */
    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory factory) {
        return new RestTemplate(factory);
    }

    /**
     * 配置ClientHttpRequestFactory，设置连接超时和读取超时
     */
    @Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        // 设置连接超时时间（毫秒）
        factory.setConnectTimeout(10000);
        // 设置读取超时时间（毫秒）
        factory.setReadTimeout(60000);
        return factory;
    }

    /**
     * 配置WebClient用于流式HTTP请求
     */
    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .codecs(configurer -> {
                    // 配置解码器以支持文本流式传输
                    configurer.defaultCodecs().maxInMemorySize(256 * 1024 * 1024);
                })
                .clientConnector(new reactor.netty.http.client.HttpClient()
                        .responseTimeout(java.time.Duration.ofSeconds(600)) // 响应超时
                        .option(io.netty.channel.ChannelOption.CONNECT_TIMEOUT_MILLIS, 30000) // 连接超时30秒
                )
                .build();
    }
}