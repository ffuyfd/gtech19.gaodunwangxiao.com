package com.example.gtech19.config.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 任务状态枚举
 */
@Getter
@AllArgsConstructor
public enum TaskStatusEnum {

    PENDING(0, "未完成", "任务尚未完成") {
        @Override
        public boolean canComplete() {
            return true;
        }
    },
    COMPLETED(1, "已完成", "任务已经完成") {
        @Override
        public boolean canComplete() {
            return false;
        }
    };

    private final Integer code;
    private final String name;
    private final String description;

    // 缓存code到枚举的映射，提高查找效率
    private static final Map<Integer, TaskStatusEnum> CODE_TO_ENUM = 
            Arrays.stream(values()).collect(Collectors.toMap(TaskStatusEnum::getCode, Function.identity()));

    /**
     * 根据code获取枚举
     * @param code 状态编码
     * @return 枚举实例或null
     */
    public static TaskStatusEnum getByCode(Integer code) {
        return CODE_TO_ENUM.get(code);
    }

    /**
     * 判断是否可以完成任务
     * @return 是否可以完成任务
     */
    public abstract boolean canComplete();

    /**
     * 判断code是否有效
     * @param code 状态编码
     * @return 是否有效
     */
    public static boolean isValid(Integer code) {
        return code != null && CODE_TO_ENUM.containsKey(code);
    }
}