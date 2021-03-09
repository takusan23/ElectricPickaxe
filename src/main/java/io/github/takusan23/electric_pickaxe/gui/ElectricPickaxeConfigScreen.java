package io.github.takusan23.electric_pickaxe.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import io.github.takusan23.electric_pickaxe.item.ModulePickaxeItem;
import io.github.takusan23.electric_pickaxe.item.RegisterItems;
import io.github.takusan23.electric_pickaxe.tool.LocalizeString;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.net.CacheRequest;

/**
 * {@link io.github.takusan23.electric_pickaxe.ElectricPickaxe}の設定画面
 * <p>
 * 範囲攻撃やシルクタッチモジュール切り替えなど
 */
@OnlyIn(Dist.CLIENT)
public class ElectricPickaxeConfigScreen extends Screen {

    /**
     * ModuleItemPickaxeのItemStack
     */
    private ItemStack modulePickaxeItemStack = null;

    /**
     * modulePickaxeItemStackからItemを取り出した
     */
    private ModulePickaxeItem modulePickaxeItem = null;

    /**
     * 範囲攻撃モジュールのボタンの位置。テキスト表示と位置を合わせるため
     */
    private int rangeButtonYPos = 0;

    /**
     * @param itemStack {@link ModulePickaxeItem}のItemStackにしてください。
     */
    public ElectricPickaxeConfigScreen(ItemStack itemStack) {
        super(new StringTextComponent(LocalizeString.getLocalizeString("screen.title"))); // タイトル
        modulePickaxeItemStack = itemStack;
        modulePickaxeItem = (ModulePickaxeItem) modulePickaxeItemStack.getItem();
    }

    protected ElectricPickaxeConfigScreen(ITextComponent titleIn) {
        super(titleIn);
    }

    /**
     * ボタンはここで描画するらしい
     */
    @Override
    protected void init() {
        super.init();

        int buttonYPos = 40;

        // エンチャント切り替えボタン
        if (isCheckInstalledModule(RegisterItems.SILK_TOUCH_FORTUNE_MODULE_ITEM.get().getRegistryNameString())) {
            addEnchantButton(buttonYPos);
            buttonYPos += 40;
        }

        // 範囲攻撃
        if (isCheckInstalledModule(RegisterItems.RANGE_ATTACK_MODULE_ITEM.get().getRegistryNameString())) {
            addRangeButton(buttonYPos);
            buttonYPos += 40;
        }

        // 戻るボタン
        addCloseButton();
    }

    /**
     * 閉じるボタンを追加
     */
    private void addCloseButton() {
        int backButtonWidth = width / 2;
        this.addButton(new Button((width - backButtonWidth) / 2, height - 30, backButtonWidth, 20, new StringTextComponent(LocalizeString.getLocalizeString("screen.close")), p_onPress_1_ -> {
            closeScreen();
        }));
    }

    /**
     * エンチャント切り替えボタンを追加
     */
    private void addEnchantButton(int top) {
        int enchantButtonWidth = width / 2;
        addButton(new Button((width - enchantButtonWidth) / 2, top, enchantButtonWidth, 20, new StringTextComponent(LocalizeString.getLocalizeString("screen.enchant")), p_onPress_1_ -> {
            modulePickaxeItem.changeSilkFortune(Minecraft.getInstance().player, modulePickaxeItemStack);
        }));
    }

    /**
     * 範囲攻撃モジュールの設定ボタンを追加
     */
    private void addRangeButton(int top) {
        // 範囲攻撃モジュールのレジストリ名
        String registryName = RegisterItems.RANGE_ATTACK_MODULE_ITEM.get().getRegistryNameString();
        rangeButtonYPos = top;
        // 各ボタンの幅
        int rangeButtonWidth = width / 6;
        // 一番右からボタンを作っていく。一番右のボタンの位置
        int rangeButtonXPos = (width - (rangeButtonWidth * 3)) / 2;
        addButton(new Button(rangeButtonXPos, top, rangeButtonWidth, 20, new StringTextComponent("-"), p_onPress_1_ -> {
            // レベルを落とす
            int currentLevel = modulePickaxeItem.getModuleSettingInt(modulePickaxeItemStack, registryName);
            // 今乗ってるインストール数
            int maxValue = modulePickaxeItem.getModuleLevel(modulePickaxeItemStack, registryName);
            // デクリメントする
            int versionDown = currentLevel - 1;
            if (0 < versionDown && versionDown <= maxValue) {
                modulePickaxeItem.setModuleSettingInt(modulePickaxeItemStack, registryName, versionDown);
            }
        }));
        // 真ん中にスペースを
        rangeButtonXPos += rangeButtonWidth + rangeButtonWidth;
        addButton(new Button(rangeButtonXPos, top, rangeButtonWidth, 20, new StringTextComponent("+"), p_onPress_1_ -> {
            // レベルを上げる
            int currentLevel = modulePickaxeItem.getModuleSettingInt(modulePickaxeItemStack, registryName);
            // 今乗ってるインストール数
            int maxValue = modulePickaxeItem.getModuleLevel(modulePickaxeItemStack, registryName);
            // インクリメント
            int versionUp = currentLevel + 1;
            if (0 < versionUp && versionUp <= maxValue) {
                modulePickaxeItem.setModuleSettingInt(modulePickaxeItemStack, registryName, versionUp);
            }
        }));
    }

    /**
     * テキスト（一番上のタイトルなど）の描画
     * <p>
     * ここは毎回呼ばれる（毎フレーム呼んでるみたいな感じで）
     */
    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        // タイトル
        drawCenteredString(matrixStack, this.font, title, this.width / 2, 10, 0xFFFFFF);

        // 範囲攻撃
        if (isCheckInstalledModule(RegisterItems.RANGE_ATTACK_MODULE_ITEM.get().getRegistryNameString())) {
            drawCenteredString(matrixStack, this.font, LocalizeString.getLocalizeString(RegisterItems.RANGE_ATTACK_MODULE_ITEM.get().getRegistryNameString()), this.width / 2, rangeButtonYPos - 10, 0xFFFFFF);
            // エリア
            int rangeAttackValue = modulePickaxeItem.getModuleSettingInt(modulePickaxeItemStack, RegisterItems.RANGE_ATTACK_MODULE_ITEM.get().getRegistryNameString());
            drawCenteredString(matrixStack, this.font, String.valueOf(rangeAttackValue), this.width / 2, rangeButtonYPos + 5, 0xFFFFFF);
        }

        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    /**
     * モジュールがインストール済みかどうか
     *
     * @param registryName モジュールのレジストリ名
     */
    private boolean isCheckInstalledModule(String registryName) {
        return modulePickaxeItem.isCheckInstalledModule(modulePickaxeItemStack, registryName);
    }


}
