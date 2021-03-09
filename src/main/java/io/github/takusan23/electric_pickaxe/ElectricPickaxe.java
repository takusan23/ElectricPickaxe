package io.github.takusan23.electric_pickaxe;

import io.github.takusan23.electric_pickaxe.generator.EnLanguageGenerator;
import io.github.takusan23.electric_pickaxe.generator.JaLanguageGenerator;
import io.github.takusan23.electric_pickaxe.generator.RecipeGenerator;
import io.github.takusan23.electric_pickaxe.item.RegisterItems;
import io.github.takusan23.electric_pickaxe.recipe.RegisterRecipe;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/**
 * エントリーポイント？
 */
@Mod(ElectricPickaxe.MOD_ID)
public class ElectricPickaxe {

    /**
     * MODのID
     */
    public static final String MOD_ID = "electric_pickaxe";

    /**
     * コンストラクタ
     */
    public ElectricPickaxe() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        // アイテム登録
        RegisterItems.register(modEventBus);
        // レシピシステム登録。NBTタグを扱うため
        RegisterRecipe.register();
        // JSONファイルを生成するなにか
        modEventBus.addListener(this::registerProviders);

    }

    /**
     * 各JSONファイルを生成してくれる
     */
    private void registerProviders(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        if (event.includeClient()) {
            /**
             * 翻訳JSONファイル生成
             * */
            generator.addProvider(new EnLanguageGenerator(generator, MOD_ID));
            generator.addProvider(new JaLanguageGenerator(generator, MOD_ID));
        }
        if (event.includeServer()) {
            /**
             * レシピ生成クラス(レシピJSON生成)
             *
             * generatedフォルダに吐き出されるらしい
             * */
            generator.addProvider(new RecipeGenerator(generator));
        }
    }
}
