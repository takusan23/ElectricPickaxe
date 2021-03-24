package io.github.takusan23.electric_pickaxe.gui;

import net.minecraft.client.gui.ScreenManager;

/**
 * GUiを登録するメソッドがあるだけ
 * */
public class RegisterScreen {

    /**
     * Containerの画面をForgeに登録する
     *
     * 注意。クライアント側のみ登録すればいいです。
     */
    public static void register() {
        ScreenManager.registerFactory(RegisterContainer.SETTING_SCREEN_CONTAINER_CONTAINER_TYPE.get(), SettingScreenContainerScreen::new);
    }
}
