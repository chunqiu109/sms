package com.danmi.sms.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;

public class FilePathUtils {

    public static String getFilePath() throws IOException {
        String path = ResourceUtils.getURL("classpath:").getPath();

        // 如果是在eclipse中运行，则和target同级目录,如果是jar部署到服务器，则默认和jar包同级
        String pathStr = path.replace("/target/classes", "") + "authent_file";
        File file1 = new File(pathStr);
        if (!file1.exists()) {
            new File(pathStr).mkdirs();
        }
        return pathStr;
    }
}
