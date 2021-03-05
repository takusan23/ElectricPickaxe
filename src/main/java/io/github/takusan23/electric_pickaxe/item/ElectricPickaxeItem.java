package io.github.takusan23.electric_pickaxe.item;

import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.text.NumberFormat;
import java.util.List;

/**
 * 電気（Forge Energy）で動くピッケル。一部のメソッドは {@link ModulePickaxeItem} の方に実装があります。
 * <p>
 * ActuallyAdditions を参考にした（パクった）。電力関連はまったく情報がない。
 * <p>
 * Forge Energy はForgeが実装例を用意（{@link EnergyStorage}）しているのでこれをそのまま使うことでエネルギー関連は終了。
 * <p>
 * 参考コード。ありざいす
 * <p>
 * https://github.com/Ellpeck/ActuallyAdditions/blob/main/src/main/java/de/ellpeck/actuallyadditions/common/items/base/ItemEnergy.java
 * <p>
 * どうやら、EnergyStorage自体には電池残量保持機能はないのでNBTタグに書き込んでおく必要がある模様。
 */
public class ElectricPickaxeItem extends ModulePickaxeItem {

    /**
     * 電気の供給方法がない場合はtrueにすることで右クリックで充電ができます
     */
    private boolean DEBUG_IS_CLICK_CHARGE = true;

    /**
     * コンストラクタ
     */
    public ElectricPickaxeItem(IItemTier tier, int attackDamageIn, float attackSpeedIn, Properties builder) {
        super(tier, attackDamageIn, attackSpeedIn, builder);
    }

    /**
     * エネルギーの残りを表示させる
     */
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        stack.getCapability(CapabilityEnergy.ENERGY).ifPresent(cap -> {
            NumberFormat format = NumberFormat.getInstance();
            // ツールの説明
            StringTextComponent toolDescriptionTextComponent = new StringTextComponent(getLocalizationText("tooltip.item_description"));
            tooltip.add(toolDescriptionTextComponent);

            // 電池残量
            String localizeBatteryLevelText = getLocalizationText("tooltip.forge_energy");
            int percent = (int) ((cap.getEnergyStored() / new Float(cap.getMaxEnergyStored())) * 100);
            StringTextComponent batteryLevelTextComponent = new StringTextComponent(String.format("%s %d/%d (%d %%)", localizeBatteryLevelText, cap.getEnergyStored(), cap.getMaxEnergyStored(), percent));
            batteryLevelTextComponent.setStyle(Style.EMPTY.setColor(Color.fromHex("#8cf4e2")));
            tooltip.add(batteryLevelTextComponent);
        });

