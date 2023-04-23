package com.fit.common.log;

import ch.qos.logback.core.PropertyDefinerBase;

import java.io.File;

/**
 * @className: LogPathDefiner
 * @description: 日志路径
 * @author: Aim
 * @date: 2023/4/12
 **/
public class LogPathDefiner extends PropertyDefinerBase {

    @Override
    public String getPropertyValue() {
        String LogPath = System.getProperty("user.dir");
        String dirPath = LogPath + "/target";
        File file = new File(dirPath);
        if (file.exists()) {
            LogPath = dirPath;
        }
        LogPath = LogPath + "/logs/";
        System.out.println(" - 日志存放路径: " + LogPath);
        return LogPath;
    }
}