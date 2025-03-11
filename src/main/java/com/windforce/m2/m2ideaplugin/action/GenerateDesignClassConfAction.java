package com.windforce.m2.m2ideaplugin.action;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

/**
 * @ClassName GenerateDesignClassConfAction
 * @Description 根据策划配置类生成服务端conf
 * @Author LiChangfei
 * @Date 2025/3/10 17:12
 * @Version 1.0
 **/
public class GenerateDesignClassConfAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        VirtualFile currentFile = anActionEvent.getData(CommonDataKeys.VIRTUAL_FILE);
        NotificationGroup notificationGroup = new NotificationGroup("GenerateDesignClassConfAction", NotificationDisplayType.BALLOON, true);
        if (currentFile == null) {
            Notification notification = notificationGroup.createNotification("当前并未打开任何文件, 无法生成策划配置类conf!", MessageType.ERROR);
            Notifications.Bus.notify(notification);
            return;
        }
        String fileName = currentFile.getName();
        String[] nameParts = fileName.split("\\.");
        if (nameParts.length != 2) {
            Notification notification = notificationGroup.createNotification("当前打开的[" + fileName + "]并不是java文件, 无法生成策划配置类conf!", MessageType.ERROR);
            Notifications.Bus.notify(notification);
            return;
        }
        if (!nameParts[1].equals("java")) {
            Notification notification = notificationGroup.createNotification("当前打开的[" + fileName + "]并不是java文件, 无法生成策划配置类conf!", MessageType.ERROR);
            Notifications.Bus.notify(notification);
            return;
        }
        String className = nameParts[0];
        if ((!className.endsWith("Resource")) && (!className.endsWith("ConfigItem"))) {
            Notification notification = notificationGroup.createNotification("当前打开的[" + fileName + "]未由'Resource'或'ConfigItem'结尾, 无法生成策划配置类conf!", MessageType.ERROR);
            Notifications.Bus.notify(notification);
            return;
        }
        // 该策划静态配置的关键词
        String keyWord;
        if (className.endsWith("Resource")) {
            keyWord = className.substring(0, className.length() - "Resource".length());
        } else {
            keyWord = className.substring(0, className.length() - "ConfigItem".length());
        }

        // 找到application.conf文件
        String javaFilePath = currentFile.getPath();
        String applicationConfPath = javaFilePath.substring(0, javaFilePath.indexOf("/src/main/") + 10) + "/resources/application.conf";
        try {
            // 找到插入内容的目标行
            List<String> oldLines = Files.readAllLines(Path.of(applicationConfPath));
            int targetLineIndex = 0;
            for (int lineIndex = 0; lineIndex < oldLines.size(); lineIndex++) {
                if (oldLines.get(lineIndex).contains("= \"aliseResourceId")) {
                    targetLineIndex = lineIndex + 2;
                    break;
                }
            }
            // 新文件内容
            List<String> newLines = new LinkedList<>();
            for (int lineIndex = 0; lineIndex < oldLines.size(); lineIndex++) {
                if (lineIndex == targetLineIndex) {
                    newLines.add("    \"" + javaFilePath.substring(javaFilePath.indexOf("com/windforce/m1"), javaFilePath.length() - 5).replace('/', '.') + "\" {");
                    newLines.add("        \"aliseResource\" {");
                    newLines.add("            absolutePath = \"" + keyWord.toLowerCase() + "config.json\"");
                    newLines.add("        }");
                    newLines.add("        fields {");
                    newLines.add("            id {");
                    newLines.add("                \"aliseResourceId\" {}");
                    newLines.add("            }");
                    newLines.add("        }");
                    newLines.add("    },");
                    newLines.add("");
                }
                newLines.add(oldLines.get(lineIndex));
            }
            // 写入新文件
            Files.write(Path.of(applicationConfPath), newLines);
            // 刷新IDEA缓存中的该文件信息
            VirtualFile file = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(new File(applicationConfPath));
            file.refresh(true, true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        /**
         * content :  通知内容
         * type  ：通知的类型，warning,info,error
         */
        Notification notification = notificationGroup.createNotification("策划配置类[" + className + "]已成功注册至application.conf", MessageType.INFO);
        Notifications.Bus.notify(notification);
    }

}
