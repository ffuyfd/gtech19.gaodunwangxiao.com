package com.example.gtech19.config.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 任务来源枚举
 */
@Getter
@AllArgsConstructor
public enum TaskSourceEnum {

    TASK_LIBRARY(1, "任务库", "系统任务库中的任务") {
        @Override
        public boolean hasOperationButton() {
            return true;
        }
    },
    AI(2, "AI", "AI生成的任务") {
        @Override
        public boolean hasOperationButton() {
            return false;
        }
    },
    USER_DEFINED(3, "用户自定义", "用户自己创建的任务") {
        @Override
        public boolean hasOperationButton() {
            return false;
        }
    };

    private final Integer code;
    private final String name;
    private final String description;

    // 缓存code到枚举的映射，提高查找效率
    private static final Map<Integer, TaskSourceEnum> CODE_TO_ENUM = 
            Arrays.stream(values()).collect(Collectors.toMap(TaskSourceEnum::getCode, Function.identity()));

    /**
     * 根据code获取枚举
     * @param code 来源编码
     * @return 枚举实例或null
     */
    public static TaskSourceEnum getByCode(Integer code) {
        return CODE_TO_ENUM.get(code);
    }

    /**
     * 判断是否有操作按钮
     * @return 是否有操作按钮
     */
    public abstract boolean hasOperationButton();

    /**
     * 判断code是否有效
     * @param code 来源编码
     * @return 是否有效
     */
    public static boolean isValid(Integer code) {
        return code != null && CODE_TO_ENUM.containsKey(code);
    }
}