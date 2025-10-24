package com.example.gtech19.common.utils;

import java.time.LocalDate;

public class ToolsUtil {

    /**
     * 根据当前系统日期判断所属学期
     * 上学期：8月1日 - 1月31日
     * 下学期：2月1日 - 7月31日
     *
     * @return "上"表示上学期，"下"表示下学期
     */
    public static String getCurrentSemester() {
        // 获取当前系统日期
        LocalDate currentDate = LocalDate.now();
        // 获取当前月份（1-12）
        int month = currentDate.getMonthValue();

        // 判断月份所属学期
        if (month >= 8 || month <= 1) {
            return "上";
        } else {
            return "下";
        }
    }


}
