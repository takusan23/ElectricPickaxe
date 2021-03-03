package io.github.takusan23.electric_pickaxe.item;

import io.github.takusan23.electric_pickaxe.ElectricPickaxe;
import io.github.takusan23.electric_pickaxe.item.ElectricPickaxeCreativeTab;
import io.github.takusan23.electric_pickaxe.item.ElectricPickaxeItem;
import io.github.takusan23.electric_pickaxe.item.BaseModuleItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemTier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * アイテム追加用クラス
 * <p>
 * 追加するアイテムをここで定義する
 */
public class RegisterItems {

    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ElectricPickaxe.MOD_ID);

    /**
     * クリエイティブタブ。
     * */
    public static final ElectricPickaxeCreativeTab CREATIVE_TAB = new ElectricPickaxeCreativeTab();

    /**
     * 本命。充電式つるはし
     */
    public static final RegistryObject<ElectricPickaxeItem> ELECTRIC_PICKAXE_ITEM = ITEMS.register("electric_pickaxe", () -> new ElectricPickaxeItem(ItemTier.DIAMOND, 5, 0, new Item.Properties().group(CREATIVE_TAB)));

    /**
     * ベースになるモジュール。こいつは特に一緒にクラフトしてインストールすることはないのでItemクラス
     * */
    public static final RegistryObject<Item> BASE_MODULE_ITEM = ITEMS.register("base_module", () -> new Item(new Item.Properties().group(CREATIVE_TAB)));

    /**
     * シルクタッチ、幸運モジュール
     */
    public static final RegistryObject<BaseModuleItem> SILK_TOUCH_FORTUNE_MODULE_ITEM = ITEMS.register("silk_touch_fortune_module", () -> new BaseModuleItem(new Item.Properties().group(CREATIVE_TAB)));

    /**
     * アイテムを追加する
     * <p>
     * {@link ElectricPickaxe}クラスから呼ばれる
     */
    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }
}
