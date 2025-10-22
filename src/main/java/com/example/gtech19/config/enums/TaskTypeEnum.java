package com.example.gtech19.config.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 任务类型枚举
 */
@Getter
@AllArgsConstructor
public enum TaskTypeEnum {

    LIVE(1, "直播", "直播任务类型") {
        @Override
        public boolean needImageDisplay() {
            return true;
        }
    },
    COURSE(2, "课程", "课程任务类型") {
        @Override
        public boolean needImageDisplay() {
            return true;
        }
    },
    OTHER(3, "其它", "其它任务类型") {
        @Override
        public boolean needImageDisplay() {
            return false;
        }
    };

    private final Integer code;
    private final String name;
    private final String description;

    // 缓存code到枚举的映射，提高查找效率
    private static final Map<Integer, TaskTypeEnum> CODE_TO_ENUM = 
            Arrays.stream(values()).collect(Collectors.toMap(TaskTypeEnum::getCode, Function.identity()));

    /**
     * 根据code获取枚举
     * @param code 类型编码
     * @return 枚举实例或null
     */
    public static TaskTypeEnum getByCode(Integer code) {
        return CODE_TO_ENUM.get(code);
    }

    /**
     * 判断是否需要显示图片
     * @return 是否需要显示图片
     */
    public abstract boolean needImageDisplay();

    /**
     * 判断code是否有效
     * @param code 类型编码
     * @return 是否有效
     */
    public static boolean isValid(Integer code) {
        return code != null && CODE_TO_ENUM.containsKey(code);
    }
}