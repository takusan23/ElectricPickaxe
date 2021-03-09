package io.github.takusan23.electric_pickaxe.recipe.module_recipe;

import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * {@link SilkTouchFortuneModuleRecipe}なんかのクラスの元になってる。
 * <p>
 * 完成品を返すメソッドなんかを定義してほしいので
 */
public interface ModuleRecipeInterface {

    /**
     * 引数のアイテムリストで材料が足りているか。作成可能な場合はtrueを返して
     */
    boolean match(List<ItemStack> craftingItemList);

    /**
     * クラフト完成品を返してね
     */
    ItemStack getResultItem();

    /**
     * 必要な材料を返してね
     */
    List<ItemStack> getNeedItemList();
}