        // 継承元のメソッドを呼ぶ
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    /**
     * 充電のための何か
     */
    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        if (DEBUG_IS_CLICK_CHARGE) {
            context.getItem().getCapability(CapabilityEnergy.ENERGY).ifPresent(cap -> {
                cap.receiveEnergy(5, false);
            });
        }
        return ActionResultType.SUCCESS;
    }

    /**
     * 破壊速度を返す
     * <p>
     * 電力切れの時はデフォルト
     */
    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        // ForgeEnergyの何か取得
        EnergyStorage cap = (EnergyStorage) stack.getCapability(CapabilityEnergy.ENERGY).resolve().get();
        if (cap.getEnergyStored() > 0) {
            // 電力があるときは高速で破壊
            return 40;
        } else {
            // からっぽなら低速モード
            return 1.0f;
        }
    }

    /**
     * ブロックを壊す。エネルギーを1減らす
     */
    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving) {
        // エネルギー関連のやつのnullチェック
        if (stack.getCapability(CapabilityEnergy.ENERGY).isPresent()) {
            // ForgeEnergyの何か取得
            EnergyStorage cap = (EnergyStorage) stack.getCapability(CapabilityEnergy.ENERGY).resolve().get();
            if (!worldIn.isRemote && state.getBlockHardness(worldIn, pos) != 0.0F) {
                stack.damageItem(0, entityLiving, (entity) -> {
                    entity.sendBreakAnimation(EquipmentSlotType.MAINHAND);
                });
                // 電池減らす
                cap.extractEnergy(1, false);
            }
        }
        return true;
    }

    /**
     * エンティティを攻撃したとき
     */
    @Override
    public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.getCapability(CapabilityEnergy.ENERGY).ifPresent(cap -> {
            // 電池切れじゃなければ
            if (cap.getEnergyStored() > 0) {
                // 攻撃モジュール発動
                rangeDamage(stack, target, attacker);
            }
            // 電池減らす
            cap.extractEnergy(2, false);
        });
        return true;
    }

    /**
     * 耐久バーを出す
     */
    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return true;
    }

    /**
     * エンチャントさせない
     */
    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return false;
    }

    /**
     * 電池が切れてなければエンチャントしたときみたいにキラキラさせる
     */
    @Override
    public boolean hasEffect(ItemStack stack) {
        // 取得可能か
        if (stack.getCapability(CapabilityEnergy.ENERGY).resolve().isPresent()) {
            // 電池切れじゃないなら
            return stack.getCapability(CapabilityEnergy.ENERGY).resolve().get().getEnergyStored() > 0;
        } else {
            return false;
        }
    }

    /**
     * 耐久バーの値を返す
     */
    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        return stack.getCapability(CapabilityEnergy.ENERGY)
                .map(cap -> {
                    double maxAmount = cap.getMaxEnergyStored();
                    double energyDif = maxAmount - cap.getEnergyStored();
                    return energyDif / maxAmount;
                })
                .orElse(super.getDurabilityForDisplay(stack));
    }


    /**
     * エネルギー追加のための何か
     * <p>
     * 今回は {@link ICapabilityProvider} の実装をこのクラスで行ったため this を返す
     */
    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new EnergyCapabilityProvider(stack);
    }

    /**
     * Forge Energyよくわからん。よくわからんから他の人の参考にすっるわ
     */
    private static class EnergyCapabilityProvider implements ICapabilityProvider {

        public final NBTEnergyStorage storage;
        private final LazyOptional<NBTEnergyStorage> lazyStorage;

        public EnergyCapabilityProvider(final ItemStack stack) {
            // 電池容量（未アップグレード時）は1000
            this.storage = new NBTEnergyStorage(1000) {
                /**
                 * getEnergyStored()が呼ばれたらNBTタグから取り出す
                 * */
                @Override
                public int getEnergyStored() {
                    if (stack.hasTag()) {
                        return stack.getOrCreateTag().getInt("energy");
                    } else {
                        return 0;
                    }
                }

                /**
                 * receiveEnergy()の時に内部で呼ばれる。残量をNBTタグに書き込む
                 * */
                @Override
                public void setEnergyStored(int energy) {
                    stack.getOrCreateTag().putInt("energy", energy);
                }

                /**
                 * 電池容量を返す
                 * */
                @Override
                public int getMaxEnergyStored() {
                    int defaultCapacity = 1000;
                    // 電池容量上昇モジュールの数
                    ElectricPickaxeItem item = (ElectricPickaxeItem) stack.getItem();
                    int batteryUpgradeLevel = item.getModuleLevel(stack, RegisterItems.BATTERY_UPGRADE_MODULE_ITEM.get().getRegistryNameString());
                    // 1モジュールで100増える
                    if (batteryUpgradeLevel > 0) {
                        defaultCapacity += batteryUpgradeLevel * 100;
                    }
                    // ついでに充電速度も上げとく
                    if (batteryUpgradeLevel > 0) {
                        this.maxExtract += batteryUpgradeLevel * 10;
                        this.maxReceive += batteryUpgradeLevel * 10;
                    }
                    this.capacity = defaultCapacity;
                    return defaultCapacity;
                }
            };
            lazyStorage = LazyOptional.of(() -> this.storage);
        }

        /**
         * エネルギー追加のための何か
         */
        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
            if (cap == CapabilityEnergy.ENERGY) {
                return this.lazyStorage.cast();
            }
            return LazyOptional.empty();
        }

        /**
         * エネルギー追加のための何か
         */
        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
            if (cap == CapabilityEnergy.ENERGY) {
                return this.lazyStorage.cast();
            }
            return LazyOptional.empty();
        }
    }
}