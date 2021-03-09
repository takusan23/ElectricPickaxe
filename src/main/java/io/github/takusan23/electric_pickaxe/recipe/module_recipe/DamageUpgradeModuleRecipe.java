package io.github.takusan23.electric_pickaxe.recipe.module_recipe;

import io.github.takusan23.electric_pickaxe.item.RegisterItems;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.PotionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 攻撃力上昇モジュールが作成可能かを判断できるクラス
 */
public class DamageUpgradeModuleRecipe implements ModuleRecipeInterface {

    @Override
    public boolean match(List<ItemStack> craftingItemList) {
        // モジュールがないならreturn
        boolean hasModule = craftingItemList.stream().anyMatch(itemStack -> itemStack.getItem() == RegisterItems.BASE_MODULE_ITEM.get());
        if (!hasModule) return false;

        // 攻撃力上昇ポーションを三つあれば作成可能
        int potionCount = 0;
        for (ItemStack itemStack : craftingItemList) {
            List<EffectInstance> potionEffect = PotionUtils.getEffectsFromStack(itemStack);
            for (EffectInstance effect : potionEffect) {
                if (effect.getPotion() == Effects.STRENGTH) {
                    potionCount++;
                }
            }
        }

        // 攻撃力上昇モジュールが作成可能ならtrue
        return potionCount == 3;
    }

    @Override
    public ItemStack getResultItem() {
        return new ItemStack(RegisterItems.DAMAGE_UPGRADE_MODULE_ITEM.get());
    }

    @Override
    public List<ItemStack> getNeedItemList() {
        // ポーション
        ItemStack potion = new ItemStack(Items.POTION);
        EffectInstance effectInstance = new EffectInstance(Effects.STRENGTH);
        // ポーション効果付与
        PotionUtils.appendEffects(potion, new ArrayList<EffectInstance>() {{
            add(effectInstance);
        }});

        // 材料の配列
        ArrayList<ItemStack> itemStackArrayList = new ArrayList<>();
        itemStackArrayList.add(new ItemStack(RegisterItems.BASE_MODULE_ITEM.get()));
        itemStackArrayList.add(potion);
        itemStackArrayList.add(potion);
        itemStackArrayList.add(potion);

        return itemStackArrayList;
    }
}
