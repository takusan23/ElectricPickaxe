package io.github.takusan23.electric_pickaxe.recipe;

import io.github.takusan23.electric_pickaxe.ElectricPickaxe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.stream.Collectors;


/**
 * オリジナルレシピシステムを登録する。
 */
public class RegisterRecipe {

    /**
     * レシピ登録
     */
    private static final DeferredRegister<IRecipeSerializer<?>> CRAFTS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, ElectricPickaxe.MOD_ID);

    /**
     * ElectricPickaxeにモジュールを追加するための独自レシピシステム
     */
    public static final RegistryObject<SpecialRecipeSerializer<AddModuleRecipe>> CRAFTING_ADD_MODULE = CRAFTS.register("add_module", () -> new SpecialRecipeSerializer<>(AddModuleRecipe::new));

    /**
     * 各モジュールを作成するレシピ
     */
    public static final RegistryObject<SpecialRecipeSerializer<ModuleRecipe>> CRAFTING_MODULE_RECIPE = CRAFTS.register("module_recipe", () -> new SpecialRecipeSerializer<>(ModuleRecipe::new));

    /**
     * {@link ElectricPickaxe}で呼んで使う
     * */
    public static void register() {
        CRAFTS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

}
