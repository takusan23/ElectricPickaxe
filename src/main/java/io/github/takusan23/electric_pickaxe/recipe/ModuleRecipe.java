package io.github.takusan23.electric_pickaxe.recipe;

import io.github.takusan23.electric_pickaxe.item.BaseModuleItem;
import io.github.takusan23.electric_pickaxe.item.RegisterItems;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.potion.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 一部のモジュールのレシピをはNBTに依存してるので自前でコードを書く。
 * <p>
 * NBTタグなんかは扱えないので
 */
public class ModuleRecipe extends SpecialRecipe {

    public ModuleRecipe(ResourceLocation idIn) {
        super(idIn);
    }

    /**
     * とりあえず {@link BaseModuleItem} があればおｋ
     */
    @Override
    public boolean matches(CraftingInventory inv, World worldIn) {
        boolean hasModule = false;

        for (int j = 0; j < inv.getSizeInventory(); ++j) {
            ItemStack itemstack1 = inv.getStackInSlot(j);
            if (!itemstack1.isEmpty()) {
                if (itemstack1.getItem() == RegisterItems.BASE_MODULE_ITEM.get()) {
                    hasModule = true;
                }
            }
        }

        return hasModule;
    }

    /**
     * 完成品を返す
     */
    @Override
    public ItemStack getCraftingResult(CraftingInventory inv) {

        ItemStack moduleItem = null;
        // モジュール以外のアイテム
        ArrayList<ItemStack> craftingItemList = new ArrayList<>();

        // ElectricPickaxeとモジュールを見つける
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack itemStack = inv.getStackInSlot(i);
            if (itemStack.getItem() == RegisterItems.BASE_MODULE_ITEM.get()) {
                moduleItem = itemStack;
            }
            if (itemStack.getItem() != Items.AIR) {
                craftingItemList.add(itemStack);
            }
        }

        // 無ければreturn
        if (moduleItem == null || craftingItemList.isEmpty()) return ItemStack.EMPTY;

        boolean hasSilkTouch = false;
        boolean hasFortune = false;
        // 幸運とシルクタッチがあれば
        for (ItemStack itemStack : craftingItemList) {
            if (EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, itemStack) == 1) {
                hasSilkTouch = true;
            }
            if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, itemStack) == 3) {
                hasFortune = true;
            }
        }
        // シルクタッチ、幸運モジュールを返す
        if (hasSilkTouch && hasFortune) {
            return new ItemStack(RegisterItems.SILK_TOUCH_FORTUNE_MODULE_ITEM.get());
        }


        // 攻撃力上昇ポーションを三つあれば作成可能
        int potionCount = 0;
        for (ItemStack itemStack : craftingItemList) {
            List<EffectInstance> potionEffect = PotionUtils.getEffectsFromStack(itemStack);
            System.out.println(potionEffect);
            for (EffectInstance effect : potionEffect) {
                if (effect.getPotion() == Effects.STRENGTH) {
                    potionCount++;
                }
            }
        }
        // 攻撃力上昇モジュールを返す
        if (potionCount == 3) {
            return new ItemStack(RegisterItems.DAMAGE_UPGRADE_MODULE_ITEM.get());
        }


        return ItemStack.EMPTY;
    }

    @Override
    public boolean canFit(int width, int height) {
        return true;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return RegisterRecipe.CRAFTING_MODULE_RECIPE.get();
    }
}
