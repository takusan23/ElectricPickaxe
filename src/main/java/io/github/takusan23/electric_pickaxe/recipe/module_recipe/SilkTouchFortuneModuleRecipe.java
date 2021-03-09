package io.github.takusan23.electric_pickaxe.recipe.module_recipe;

import io.github.takusan23.electric_pickaxe.item.RegisterItems;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * シルクタッチ、幸運モジュールの作り方を {@link io.github.takusan23.electric_pickaxe.recipe.ModuleRecipe}とか {@link io.github.takusan23.electric_pickaxe.JEIPlugin}とかに
 * <p>
 * 提供するときに使うクラス。
 */
public class SilkTouchFortuneModuleRecipe implements ModuleRecipeInterface {

    /**
     * 引数の配列に渡したアイテムで作成可能かどうか
     *
     * @param craftingItemList 作業台に乗ってるアイテム
     * @return 作成可能ならtrue
     */
    @Override
    public boolean match(List<ItemStack> craftingItemList) {
        // モジュールがないならreturn
        boolean hasModule = craftingItemList.stream().anyMatch(itemStack -> itemStack.getItem() == RegisterItems.BASE_MODULE_ITEM.get());
        if (!hasModule) return false;

        boolean hasSilkTouch = false;
        boolean hasFortune = false;
        // 幸運とシルクタッチがあれば
        for (ItemStack itemStack : craftingItemList) {
            if (EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, itemStack) == 1) {
                hasSilkTouch = true;
            }
            if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, itemStack) == 3) {
                hasFortune = true;
            }
        }
        // シルクタッチ、幸運モジュールがある場合はtrueを返す
        return hasSilkTouch && hasFortune;
    }

    /**
     * 完成品を返す
     *
     * @return 完成品
     */
    @Override
    public ItemStack getResultItem() {
        return new ItemStack(RegisterItems.SILK_TOUCH_FORTUNE_MODULE_ITEM.get());
    }

    /**
     * 作成に必要なアイテム。JEIに提供するため
     *
     * @return 必要なアイテム
     */
    @Override
    public List<ItemStack> getNeedItemList() {
        // エンチャントつるはし
        ItemStack silkTouchPickaxe = new ItemStack(Items.STONE_PICKAXE);
        EnchantmentHelper.setEnchantments(new HashMap<Enchantment, Integer>() {
            {
                put(Enchantments.SILK_TOUCH, 1);
            }
        }, silkTouchPickaxe);
        ItemStack fortunePickaxe = new ItemStack(Items.STONE_PICKAXE);
        EnchantmentHelper.setEnchantments(new HashMap<Enchantment, Integer>() {
            {
                put(Enchantments.FORTUNE, 3);
            }
        }, fortunePickaxe);

        // 材料の配列
        ArrayList<ItemStack> itemStackList = new ArrayList<ItemStack>() {
            {
                add(new ItemStack(RegisterItems.BASE_MODULE_ITEM.get()));
                add(silkTouchPickaxe);
                add(fortunePickaxe);
            }
        };
        return itemStackList;
    }
}
