package io.github.takusan23.electric_pickaxe;

import io.github.takusan23.electric_pickaxe.recipe.ModuleRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * JEIへ独自レシピ（{@link ModuleRecipe}）を教えるためのクラス
 * */
@JeiPlugin
public class JEIPlugin implements IModPlugin {
    /**
     * レシピを教えるときに使う
     */
    ResourceLocation location = new ResourceLocation(ElectricPickaxe.MOD_ID, "module_recipe");

    /**
     * わからん
     */
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(ElectricPickaxe.MOD_ID, "jei_plugin");
    }

    /**
     * JEIに表示させるレシピをここで登録する
     */
    @Override
    public void registerRecipes(IRecipeRegistration registration) {

        // レシピ配列
        ArrayList<IRecipe> recipeArrayList = new ArrayList<IRecipe>(){
            {
                add(getSilkTouchFortuneRecipe());
                add(getDamageUpgrade());
            }
        };

        registration.addRecipes(recipeArrayList, VanillaRecipeCategoryUid.CRAFTING);
    }

    /**
     * 攻撃力上昇モジュールのレシピを返す
     */
    private ShapelessRecipe getDamageUpgrade() {
        return new ShapelessRecipe(location,
                "",
                ModuleRecipe.DAMAGE_UPGRADE_MODULE_RECIPE.getResultItem(),
                itemStackListToNonNullList(ModuleRecipe.DAMAGE_UPGRADE_MODULE_RECIPE.getNeedItemList())
        );
    }

    /**
     * シルクタッチ、幸運モジュールのレシピを返す
     */
    private ShapelessRecipe getSilkTouchFortuneRecipe() {
        return new ShapelessRecipe(location,
                "",
                ModuleRecipe.SILK_TOUCH_FORTUNE_MODULE_RECIPE.getResultItem(),
                itemStackListToNonNullList(ModuleRecipe.SILK_TOUCH_FORTUNE_MODULE_RECIPE.getNeedItemList())
        );
    }

    /**
     * ItemStack配列をNonNullListに変換する
     * <p>
     * Kotlinの拡張関数ほしい
     */
    private NonNullList<Ingredient> itemStackListToNonNullList(List<ItemStack> itemStackList) {
        NonNullList<Ingredient> itemStackNonNullList = NonNullList.create();
        itemStackList.forEach(itemStack -> itemStackNonNullList.add(Ingredient.fromStacks(itemStack)));
        return itemStackNonNullList;
    }

}
