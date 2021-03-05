package io.github.takusan23.electric_pickaxe.generator;

import io.github.takusan23.electric_pickaxe.item.RegisterItems;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

/**
 * ローカライズのファイルを生成する
 * <p>
 * 日本語版
 * <p>
 * なお、デフォルト状態では日本語を扱えないため、build.gradleに一行書き足す必要があります。
 */
public class JaLanguageGenerator extends LanguageProvider {

    public JaLanguageGenerator(DataGenerator gen, String modid) {
        super(gen, modid, "ja_jp"); // locale.jsonが生成
    }

    @Override
    protected void addTranslations() {
        // アイテム名など
        add(RegisterItems.ELECTRIC_PICKAXE_ITEM.get(), "電動ツルハシ");
        add(RegisterItems.BASE_MODULE_ITEM.get(), "基本モジュール");
        add(RegisterItems.SILK_TOUCH_FORTUNE_MODULE_ITEM.get(), "シルクタッチ、幸運モジュール");
        add(RegisterItems.RANGE_ATTACK_MODULE_ITEM.get(), "範囲攻撃モジュール");
        add(RegisterItems.BATTERY_UPGRADE_MODULE_ITEM.get(), "電池容量増加モジュール");
        add(RegisterItems.DAMAGE_UPGRADE_MODULE_ITEM.get(), "攻撃力上昇モジュール");
        add(RegisterItems.CREATIVE_TAB.getGroupName().getString(), "電動ツルハシ");

        // そのほかTooltipの説明など
        add("tooltip.installed_module", "インストール済みモジュール");
        add("tooltip.not_found_module", "インストール済みモジュールはありません");
        add("tooltip.max_count", "最大搭載量は1です");
        add("tooltip.item_description","シャベル + ツルハシ + 斧");
        add("tooltip.forge_energy","Forge Energy");
    }
}
