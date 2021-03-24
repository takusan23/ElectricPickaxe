package io.github.takusan23.electric_pickaxe.network;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * なんかクライアント側の処理をサーバーと同期するのに使うらしい
 */
public class SettingScreenPacket {

    private ItemStack itemStack;

    /**
     * @param itemStack 置き換えるElectric Pickaxe。
     */
    public SettingScreenPacket(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    /**
     * データを送るときに使う？
     */
    public static void encode(SettingScreenPacket settingScreenPacket, PacketBuffer packetBuffer) {
        packetBuffer.writeItemStack(settingScreenPacket.itemStack);
    }

    /**
     * データをもらう時に使う？
     */
    public static SettingScreenPacket decode(PacketBuffer packetBuffer) {
        return new SettingScreenPacket(packetBuffer.readItemStack());
    }

    /**
     * 送信受け取り（つまり受信）
     */
    public static void handle(SettingScreenPacket settingScreenPacket, Supplier<NetworkEvent.Context> contextSupplier) {
        // 送られてきたItemStack
        ItemStack itemStack = settingScreenPacket.itemStack;
        // 置き換える
        PlayerEntity playerEntity = contextSupplier.get().getSender();
        if (playerEntity != null) {
            // 今持ってるアイテムを置き換える
            playerEntity.setHeldItem(Hand.MAIN_HAND, itemStack);
        }
    }
}