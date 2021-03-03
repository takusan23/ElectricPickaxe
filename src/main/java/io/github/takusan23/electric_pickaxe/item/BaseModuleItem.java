package io.github.takusan23.electric_pickaxe.item;

import net.minecraft.item.Item;

/**
 * モジュールのベースとなるアイテム
 */
public class BaseModuleItem extends Item {

    /**
     * @param properties クリエイティブタブの設定など
     * */
    public BaseModuleItem(Properties properties) {
        super(properties);
    }

    /**
     * レジストリ名を返す
     */
    public String getRegistryNameString() {
        return getItem().getRegistryName().getPath();
    }

}
