# network
Electric Pickaxeの設定画面を反映させるのに必要。  

## は？
Electric PickaxeのGUIはクライアント側のみ動く。GUIでエンチャント切り替えをしてもサーバーに反映されない（保存されないか合わなくなる） 。  
というわけで、サーバーと同期しないといけないのでそのため。

## サーバーへ送信
引数の`ItemStack`は置き換えるItemStackにしてね

```java
PacketHandler.INSTANCE.sendToServer(new SettingScreenPacket(itemStack));
```

## クライアントから来たItemStackの受け取り
`SettingScreenContainer.java`

```java
public class SettingScreenPacket {

    private ItemStack itemStack;
    
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
```

あってるかは知らん