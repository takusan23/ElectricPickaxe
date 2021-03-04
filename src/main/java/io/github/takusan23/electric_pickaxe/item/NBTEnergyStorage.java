package io.github.takusan23.electric_pickaxe.item;

import net.minecraftforge.energy.EnergyStorage;

/**
 * {@link EnergyStorage}、もしかして：セーブして閉じたら消える？
 * <p>
 * というわけでNBTに書き込む
 *
 * ちなみにNBTに書き込んでいる実装は {@link ElectricPickaxeItem} を参照
 */
public abstract class NBTEnergyStorage extends EnergyStorage {
    public NBTEnergyStorage(int capacity) {
        super(capacity);
    }

    public NBTEnergyStorage(int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
    }

    public NBTEnergyStorage(int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract);
    }

    public NBTEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy) {
        super(capacity, maxReceive, maxExtract, energy);
    }

    /**
     * 電力を受け取ったときに呼ばれる
     */
    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        if (!canReceive()) {
            return 0;
        }
        int energy = this.getEnergyStored();
        int energyReceived = Math.min(this.capacity - energy, Math.min(this.maxReceive, maxReceive));
        if (!simulate) {
            setEnergyStored(energy + energyReceived);
        }
        return energyReceived;
    }

    /**
     * 電力を消費したときに呼ばれる
     */
    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        if (!this.canExtract()) {
            return 0;
        }
        int energy = this.getEnergyStored();
        int energyExtracted = Math.min(energy, Math.min(this.maxExtract, maxExtract));
        if (!simulate) {
            this.setEnergyStored(energy - energyExtracted);
        }
        return energyExtracted;
    }

    /**
     * energyをNBTに書き込んで永続化させる
     */
    public void setEnergyStored(int energy) {
        this.energy = energy;
    }


    /**
     * 出力量を変更する
     */
    public void setEnergyTransfer(int transfer) {
        this.maxReceive = transfer;
        this.maxExtract = transfer;
    }
}
