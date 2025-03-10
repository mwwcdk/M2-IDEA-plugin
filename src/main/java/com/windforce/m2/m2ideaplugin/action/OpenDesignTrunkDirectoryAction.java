package com.windforce.m2.m2ideaplugin.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.windforce.m2.m2ideaplugin.persistent.PluginSettings;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * @ClassName OpenDesignTrunkDirectoryAction
 * @Description 行为 -- 打开策划主干目录
 * @Author LiChangfei
 * @Date 2025/3/10 16:49
 * @Version 1.0
 **/
public class OpenDesignTrunkDirectoryAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        // 读取本地存储的策划主干目录
        String designTrunkDirectory = PluginSettings.getInstance().getState().designTrunkDirectory;
        // 如果未曾设置过策划主干目录
        if (StringUtils.isEmpty(designTrunkDirectory)) {
            // 选取SVN目录
            FileChooserDescriptor descriptor = new FileChooserDescriptor(false, true, false, false, false, false);
            descriptor.setTitle("请指定策划trunk所在目录");
            descriptor.setDescription("例如G:\\FM-M1\\Design\\trunk");
            VirtualFile selectedDir = FileChooser.chooseFile(descriptor, anActionEvent.getProject(), null);
            designTrunkDirectory = selectedDir.getPath();
            // 持久化
            PluginSettings.getInstance().getState().designTrunkDirectory = designTrunkDirectory;
        }
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.OPEN)) {
                try {
                    desktop.open(new File(designTrunkDirectory));
                } catch (IOException e) {
                    Messages.showErrorDialog("无法打开目录: " + e.getMessage(), "错误");
                }
            }
        }
    }

}
