package com.windforce.m2.m2ideaplugin.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.vfs.VirtualFile;
import com.windforce.m2.m2ideaplugin.persistent.PluginSettings;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

/**
 * @ClassName SvnUpdateClientTrunkDirectoryAction
 * @Description 行为 -- SVN更新客户端trunk目录
 * @Author LiChangfei
 * @Date 2025/3/10 15:38
 * @Version 1.0
 **/
public class SvnUpdateClientTrunkDirectoryAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        // 读取本地存储的策划主干目录
        String clientTrunkDirectory = PluginSettings.getInstance().getState().clientTrunkDirectory;
        // 如果未曾设置过策划主干目录
        if (StringUtils.isEmpty(clientTrunkDirectory)) {
            // 选取SVN目录
            FileChooserDescriptor descriptor = new FileChooserDescriptor(false, true, false, false, false, false);
            descriptor.setTitle("选择目录");
            descriptor.setDescription("请指定客户端trunk所在目录");
            VirtualFile selectedDir = FileChooser.chooseFile(descriptor, anActionEvent.getProject(), null);
            clientTrunkDirectory = selectedDir.getPath();
            // 持久化
            PluginSettings.getInstance().getState().clientTrunkDirectory = clientTrunkDirectory;
        }
        // 执行SVN更新
        try {
            Runtime.getRuntime().exec("cmd /c start TortoiseProc.exe /command:update /path:\"" + clientTrunkDirectory + "\"");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
