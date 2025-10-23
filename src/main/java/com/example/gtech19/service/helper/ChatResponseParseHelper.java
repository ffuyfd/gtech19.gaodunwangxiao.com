package com.example.gtech19.service.helper;

import com.example.gtech19.service.impl.dto.response.TaskInitResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class ChatResponseParseHelper {
    @Autowired
    private Gson gson;

    // 解析为 Map 类型的列表
    public String extractResultContent(Map<String, Object> data) {
        try {
            // 1. 从 Result 中获取顶层数据 Map（根据你的 Result 类实际方法调整，比如 getResult()）
            if (data == null) {
                return "";
            }

            // 2. 提取 choices 列表（第一层嵌套）
            List<Map<String, Object>> choices = (List<Map<String, Object>>) data.get("choices");
            if (choices == null || choices.isEmpty()) {
                return "";
            }

            // 3. 提取第一个 choice 中的 message 字典（第二层嵌套）
            Map<String, Object> firstChoice = choices.get(0);
            Map<String, Object> message = (Map<String, Object>) firstChoice.get("message");
            if (message == null) {
                return "";
            }

            // 4. 提取 message 中的 content 字符串（第三层嵌套）
            String content = (String) message.get("content");
            if (content == null || content.trim().isEmpty()) {
                return "";
            }

            return content;

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public List<TaskInitResponse> extractTaskList(String content) {
        try {
            Type taskType = new TypeToken<Map<String, List<TaskInitResponse>>>() {
            }.getType();
            Map<String, List<TaskInitResponse>> contentMap = gson.fromJson(content, taskType);

            return contentMap.getOrDefault("result", new ArrayList<>());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
