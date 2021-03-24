package io.github.takusan23.electric_pickaxe.gui;

import io.github.takusan23.electric_pickaxe.item.ModulePickaxeItem;
import io.github.takusan23.electric_pickaxe.item.RegisterItems;
import io.github.takusan23.electric_pickaxe.network.PacketHandler;
import io.github.takusan23.electric_pickaxe.network.SettingScreenPacket;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fml.network.PacketDistributor;

/**
 * GUI表示の際に使うコンテナ的な何か
 * <p>
 * AndroidでいうとViewModelみたいな（GUIがActivityなら）
 */
public class SettingScreenContainer extends Container {

    private PlayerInventory playerInventory;

    /**
     * Forge登録用にこのコンストラクタが必要
     */
    public SettingScreenContainer(int id, PlayerInventory playerInventory) {
        super(RegisterContainer.SETTING_SCREEN_CONTAINER_CONTAINER_TYPE.get(), id);
        this.playerInventory = playerInventory;
    }


    /**
     * このコンテナを利用できるかどうか
     */
    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }

    /**
     * ModulePickaxeのItemStackを返す。ModulePickaxeを持ってるときじゃないとGUI開かないので多分これでいける
     */
    public ItemStack getModulePickaxeItemStack() {
        return playerInventory.player.getHeldItemMainhand();
    }

    /**
     * ModulePickaxeを返す
     */
    public ModulePickaxeItem getModulePickaxeItem() {
        return ((ModulePickaxeItem) getModulePickaxeItemStack().getItem());
    }

    /**
     * サーバーと同期する
     *
     * サーバーと同期しないと多分NBTの保存ができてない。
     * */
    public void syncServer(ItemStack itemStack){
        PacketHandler.INSTANCE.sendToServer(new SettingScreenPacket(itemStack));
    }

}
