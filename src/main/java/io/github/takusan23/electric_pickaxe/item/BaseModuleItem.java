package io.github.takusan23.electric_pickaxe.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.*;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

/**
 * モジュールのベースとなるアイテム
 */
public class BaseModuleItem extends Item {

    /**
     * ToolTipの中身
     */
    private String description = null;

    private boolean isMaxCountOne = false;

    /**
     * @param properties クリエイティブタブの設定など
     */
    public BaseModuleItem(Properties properties, String description) {
        super(properties);
        this.description = description;
    }

    /**
     * 最大搭載量が1の場合のコンストラクタ
     */
    public BaseModuleItem(Properties properties, boolean isMaxCountOne) {
        super(properties);
        this.isMaxCountOne = isMaxCountOne;
    }


    /**
     * レジストリ名を返す
     */
    public String getRegistryNameString() {
        return getItem().getTranslationKey();
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        if (description != null) {
            tooltip.add(new StringTextComponent(description));
        }
        // 最大搭載量が1の場合
        if (isMaxCountOne) {
            String localizeText = new TranslationTextComponent("tooltip.max_count").getString();
            StringTextComponent maxCountOneText = new StringTextComponent(localizeText);
            maxCountOneText.setStyle(Style.EMPTY.setColor(Color.fromHex("#FF0000")));
            tooltip.add(maxCountOneText);
        }
    }
}
