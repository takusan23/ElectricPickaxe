package io.github.takusan23.electric_pickaxe.item;

import io.github.takusan23.electric_pickaxe.data.InstalledModule;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * {@link ElectricPickaxeItem}から電力関係ない実装をこっちにした。
 * <p>
 * もじゅーらーピッケル
 * <p>
 * インストール済みモジュールはNBTで管理して、KeyにモジュールのアイテムID（レジストリ名？）（String）、Valueにレベルが入っています（Int）。
 */
public class ModulePickaxeItem extends PickaxeItem {

    public ModulePickaxeItem(IItemTier tier, int attackDamageIn, float attackSpeedIn, Properties builder) {
        super(tier, attackDamageIn, attackSpeedIn, builder);
    }

    /**
     * 右クリックでエンチャント内容を切り替える処理など。
     * <p>
     * onUseItemとの違いはブロックに向かってクリックしているか
     */
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemStack = playerIn.getHeldItem(handIn);

        // エンチャントモジュール
        applySilkFortuneModule(playerIn, itemStack);

        return ActionResult.resultSuccess(itemStack);
    }

    /**
     * 範囲攻撃モジュール
     * */
    public void rangeDamage(ItemStack stack, LivingEntity target, LivingEntity attacker) {

        // 範囲
        double rangeValue = 10D;

        // PlayerEntity.java 参照
        float damage = getAttackDamage();
        for (LivingEntity livingentity : attacker.world.getEntitiesWithinAABB(LivingEntity.class, target.getBoundingBox().grow(rangeValue, 0.25D, rangeValue))) {
            // アーマースタンドを攻撃しないようになど
            if (livingentity != target && !attacker.isOnSameTeam(livingentity) && (!(livingentity instanceof ArmorStandEntity) || !((ArmorStandEntity) livingentity).hasMarker())) {
                // 敵対MOBのみ
                if (livingentity instanceof MonsterEntity||livingentity instanceof SlimeEntity) {
                    livingentity.applyKnockback(0.4F, (double) MathHelper.sin(attacker.rotationYaw * ((float) Math.PI / 180F)), (double) (-MathHelper.cos(attacker.rotationYaw * ((float) Math.PI / 180F))));
                    livingentity.attackEntityFrom(DamageSource.causePlayerDamage((PlayerEntity) attacker), damage);
                }
            }
        }
        attacker.world.playSound((PlayerEntity) null, attacker.getPosX(), attacker.getPosY(), attacker.getPosZ(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, attacker.getSoundCategory(), 1.0F, 1.0F);
        ((PlayerEntity) attacker).spawnSweepParticles();
    }

    /**
     * シルクタッチ、幸運エンチャントのモジュールを適用する
     */
    private void applySilkFortuneModule(PlayerEntity playerEntity, ItemStack itemStack) {
        // モジュールがあるときのみ
        if (isCheckInstalledModule(itemStack, RegisterItems.SILK_TOUCH_FORTUNE_MODULE_ITEM.get().getRegistryNameString())) {
            // しゃがんでいるとき
            if (playerEntity.isSneaking()) {
                // どっちのエンチャントがついているのか
                boolean isSilkTouch = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, itemStack) == 1;
                // エンチャントをはがす
                itemStack.removeChildTag("Enchantments");
                if (isSilkTouch) {
                    // Fortuneを付与
                    itemStack.addEnchantment(Enchantments.FORTUNE, 3);
                } else {
                    // SilkTouchを付与
                    itemStack.addEnchantment(Enchantments.SILK_TOUCH, 1);
                }
            }
        }
    }

    /**
     * ツールチップ追加
     */
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);

        // インストール済み配列
        List<InstalledModule> installedModuleList = getInstalledModuleList(stack);

        if (installedModuleList.isEmpty()) {
            // ない場合は無いって出す
            addToolTip(stack, worldIn, tooltip, flagIn, "--- Not found module ---", "#ffffff");
        } else {
            // インストール済み表示する
            addToolTip(stack, worldIn, tooltip, flagIn, "--- Installed Module ---", "#ffffff");
            // インストール済みモジュールをずらーっと
            installedModuleList.forEach(installedModule -> {
                String toolTipText = String.format("%S / Lv = %d", installedModule.getModuleRegistryId(), installedModule.getLevel());
                addToolTip(stack, worldIn, tooltip, flagIn, toolTipText, "#ffff8b");
            });
            addToolTip(stack, worldIn, tooltip, flagIn, "---", "#ffffff");
        }
    }

    /**
     * ツールチップを追加する。長いのでまとめた
     */
    public void addToolTip(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn, String text, String textColor) {
        StringTextComponent batteryLevelTextComponent = new StringTextComponent(text);
        batteryLevelTextComponent.setStyle(Style.EMPTY.setColor(Color.fromHex(textColor)));
        tooltip.add(batteryLevelTextComponent);
    }

    /**
     * インストール済みモジュールを配列にして返す
     *
     * @param itemStack {@link ModulePickaxeItem}
     * @return {@link InstalledModule}の配列
     */
    public List<InstalledModule> getInstalledModuleList(ItemStack itemStack) {
        CompoundNBT compoundNBT = itemStack.getOrCreateTag();
        // やっぱ、Stream API、、、最高やな！
        List installedModuleList = compoundNBT.keySet()
                .stream()
                .filter(s -> s.contains("_module")) // moduleの文字列が含まれている
                .map(s -> new InstalledModule(s, compoundNBT.getInt(s))) // データクラスへ
                .collect(Collectors.toList());
        return installedModuleList;
    }

    /**
     * モジュールをインストールする。二回目以降はレベルが上がる
     *
     * @param pickaxe    {@link ModulePickaxeItem}
     * @param moduleItem 追加するモジュールのアイテム
     */
    public void addModule(ItemStack pickaxe, BaseModuleItem moduleItem) {
        // NBT
        CompoundNBT compoundNBT = pickaxe.getOrCreateTag();
        // インストール済みのモジュール
        List<InstalledModule> installedModuleList = new ArrayList<>(getInstalledModuleList(pickaxe));
        // これから導入するモジュールのID
        String key = moduleItem.getRegistryNameString();
        // 導入済みかどうか。導入済みなら今のレベル。無ければ-1
        int level = getModuleLevel(pickaxe, moduleItem.getRegistryNameString());
        // 追加する
        if (level == -1) {
            installedModuleList.add(new InstalledModule(key, 1));
        } else {
            // レベルアップ。位置を出す
            InstalledModule module = installedModuleList
                    .stream()
                    .filter(installedModule -> installedModule.getModuleRegistryId().equals(moduleItem.getRegistryNameString()))
                    .findFirst().get();
            int index = installedModuleList.indexOf(module);
            // 更新
            installedModuleList.set(index, new InstalledModule(key, level + 1));
        }
        //NBTへ
        installedModuleList.forEach(installedModule -> {
            // KeyにモジュールのアイテムID、Valueはモジュールのレベル
            compoundNBT.putInt(installedModule.getModuleRegistryId(), installedModule.getLevel());
        });
        // NBT再セット
        pickaxe.setTag(compoundNBT);
    }

    /**
     * 指定したモジュールのレベルを返す
     *
     * @param pickaxe            {@link ModulePickaxeItem}
     * @param moduleRegistryName 追加するモジュールのアイテムID
     * @return レベル。なければ -1
     */
    public int getModuleLevel(ItemStack pickaxe, String moduleRegistryName) {
        return getInstalledModuleList(pickaxe)
                .stream()
                .filter(installedModule -> installedModule.getModuleRegistryId().equals(moduleRegistryName))
                .map(installedModule -> installedModule.getLevel())
                .findFirst().orElse(-1); // 初めての場合は -1 を返す
    }

    /**
     * moduleRegistryName　がインストール済みかどうかを返す。
     *
     * @param itemStack          {@link ModulePickaxeItem}
     * @param moduleRegistryName 追加するモジュールのアイテムID
     * @return インストール済みならtrue
     */
    public boolean isCheckInstalledModule(ItemStack itemStack, String moduleRegistryName) {
        return getModuleLevel(itemStack, moduleRegistryName) != -1;
    }

}