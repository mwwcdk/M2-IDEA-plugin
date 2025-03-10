package com.windforce.m2.m2ideaplugin.persistent;

import com.intellij.openapi.components.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @ClassName PluginSettings
 * @Description 插件数据持久化保存
 * @Author LiChangfei
 * @Date 2025/3/10 12:42
 * @Version 1.0
 **/
@State(name = "PluginSettings", storages = @Storage("M2_plugin_settings.xml"))
@Service
public final class PluginSettings implements PersistentStateComponent<PluginSettings.State> {

    private State state = new State();

    public static class State {
        /** 策划trunk目录 */
        public String designTrunkDirectory = "";
    }

    @Override
    public @Nullable State getState() {
        return state;
    }

    @Override
    public void loadState(@NotNull State state) {
        this.state = state;
    }

    public static PluginSettings getInstance() {
        return ServiceManager.getService(PluginSettings.class);
    }

}
