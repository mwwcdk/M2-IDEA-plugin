package com.windforce.m2.m2ideaplugin.action;

import com.intellij.ide.util.DirectoryChooser;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.vfs.VirtualFile;
import com.windforce.m2.m2ideaplugin.persistent.PluginSettings;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 行为 -- SVN更新策划trunk目录
 * @Author LiChangfei
 */
public class SvnUpdateDesignTrunkDirectoryAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        // 读取本地存储的策划主干目录
        String designTrunkDirectory = PluginSettings.getInstance().getState().designTrunkDirectory;
        // 如果未曾设置过策划主干目录
        if (StringUtils.isEmpty(designTrunkDirectory)) {
            // 选取SVN目录
            FileChooserDescriptor descriptor = new FileChooserDescriptor(false, true, false, false, false, false);
            descriptor.setTitle("选择目录");
            descriptor.setDescription("请指定策划trunk所在目录");
            VirtualFile selectedDir = FileChooser.chooseFile(descriptor, anActionEvent.getProject(), null);
            designTrunkDirectory = selectedDir.getPath();
            // 持久化
            PluginSettings.getInstance().getState().designTrunkDirectory = designTrunkDirectory;
        }
        // 执行SVN更新
        try {
            Runtime.getRuntime().exec("cmd /c start TortoiseProc.exe /command:update /path:\"" + designTrunkDirectory + "\"");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

//        NotificationGroup notificationGroup = new NotificationGroup("testid", NotificationDisplayType.BALLOON, false);
//        /**
//         * content :  通知内容
//         * type  ：通知的类型，warning,info,error
//         */
//        Notification notification = notificationGroup.createNotification("测试通知", MessageType.INFO);
//        Notifications.Bus.notify(notification);
    }

}
