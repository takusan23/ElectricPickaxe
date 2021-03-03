package io.github.takusan23.electric_pickaxe.item;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

/**
 * Electric Pickaxeで使うクリエイティブタブ
 */
public class ElectricPickaxeCreativeTab extends ItemGroup {

    /**
     * super()の中身は内部で使っている模様
     */
    public ElectricPickaxeCreativeTab() {
        super("electric_pickaxe_tab");
    }

    /**
     * クリエイティブタブのアイコン
     */
    @Override
    public ItemStack createIcon() {
        return new ItemStack(RegisterItems.ELECTRIC_PICKAXE_ITEM.get());
    }

}
