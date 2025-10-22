package com.example.gtech19.config.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 删除状态枚举
 */
@Getter
@AllArgsConstructor
public enum DeleteStatusEnum {

    NOT_DELETED(0, "未删除", "记录未被删除") {
        @Override
        public boolean isActive() {
            return true;
        }
    },
    DELETED(1, "已删除", "记录已被删除") {
        @Override
        public boolean isActive() {
            return false;
        }
    };

    private final Integer code;
    private final String name;
    private final String description;

    // 缓存code到枚举的映射，提高查找效率
    private static final Map<Integer, DeleteStatusEnum> CODE_TO_ENUM = 
            Arrays.stream(values()).collect(Collectors.toMap(DeleteStatusEnum::getCode, Function.identity()));

    /**
     * 根据code获取枚举
     * @param code 状态编码
     * @return 枚举实例或null
     */
    public static DeleteStatusEnum getByCode(Integer code) {
        return CODE_TO_ENUM.get(code);
    }

    /**
     * 判断记录是否处于活动状态
     * @return 是否活动
     */
    public abstract boolean isActive();

    /**
     * 判断code是否有效
     * @param code 状态编码
     * @return 是否有效
     */
    public static boolean isValid(Integer code) {
        return code != null && CODE_TO_ENUM.containsKey(code);
    }
}