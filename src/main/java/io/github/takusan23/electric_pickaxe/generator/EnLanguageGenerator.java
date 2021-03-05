package io.github.takusan23.electric_pickaxe.generator;

import io.github.takusan23.electric_pickaxe.item.RegisterItems;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

/**
 * ローカライズのファイルを生成する
 * <p>
 * 英語版
 */
public class EnLanguageGenerator extends LanguageProvider {

    public EnLanguageGenerator(DataGenerator gen, String modid) {
        super(gen, modid, "en_us"); // local.jsonが生成させる
    }

    @Override
    protected void addTranslations() {
        // アイテム名など
        add(RegisterItems.ELECTRIC_PICKAXE_ITEM.get(), "Electric Pickaxe");
        add(RegisterItems.BASE_MODULE_ITEM.get(), "Base Module");
        add(RegisterItems.SILK_TOUCH_FORTUNE_MODULE_ITEM.get(), "SilkTouch Fortune Module");
        add(RegisterItems.RANGE_ATTACK_MODULE_ITEM.get(), "Range Attack Module");
        add(RegisterItems.BATTERY_UPGRADE_MODULE_ITEM.get(), "Battery Upgrade Module");
        add(RegisterItems.DAMAGE_UPGRADE_MODULE_ITEM.get(), "Damage Upgrade Module");
        add(RegisterItems.CREATIVE_TAB.getGroupName().getString(), "Electric Pickaxe");


        // そのほかTooltipの説明など
        add("tooltip.installed_module", "Installed modules");
        add("tooltip.not_found_module", "No installed modules");
        add("tooltip.max_count", "Maximum loading capacity is 1");
        add("tooltip.item_description","Shovel + Pickaxe + Axe");
        add("tooltip.forge_energy","Forge Energy");
    }
}
