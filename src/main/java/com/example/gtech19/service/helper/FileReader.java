package com.example.gtech19.service.helper;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.nio.charset.StandardCharsets;

/**
 * 文件读取助手类
 *
 * @return 文件内容
 */
@Component
public class FileReader {
    public String readStaticFile(String fileName) throws Exception {
        // 读取static目录下的test.txt文件
        ClassPathResource resource = new ClassPathResource("static/" + fileName);

        // 读取文件内容为字符串
        byte[] bytes = FileCopyUtils.copyToByteArray(resource.getInputStream());
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
