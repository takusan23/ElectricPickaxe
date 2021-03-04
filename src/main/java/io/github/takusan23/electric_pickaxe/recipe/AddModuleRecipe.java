package io.github.takusan23.electric_pickaxe.recipe;

import io.github.takusan23.electric_pickaxe.item.BaseModuleItem;
import io.github.takusan23.electric_pickaxe.item.ElectricPickaxeItem;
import io.github.takusan23.electric_pickaxe.item.RegisterItems;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.ArrayList;

/**
 * レシピシステムの実装
 */
public class AddModuleRecipe extends SpecialRecipe {

    public AddModuleRecipe(ResourceLocation idIn) {
        super(idIn);
    }

    /**
     * 作業台に {@link io.github.takusan23.electric_pickaxe.item.ElectricPickaxeItem} と {@link BaseModuleItem} があるかどうか
     */
    @Override
    public boolean matches(CraftingInventory inv, World worldIn) {
        boolean hasElectricPickaxe = false;
        boolean hasModule = false;

        for (int j = 0; j < inv.getSizeInventory(); ++j) {
            ItemStack itemstack1 = inv.getStackInSlot(j);
            if (!itemstack1.isEmpty()) {
                if (itemstack1.getItem() == RegisterItems.ELECTRIC_PICKAXE_ITEM.get()) {
                    // ElectricPickaxeがあるか
                    hasElectricPickaxe = true;
                } else if (itemstack1.getItem() instanceof BaseModuleItem) {
                    // モジュールがあるか
                    hasModule = true;
                } else if (itemstack1.getItem() != Items.AIR) {
                    // その他上記＋空気以外が入ってきた場合はreturn
                    return false;
                }
            }
        }

        return hasElectricPickaxe && hasModule;
    }

    /**
     * 作成できるアイテムを返す。
     * <p>
     * NBTタグをつけてアイテムを返す
     */
    @Override
    public ItemStack getCraftingResult(CraftingInventory inv) {
        ItemStack electricPickaxe = null;
        // 複数インストール機能
        ArrayList<ItemStack> moduleItemArrayList = new ArrayList<>();

        // ElectricPickaxeとモジュールを見つける
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack itemStack = inv.getStackInSlot(i);
            if (itemStack.getItem() == RegisterItems.ELECTRIC_PICKAXE_ITEM.get()) {
                electricPickaxe = itemStack;
            }
            if (itemStack.getItem() instanceof BaseModuleItem) {
                moduleItemArrayList.add(itemStack);
            }
        }

        // なければreturn
        if (electricPickaxe == null || moduleItemArrayList.isEmpty()) return null;

        // これないとこのメソッドが呼ばれる度にクラフトしてないのにレベルが上がっていってしまう。
        electricPickaxe = electricPickaxe.copy();

        // インストールメソッドを呼ぶ
        ElectricPickaxeItem electricPickaxeItem = (ElectricPickaxeItem) electricPickaxe.getItem();

        ItemStack finalElectricPickaxe = electricPickaxe;
        moduleItemArrayList.forEach(itemStack -> {
            BaseModuleItem baseModuleItem = (BaseModuleItem) itemStack.getItem();
            electricPickaxeItem.addModule(finalElectricPickaxe, baseModuleItem);
        });

        // 返す。コピーを
        return electricPickaxe;
    }

    /**
     * わからん。なぞ
     */
    @Override
    public boolean canFit(int width, int height) {
        return true;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return RegisterRecipe.CRAFTING_ADD_MODULE.get();
    }

}


