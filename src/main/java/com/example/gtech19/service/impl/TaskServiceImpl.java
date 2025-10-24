package com.example.gtech19.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.example.gtech19.config.enums.*;
import com.example.gtech19.mapper.TaskMapper;
import com.example.gtech19.mapper.UserMapper;
import com.example.gtech19.model.Task;
import com.example.gtech19.model.TaskAiDesc;
import com.example.gtech19.model.User;
import com.example.gtech19.service.ChatService;
import com.example.gtech19.service.TaskAiDescService;
import com.example.gtech19.service.TaskService;
import com.example.gtech19.service.helper.ChatResponseParseHelper;
import com.example.gtech19.service.helper.FileReader;
import com.example.gtech19.service.helper.TaskHelper;
import com.example.gtech19.service.impl.dto.request.TaskListRequest;
import com.example.gtech19.service.impl.dto.request.TaskUpdateRequest;
import com.example.gtech19.service.impl.dto.request.TaskUserCompleteRequest;
import com.example.gtech19.service.impl.dto.request.TaskUserCreateRequest;
import com.example.gtech19.service.impl.dto.response.TaskResponse;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * 任务服务实现类
 */
@Service
@Slf4j
public class TaskServiceImpl implements TaskService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private TaskHelper taskHelper;

    @Autowired
    private TaskAiDescService taskAiDescService;

    @Autowired
    private ChatService chatService;

    @Autowired
    private ChatResponseParseHelper chatResponseParseHelper;

    @Autowired
    private FileReader fileReader;

    @Autowired
    private Gson gson;

    @Value("${chat.create-task-detail.system}")
    private String createTaskDetailSystemPrompt;

    @Value("${chat.create-task-detail.user}")
    private String createTaskDetailUserPrompt;

    @Override
    public Long userCreateTask(TaskUserCreateRequest request) {

        Task task = new Task();
        BeanUtils.copyProperties(request, task);
        task.setTaskType(TaskTypeEnum.OTHER.getCode());
        task.setTaskSource(TaskSourceEnum.USER_DEFINED.getCode());

        // 设置默认值
        if (task.getTaskPoints() == null) {
            task.setTaskPoints(0);
        }
        task.setTaskStatus(0); // 初始状态为未完成
        task.setTaskDate(new Date());
        task.setCreateTime(new Date());
        task.setUpdateTime(new Date());
        task.setTaskDate(DateUtil.parse(request.getTaskDate()));
        task.setIsDeleted(0);
        task.setTaskPoints(TaskTypeEnum.OTHER.getPoints());
        // 新增任务
        taskMapper.insert(task);
        return task.getId();
    }

    @Override
    public TaskResponse updateTask(TaskUpdateRequest request) {
        if (request == null || request.getId() == null) {
            return null;
        }

        Task task = taskMapper.selectById(request.getId());
        if (task == null) {
            return null;
        }

        // 复制更新字段
        BeanUtils.copyProperties(request, task, "id", "createTime", "updateTime");
        task.setUpdateTime(new Date());

        // 更新任务
        taskMapper.update(task);
        return getTaskById(request.getId());
    }

    @Override
    public TaskResponse getTaskById(Long id) {
        Task task = taskMapper.selectById(id);
        if (task == null) {
            return null;
        }
        return convertToResponse(task);
    }

    @Override
    public List<TaskResponse> getTasksByUserId(TaskListRequest request) {
        // 执行查询
        List<Task> tasks = taskMapper.selectByPage(request);

        // 转换为TaskResponse列表
        return tasks.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public TaskResponse completeTask(TaskUserCompleteRequest request) {
        Task task = taskMapper.selectById(request.getTaskId());
        if (task == null) {
            return null;
        }
        task.setTaskStatus(1); // 标记为已完成
        task.setFinishTime(new Date());
        task.setUpdateTime(new Date());
        taskMapper.update(task);
        return getTaskById(request.getTaskId());
    }


    /**
     * 转换Task对象为TaskResponse对象
     */
    private TaskResponse convertToResponse(Task task) {
        TaskResponse response = new TaskResponse();
        BeanUtils.copyProperties(task, response);
        response.setTaskSourceName(TaskSourceEnum.getByCode(response.getTaskSource()).getName());
        response.setTaskTypeName(TaskTypeEnum.getByCode(response.getTaskType()).getName());
        response.setTaskStatusName(TaskStatusEnum.getByCode(response.getTaskStatus()).getName());
        response.setTaskDate(DateUtil.formatDate(task.getTaskDate()));
        if (StrUtil.isNotBlank(task.getTaskCode())) {
            TaskLibraryEnum taskLibraryEnum = TaskLibraryEnum.getByTaskId(Integer.parseInt(task.getTaskCode()));
            if (taskLibraryEnum != null) {
                if ("TOOLS".equals(taskLibraryEnum.getTaskType())) {
                    response.setToolCodeImageUrl(taskLibraryEnum.getJumpUrl());
                }
                response.setJumpType(taskLibraryEnum.getTaskType().equals("——") ? null : taskLibraryEnum.getTaskType());
            }
        }
        if (TaskTypeEnum.COURSE.getCode().equals(task.getTaskType())) {
            response.setImageUrl("https://res.gaodunwangxiao.com/tools/2025-10-24/84684075_course1.png");
        }
        if (TaskTypeEnum.LIVE.getCode().equals(task.getTaskType())) {
            response.setImageUrl("https://res.gaodunwangxiao.com/tools/2025-10-24/84716154_live5.png");
        }
        return response;
    }

    @Override
    public Flux<String> createTaskAiDetail(Long taskId) {
        if (taskId == null) {
            return Flux.error(new IllegalArgumentException("任务ID不能为空"));
        }

        // 1. 查询task_ai_desc表
        TaskAiDesc existingDesc = taskAiDescService.getTaskAiDescByTaskId(taskId);
        if (existingDesc != null && StrUtil.isNotBlank(existingDesc.getTaskAiDesc())) {
            // 如果记录存在且task_ai_desc不为空，直接返回
            String content = existingDesc.getTaskAiDesc();
            // 将完整内容按字符拆分，模拟流式输出
            return Flux.fromArray(content.split(""))
                    .delayElements(Duration.ofMillis(50)); // 可选：添加延迟模拟打字效果
        }

        // 2. 查询任务详情获取taskCode和userId
        Task task = taskMapper.selectById(taskId);
        if (task == null) {
            return Flux.error(new IllegalArgumentException("任务不存在"));
        }

        String taskCode = task.getTaskCode();
        if (StrUtil.isBlank(taskCode)) {
            return Flux.error(new IllegalArgumentException("任务编码为空"));
        }

        // 3. 查询用户信息获取grade、major、target
        User user = userMapper.selectByUserId(task.getUserId());
        if (user == null) {
            return Flux.error(new IllegalArgumentException("用户不存在"));
        }

        // 4. 通过taskCode查询TaskLibraryConfigEnum获取taskDesc
        TaskLibraryEnum taskConfig = TaskLibraryEnum.getByTaskId(Integer.parseInt(taskCode));
        if (taskConfig == null) {
            return Flux.error(new IllegalArgumentException("任务配置不存在"));
        }

        String taskDesc = taskConfig.getTaskDesc();

        // 5. 读取提示词模板
        String systemPrompt;
        String userPrompt;
        try {
            systemPrompt = fileReader.readStaticFile(createTaskDetailSystemPrompt);
            userPrompt = fileReader.readStaticFile(createTaskDetailUserPrompt);
        } catch (Exception e) {
            log.error("读取提示词模板失败", e);
            return Flux.error(new RuntimeException("读取提示词模板失败"));
        }

        // 6. 替换用户提示词中的占位符
        userPrompt = userPrompt.replace("{{user-command}}", taskDesc)
                .replace("{{grade}}", user.getGrade() != null ? user.getGrade() : "")
                .replace("{{major}}", user.getMajor() != null ? user.getMajor() : "")
                .replace("{{target}}", user.getTarget() != null ? user.getTarget() : "");

        // 7. 构建请求参数
        Map<String, Object> request = new HashMap<>();
        request.put("system", systemPrompt);
        request.put("content", userPrompt);

        // 8. 调用流式模型
        long startTime = System.currentTimeMillis();
        Flux<String> aiResponseFlux = chatService.generalChatStream(request);

        // 9. 处理流式响应：累积完整内容并保存到数据库
        AtomicReference<StringBuilder> fullContentRef = new AtomicReference<>(new StringBuilder());

        return aiResponseFlux
                .doOnNext(chunk -> {
                    // 解析每个chunk，提取content
                    try {
                        // SSE格式的chunk以"data: "开头，需要先移除前缀
                        String jsonStr = chunk;
                        if (chunk.startsWith("data: ")) {
                            jsonStr = chunk.substring(6).trim();
                        }

                        Map<String, Object> chunkMap = gson.fromJson(jsonStr, Map.class);
                        String content = chatResponseParseHelper.extractResultContent(chunkMap);
                        if (StrUtil.isNotBlank(content)) {
                            fullContentRef.get().append(content);
                        }
                    } catch (Exception e) {
                        log.warn("解析chunk失败: {}", chunk, e);
                    }
                })
                .doOnComplete(() -> {
                    // 流式响应完成后，保存到数据库
                    String fullContent = fullContentRef.get().toString();
                    long endTime = System.currentTimeMillis();
                    long costMs = endTime - startTime;
                    if (StrUtil.isNotBlank(fullContent)) {
                        TaskAiDesc newDesc = new TaskAiDesc();
                        newDesc.setTaskId(taskId);
                        newDesc.setTaskName(task.getTaskName());
                        newDesc.setTaskAiDesc(fullContent);
                        newDesc.setCreateTime(new Date());
                        newDesc.setUpdateTime(new Date());
                        newDesc.setIsDeleted(0);
                        taskAiDescService.createTaskAiDesc(newDesc);
                        log.info("任务AI详情已保存，taskId={}", taskId);
                    }
                    try {
                        taskHelper.createChatLog(task.getUserId(), gson.toJson(request), fullContent, costMs, "createTaskDetail");
                    } catch (Exception e) {
                        log.error("创建聊天记录日志失败", e);
                    }

                })
                .map(chunk -> {
                    // 返回解析后的content给前端
                    try {
                        // SSE格式的chunk以"data: "开头，需要先移除前缀
                        String jsonStr = chunk;
                        if (chunk.startsWith("data: ")) {
                            jsonStr = chunk.substring(6).trim();
                        }

                        Map<String, Object> chunkMap = gson.fromJson(jsonStr, Map.class);
                        String content = chatResponseParseHelper.extractResultContent(chunkMap);
                        return StrUtil.isNotBlank(content) ? content : "";
                    } catch (Exception e) {
                        log.warn("解析返回chunk失败: {}", chunk, e);
                        return "";
                    }
                })
                .filter(StrUtil::isNotBlank);
    }

}