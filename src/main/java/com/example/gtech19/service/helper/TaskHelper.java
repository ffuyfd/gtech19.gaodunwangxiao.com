package com.example.gtech19.service.helper;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.example.gtech19.common.Result;
import com.example.gtech19.config.enums.*;
import com.example.gtech19.mapper.TaskMapper;
import com.example.gtech19.mapper.UserMapper;
import com.example.gtech19.model.ChatLog;
import com.example.gtech19.model.Task;
import com.example.gtech19.model.User;
import com.example.gtech19.service.ChatLogService;
import com.example.gtech19.service.ChatService;
import com.example.gtech19.service.impl.dto.response.TaskInitResponse;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class TaskHelper {

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private ChatService chatService;

    @Autowired
    private ChatLogService chatLogService;

    @Autowired
    private FileReader fileReader;

    @Autowired
    private ChatResponseParseHelper chatResponseParseHelper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private Gson gson;

    @Value("${chat.init-task.system}")
    private String initTaskSystemPrompt;

    @Value("${chat.init-task.user}")
    private String initTaskUserPrompt;

    @Value("${chat.create-task-detail.system}")
    private String createTaskDetailSystemPrompt;

    @Value("${chat.create-task-detail.user}")
    private String createTaskDetailUserPrompt;

    private final String FIRST_TARGET_DAY = "今天到本周日";
    private final String SECOND_TARGET_DAY = "下周一到2025年12月31日";

    /**
     * 用户首次初始化任务
     *
     * @param userId
     */
    public void firstInitTask(String userId) {

        User user = userMapper.selectByUserId(userId);
        if (user == null) {
            return;
        }
        if (StrUtil.isBlank(user.getSchool()) && StrUtil.isBlank(user.getGrade()) && StrUtil.isBlank(user.getMajor()) && StrUtil.isBlank(user.getTarget())) {
            return;
        }

        List<Task> tasks = taskMapper.selectByUserId(userId);
        //已有任务先删除
        if (CollectionUtil.isNotEmpty(tasks)) {
            taskMapper.deleteUserTask(userId, new Date());
        }

        String taskLibrary = filterTaskLibrary(user);

        initTask(user, taskLibrary, FIRST_TARGET_DAY);
        initTask(user, taskLibrary, SECOND_TARGET_DAY);
    }

    public void initTask(User user, String taskLibrary, String targetDay) {

        String systemPrompt = "";
        String userPrompt = "";
        try {
            // 读取系统提示词
            systemPrompt = fileReader.readStaticFile(initTaskSystemPrompt);
            // 读取用户提示词
            userPrompt = fileReader.readStaticFile(initTaskUserPrompt);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        systemPrompt = systemPrompt.replace("{{taskLibrary}}", taskLibrary);

        // 替换用户提示词中的占位符
        userPrompt = userPrompt.replace("{{grade}}", user.getGrade());
        userPrompt = userPrompt.replace("{{major}}", user.getMajor());
        userPrompt = userPrompt.replace("{{target}}", user.getTarget());
        userPrompt = userPrompt.replace("{{currentDay}}", DateUtil.format(new Date(), "yyyy-MM-dd"));
        userPrompt = userPrompt.replace("{{targetDay}}", targetDay);

        // 调用ChatService生成任务
        Map<String, Object> request = new HashMap<>();
        request.put("system", systemPrompt);
        request.put("content", userPrompt);
        // 调用ChatService生成任务
        long startTime = System.currentTimeMillis();
        Result<Map<String, Object>> result = chatService.generalChat(request);
        long endTime = System.currentTimeMillis();
        Long costMs = endTime - startTime;
        createChatLog(user.getUserid(), gson.toJson(request), gson.toJson(result.getResult()), costMs, "createTask");

        String content = chatResponseParseHelper.extractResultContent(result.getResult());

        List<TaskInitResponse> taskInitResponses = chatResponseParseHelper.extractTaskList(content);
        //写表
        if (CollectionUtil.isNotEmpty(taskInitResponses)) {
            createTask(taskInitResponses, user.getUserid());
        }
    }

    private void createTask(List<TaskInitResponse> taskInitResponses, String userId) {
        taskInitResponses.forEach(taskInitResponse -> {
            Task task = new Task();
            task.setUserId(userId);
            task.setTaskDate(DateUtil.parse(taskInitResponse.getYmd()));
            task.setTaskName(taskInitResponse.getName());
            task.setTaskDesc(taskInitResponse.getMs());
            task.setTaskCode(StrUtil.isBlank(taskInitResponse.getId()) || "null".equals(taskInitResponse.getId())
                    ? null : taskInitResponse.getId());

            if (StrUtil.isBlank(taskInitResponse.getType()) || "null".equals(taskInitResponse.getType())) {
                taskInitResponse.setType(TaskTypeEnum.OTHER.getName());
            }

            TaskTypeEnum taskTypeEnum = TaskTypeEnum.getByName(taskInitResponse.getType());

            task.setTaskType(taskTypeEnum.getCode());
            task.setIsDeleted(0);
            task.setTaskSource(StrUtil.isNotEmpty(task.getTaskCode())
                    ? TaskSourceEnum.TASK_LIBRARY.getCode() : TaskSourceEnum.AI.getCode());

            //分数
            int points = taskTypeEnum.getPoints();
            if (TaskTypeEnum.OTHER.getName().equals(taskInitResponse.getType()) && StrUtil.isNotEmpty(task.getTaskCode())) {
                TaskLibraryEnum taskLibraryEnum = TaskLibraryEnum.getByTaskId(Integer.parseInt(task.getTaskCode()));
                points = taskLibraryEnum != null && "TOOLS".equals(taskLibraryEnum.getTaskType()) ? TaskTypeEnum.TOOLS.getPoints() : TaskTypeEnum.OTHER.getPoints();
            }
            task.setTaskPoints(points);
            task.setTaskStatus(TaskStatusEnum.PENDING.getCode());
            task.setImageUrl("");
            task.setCreateTime(new Date());
            task.setUpdateTime(new Date());
            taskMapper.insert(task);
        });
    }

    public void createChatLog(String userId, String chatRequest, String chatResponse, Long costMs, String bizType) {
        ChatLog chatLog = new ChatLog();
        chatLog.setUserId(userId);
        chatLog.setBizType(StrUtil.isBlank(bizType) ? "createTask" : bizType);
        chatLog.setChatRequest(chatRequest);
        chatLog.setChatResponse(chatResponse);
        chatLog.setCostMs(costMs);
        chatLog.setIsDeleted(0);
        chatLog.setCreateTime(new Date());
        chatLog.setUpdateTime(new Date());
        chatLogService.createChatLog(chatLog);
    }

    public String filterTaskLibrary(User user) {
        List<TaskLibraryEnum> libraryEnums = Arrays.asList(TaskLibraryEnum.values());

        List<TaskLibraryEnum> filteredEnums = libraryEnums.stream()
                .filter(enumItem -> enumItem.getTarget().contains(user.getTarget()))
                .filter(enumItem -> enumItem.getGrade().contains(user.getGrade()))
                .collect(Collectors.toList());
        if (CollectionUtil.isEmpty(filteredEnums)) {
            filteredEnums = libraryEnums;
        }
        //公务员选调科普	1	想考公选调？了解科普，开启公职预备。	公务员	大一上	8月,9月,10月,11月,12月
        String response = "";
        for (TaskLibraryEnum taskLibraryEnum : filteredEnums) {
            response += taskLibraryEnum.getTaskName() + "\t" + taskLibraryEnum.getTaskId() + "\t"
                    + taskLibraryEnum.getTaskDesc() + "\t"
                    + taskLibraryEnum.getGrade() + "\t" + taskLibraryEnum.getTimePoint() + "\n";
        }

        return response;
    }

}
