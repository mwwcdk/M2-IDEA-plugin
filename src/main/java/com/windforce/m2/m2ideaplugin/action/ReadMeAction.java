package com.windforce.m2.m2ideaplugin.action;

import com.intellij.notification.*;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

/**
 * @ClassName ReadMeAction
 * @Description 行为 -- 插件说明
 * @Author LiChangfei
 * @Date 2025/3/11 09:58
 * @Version 1.0
 **/
public class ReadMeAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        NotificationGroup notificationGroup = new NotificationGroup("ReadMeAction", NotificationDisplayType.STICKY_BALLOON, true);
        String content = "<html>SVN更新策划主干目录:<br>" +
                "初次使用时会要求用户手动选择目录<br><br>" +
                "SVN更新客户端主干目录:<br>" +
                "初次使用时会要求用户手动选择目录<br><br>" +
                "打开策划主干目录:<br>" +
                "初次使用时会要求用户手动选择目录<br><br>" +
                "根据策划配置class生成服务端conf:<br>" +
                "需要先将光标定位至XxResource.java或YyConfigItem.java文件编辑器, 再执行此行为, 会自动生成application.conf中的注册信息</html>";
        /**
         * content :  通知内容
         * type  ：通知的类型，warning,info,error
         */
        Notification notification = notificationGroup.createNotification("M2插件使用说明", content, NotificationType.INFORMATION);
        Notifications.Bus.notify(notification);
    }

}
