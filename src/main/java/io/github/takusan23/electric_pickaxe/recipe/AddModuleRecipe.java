package io.github.takusan23.electric_pickaxe.recipe;

import io.github.takusan23.electric_pickaxe.ElectricPickaxe;
import io.github.takusan23.electric_pickaxe.item.BaseModuleItem;
import io.github.takusan23.electric_pickaxe.item.ElectricPickaxeItem;
import io.github.takusan23.electric_pickaxe.item.RegisterItems;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

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

        System.out.println("-------------------------- matches -------------------------------");

        boolean hasElectricPickaxe = false;
        boolean hasModule = false;

        for (int j = 0; j < inv.getSizeInventory(); ++j) {
            ItemStack itemstack1 = inv.getStackInSlot(j);
            if (!itemstack1.isEmpty()) {
                // ElectricPickaxeがあるか
                if (itemstack1.getItem() == RegisterItems.ELECTRIC_PICKAXE_ITEM.get()) {
                    hasElectricPickaxe = true;
                }
                // モジュールがあるか
                if (itemstack1.getItem() instanceof BaseModuleItem) {
                    hasModule = true;
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

        System.out.println("-------------------------- getCraftingResult -------------------------------");


        ItemStack electricPickaxe = null;
        ItemStack moduleItem = null;

        // ElectricPickaxeとモジュールを見つける
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack itemStack = inv.getStackInSlot(i);
            if (itemStack.getItem() == RegisterItems.ELECTRIC_PICKAXE_ITEM.get()) {
                electricPickaxe = itemStack;
            }
            if (itemStack.getItem() instanceof BaseModuleItem) {
                moduleItem = itemStack;
            }
        }

        // なければreturn
        if (electricPickaxe == null || moduleItem == null) return null;

        // インストールメソッドを呼ぶ
        ElectricPickaxeItem electricPickaxeItem = (ElectricPickaxeItem) electricPickaxe.getItem();
        BaseModuleItem baseModuleItem = (BaseModuleItem) moduleItem.getItem();
        electricPickaxeItem.addModule(electricPickaxe, baseModuleItem);

        // 返す。コピーを
        return electricPickaxe.copy();
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


