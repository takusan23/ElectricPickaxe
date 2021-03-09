package io.github.takusan23.electric_pickaxe.recipe;

import io.github.takusan23.electric_pickaxe.recipe.module_recipe.DamageUpgradeModuleRecipe;
import io.github.takusan23.electric_pickaxe.recipe.module_recipe.SilkTouchFortuneModuleRecipe;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.ArrayList;

/**
 * 一部のモジュールのレシピをはNBTに依存してるので自前でコードを書く。
 * <p>
 * NBTタグなんかは扱えないので
 */
public class ModuleRecipe extends SpecialRecipe {

    /**
     * シルクタッチ、幸運モジュールのレシピ関係のクラス（完成品を返すメソッドとかがある）
     */
    public static final SilkTouchFortuneModuleRecipe SILK_TOUCH_FORTUNE_MODULE_RECIPE = new SilkTouchFortuneModuleRecipe();

    /**
     * 攻撃力上昇モジュールのレシピ関係のクラス
     */
    public static final DamageUpgradeModuleRecipe DAMAGE_UPGRADE_MODULE_RECIPE = new DamageUpgradeModuleRecipe();

    public ModuleRecipe(ResourceLocation idIn) {
        super(idIn);
    }

    /**
     * クラフト可能かどうかを返す
     */
    @Override
    public boolean matches(CraftingInventory inv, World worldIn) {
        // モジュール以外のアイテム
        ArrayList<ItemStack> craftingItemList = new ArrayList<>();

        for (int j = 0; j < inv.getSizeInventory(); ++j) {
            ItemStack itemstack1 = inv.getStackInSlot(j);
            if (itemstack1.getItem() != Items.AIR) {
                craftingItemList.add(itemstack1);
            }
        }

        // シルクタッチ、幸運モジュールが作成可能なら
        if (SILK_TOUCH_FORTUNE_MODULE_RECIPE.match(craftingItemList)) {
            return true;
        }

        // 攻撃力上昇ポーションが三つあるか
        if (DAMAGE_UPGRADE_MODULE_RECIPE.match(craftingItemList)) {
            return true;
        }

        return false;
    }

    /**
     * 完成品を返す
     */
    @Override
    public ItemStack getCraftingResult(CraftingInventory inv) {

        // モジュール以外のアイテム
        ArrayList<ItemStack> craftingItemList = new ArrayList<>();

        for (int j = 0; j < inv.getSizeInventory(); ++j) {
            ItemStack itemstack1 = inv.getStackInSlot(j);
            if (itemstack1.getItem() != Items.AIR) {
                craftingItemList.add(itemstack1);
            }
        }

        // シルクタッチ、幸運モジュールが作成可能なら
        if (SILK_TOUCH_FORTUNE_MODULE_RECIPE.match(craftingItemList)) {
            return SILK_TOUCH_FORTUNE_MODULE_RECIPE.getResultItem();
        }

        // 攻撃力上昇ポーションを三つあるか
        if (DAMAGE_UPGRADE_MODULE_RECIPE.match(craftingItemList)) {
            return DAMAGE_UPGRADE_MODULE_RECIPE.getResultItem();
        }

        return ItemStack.EMPTY;
    }

    @Override
    public boolean canFit(int width, int height) {
        return true;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return RegisterRecipe.CRAFTING_MODULE_RECIPE.get();
    }
}
