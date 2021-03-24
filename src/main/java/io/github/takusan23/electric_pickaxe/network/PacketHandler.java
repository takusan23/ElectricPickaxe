package io.github.takusan23.electric_pickaxe.network;

import io.github.takusan23.electric_pickaxe.ElectricPickaxe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.Optional;
import java.util.function.Supplier;

public class PacketHandler {

    private static final String PROTOCOL_VERSION = "1";

    /**
     * サーバーとの架け橋
     */
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(ElectricPickaxe.MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    /**
     * Forgeにパケットシステムを登録する。これを使うことでクライアント側の処理（GUIなど）をサーバーと同期できる。
     * <p>
     * 受け取りは {@link SettingScreenPacket#handle(SettingScreenPacket, Supplier)} でできます
     */
    public static void register() {
        int id = 0;

        INSTANCE.registerMessage(id++, SettingScreenPacket.class, SettingScreenPacket::encode, SettingScreenPacket::decode, SettingScreenPacket::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
    }

}
