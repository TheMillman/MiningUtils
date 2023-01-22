package com.the_millman.miningutils.common.blockentity;

import java.util.Random;

import org.jetbrains.annotations.NotNull;

import com.the_millman.miningutils.core.init.BlockEntityInit;
import com.the_millman.miningutils.core.init.ItemInit;
import com.the_millman.miningutils.core.networking.FluidSyncS2CPacket;
import com.the_millman.miningutils.core.networking.ModMessages;
import com.the_millman.miningutils.core.util.MiningConfig;
import com.the_millman.themillmanlib.common.blockentity.ItemEnergyFluidBlockEntity;
import com.the_millman.themillmanlib.core.energy.ModEnergyStorage;
import com.the_millman.themillmanlib.core.util.BlockUtils;
import com.the_millman.themillmanlib.core.util.ModItemHandlerHelp;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class WaveTableBE extends ItemEnergyFluidBlockEntity {

	int tick;
	boolean initialized = false;
	
	public WaveTableBE(BlockPos pPos, BlockState pBlockState) {
		super(BlockEntityInit.WAVE_TABLE.get(), pPos, pBlockState);
	}

	@Override
	protected void init() {
		this.initialized = true;
		this.tick = 0;
	}
	
	public void tickServer() {
		if (!initialized)
			init();

		if (getStackInSlot(itemStorage, 4).getCount() < 16) {
			transferItemFluidToFluidTank(itemStorage, fluidStorage, 3, 4);
		}

		if (hasPowerToWork(energyStorage, MiningConfig.WAVE_TABLE_USEPERTICK.get()) && 
				getFluidAmount(fluidStorage) >= MiningConfig.WAVE_TABLE_FLUID_USEPERTICK.get()) {
			if (getStackInSlot(itemStorage, 0).is(ItemInit.DIRTY_WATER_BUCKET.get())) {
				tick++;
				if (tick >= getMaxProgress()) {
					tick = 0;
					if (!level.isClientSide()) {
						work();
					}
				}
			}

			if (getStackInSlot(itemStorage, 0).isEmpty()) {
				this.tick = 0;
			}
		}
	}
	
	private void work() {
		ItemStack gold = new ItemStack(Items.GOLD_NUGGET, random());
		ItemStack goldStack = ModItemHandlerHelp.insertItemStacked(itemStorage, gold, 1, 2, false);
		ItemStack bucketStack = ModItemHandlerHelp.insertItemStacked(itemStorage, Items.BUCKET.getDefaultInstance(), 2, 3, false);
		consumeStack(itemStorage, 0, 1);
		consumeEnergy(energyStorage, MiningConfig.WAVE_TABLE_USEPERTICK.get());
		drain(fluidStorage, MiningConfig.WAVE_TABLE_FLUID_USEPERTICK.get(), FluidAction.EXECUTE);
		if (!goldStack.isEmpty()) {
			BlockUtils.spawnItemStack(goldStack, this.level, getBlockPos().above());
		}
		
		if (!bucketStack.isEmpty()) {
			BlockUtils.spawnItemStack(bucketStack, this.level, getBlockPos().above());
		}
	}
	
	public int getProgress() {
		return this.tick;
	}
	
	public int getMaxProgress() {
		return MiningConfig.WAVE_TABLE_TICK.get();
	}
	
	public boolean getWater() {
		return getFluidAmount(fluidStorage) > 0 ? true : false;
	}
	
	private int random() {
		return new Random().nextInt(3, 10);
	}
	
	public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemStorage.getSlots());
        for (int i = 0; i < itemStorage.getSlots(); i++) {
            inventory.setItem(i, itemStorage.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }
	
	public void setFluid(FluidStack fluidStack) {
		setFluid(fluidStorage, fluidStack);
	}

	@Override
	public boolean isValidBlock(ItemStack itemStack) {
		return false;
	}

	@Override
	protected ItemStackHandler itemStorage() {
		return new ItemStackHandler(5) {
			@Override
			protected void onContentsChanged(int slot) {
				setChanged();
			}
			
			@Override
			public boolean isItemValid(int slot, @NotNull ItemStack stack) {
				switch(slot) {
				case 0:
					return stack.is(ItemInit.DIRTY_WATER_BUCKET.get()) ? true : false;
				case 1:
					return stack.is(Items.GOLD_NUGGET) ? true : false;
				case 2:
					return stack.is(Items.BUCKET) ? true : false;
				case 3:
					return stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).isPresent() ? true : false;
				case 4:
					return stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).isPresent() ? true : false;
				default:
					return false;
				}
			}
		};
	}
	
	@Override
	protected ItemStackHandler upgradeItemStorage() {
		return null;
	}
	
	@Override
	protected IItemHandler createCombinedItemHandler() {
		return null;
	}
	
	@Override
	protected ModEnergyStorage createEnergy() {
		return new ModEnergyStorage(true, MiningConfig.WAVE_TABLE_CAPACITY.get(), MiningConfig.WAVE_TABLE_USEPERTICK.get()*2) {
			@Override
			protected void onEnergyChanged() {
				boolean newHasPower = hasPowerToWork(energyStorage, MiningConfig.WAVE_TABLE_USEPERTICK.get());
				if (newHasPower) {
					level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
				}
				setChanged();
			}
		};
	}

	@Override
	protected FluidTank fluidStorage() {
		return new FluidTank(MiningConfig.WAVE_TABLE_FLUID_CAPACITY.get()) {
			@Override
			protected void onContentsChanged() {
				setChanged();
				if(!level.isClientSide()) {
	                ModMessages.sendToClients(new FluidSyncS2CPacket(this.fluid, worldPosition));
	            }
			}
			
			@Override
			public boolean isFluidValid(FluidStack stack) {
				return stack.getFluid() == Fluids.WATER ? true : false;
			}
		};
	}
	
	@Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    	if(cap == ForgeCapabilities.ITEM_HANDLER) {
//			if (side == null) {
//            	upgradeItemHandler.cast();
//                return combinedItemHandler.cast();
//            } else {
                return itemStorageHandler.cast();
//            }
        }
    	return super.getCapability(cap, side);
    }

	@Override
	protected <T> LazyOptional<T> callCapability(Capability<T> arg0, Direction arg1) {
		return null;
	}
}
