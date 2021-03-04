package io.github.takusan23.electric_pickaxe.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
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

    /**
     * @param properties クリエイティブタブの設定など
     */
    public BaseModuleItem(Properties properties, String description) {
        super(properties);
        this.description = description;
    }

    /**
     * レジストリ名を返す
     */
    public String getRegistryNameString() {
        return getItem().getRegistryName().getPath();
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        if (description != null) {
            tooltip.add(new StringTextComponent(description));
        }
    }
}
