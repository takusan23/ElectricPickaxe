package io.github.takusan23.electric_pickaxe.generator;

import io.github.takusan23.electric_pickaxe.item.RegisterItems;
import net.minecraft.data.*;
import net.minecraft.item.Items;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Consumer;

/**
 * レシピJSONを生成してくれるクラス
 */
@ParametersAreNonnullByDefault
public class RecipeGenerator extends RecipeProvider {

    public RecipeGenerator(DataGenerator generatorIn) {
        super(generatorIn);
    }

    /**
     * 追加するレシピをここに書いていく
     * <p>
     * あと、key()の第一引数は「文字列」ではなく「文字（character）」です。シングルクオーテーション
     */
    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {

        // ElectricPickaxeのレシピ
        ShapedRecipeBuilder.shapedRecipe(RegisterItems.ELECTRIC_PICKAXE_ITEM.get(), 1)
                .key('S', Items.DIAMOND_SHOVEL)
                .key('P', Items.DIAMOND_PICKAXE)
                .key('A', Items.DIAMOND_AXE)
                .key('R', Items.REDSTONE_BLOCK)
                .key('I', Items.IRON_INGOT)
                .patternLine("SPA")
                .patternLine("IRI")
                .patternLine("IRI")
                .addCriterion("has_diamond_pickaxe", hasItem(Items.DIAMOND_PICKAXE)) // レシピ解放？
                .build(consumer);

        // BaseModule。不定形
        ShapelessRecipeBuilder.shapelessRecipe(RegisterItems.BASE_MODULE_ITEM.get(), 3)
                .addIngredient(Items.IRON_INGOT, 3)
                .addIngredient(Items.DIAMOND, 1)
                .addIngredient(Items.REDSTONE, 3)
                .addCriterion("has_redstone", hasItem(Items.DIAMOND))
                .build(consumer);

        // BatteryUpgrade
        ShapedRecipeBuilder.shapedRecipe(RegisterItems.BATTERY_UPGRADE_MODULE_ITEM.get(), 1)
                .key('R', Items.REDSTONE)
                .key('M', RegisterItems.BASE_MODULE_ITEM.get())
                .patternLine("RRR")
                .patternLine("RMR")
                .patternLine("RRR")
                .addCriterion("has_base_module_item", hasItem(RegisterItems.BASE_MODULE_ITEM.get())) // レシピ解放？
                .build(consumer);

        // RangeAttackModule
        ShapelessRecipeBuilder.shapelessRecipe(RegisterItems.RANGE_ATTACK_MODULE_ITEM.get(), 1)
                .addIngredient(RegisterItems.BASE_MODULE_ITEM.get(), 1)
                .addIngredient(Items.WOODEN_SWORD, 1)
                .addIngredient(Items.STONE_SWORD, 1)
                .addIngredient(Items.IRON_SWORD, 1)
                .addIngredient(Items.GOLDEN_SWORD, 1)
                .addIngredient(Items.DIAMOND_SWORD, 1)
                .addIngredient(Items.NETHERITE_SWORD, 1)
                .addCriterion("has_base_module_item", hasItem(RegisterItems.BASE_MODULE_ITEM.get())) // レシピ解放？
                .build(consumer);


    }
}
