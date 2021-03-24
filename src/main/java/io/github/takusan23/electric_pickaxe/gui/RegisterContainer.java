package io.github.takusan23.electric_pickaxe.gui;

import io.github.takusan23.electric_pickaxe.ElectricPickaxe;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * ContainerをForgeに登録する何か
 */
public class RegisterContainer {

    /**
     * ContainerをForgeに登録する
     */
    private static final DeferredRegister<ContainerType<?>> CONTAINER_TYPE = DeferredRegister.create(ForgeRegistries.CONTAINERS, ElectricPickaxe.MOD_ID);

    /**
     * 設定画面GUI表示に使うContainer
     */
    public static final RegistryObject<ContainerType<SettingScreenContainer>> SETTING_SCREEN_CONTAINER_CONTAINER_TYPE = CONTAINER_TYPE.register("electric_pickaxe_container", () -> new ContainerType<>(SettingScreenContainer::new));

    /**
     * Forgeに登録する
     */
    public static void register() {
        CONTAINER_TYPE.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

}
