package dev.the_millman.miningutils.common.blockentity;

import java.util.Random;

import org.jetbrains.annotations.NotNull;

import dev.the_millman.miningutils.core.init.BlockEntityInit;
import dev.the_millman.miningutils.core.init.ItemInit;
import dev.the_millman.miningutils.core.networking.FluidSyncS2CPacket;
import dev.the_millman.miningutils.core.networking.ModMessages;
import dev.the_millman.miningutils.core.util.MiningConfig;
import dev.the_millman.themillmanlib.common.blockentity.ItemEnergyFluidBlockEntity;
import dev.the_millman.themillmanlib.core.energy.ModEnergyStorage;
import dev.the_millman.themillmanlib.core.util.BlockUtils;
import dev.the_millman.themillmanlib.core.util.ModItemHandlerHelp;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

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

		if (getStackInSlot(upgradeItemStorage, 2).getCount() < 16) {
			transferItemFluidToFluidTank(itemStorage, fluidStorage, 1, 2);
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
	
	@Override
	public void fillTankWithFluid(ItemStackHandler itemStorage, FluidTank fluidStorage, FluidStack stack, ItemStack container, int slotI, int slotO) {
		fluidStorage.fill(stack, IFluidHandler.FluidAction.EXECUTE);

        itemStorage.extractItem(slotI, 1, false);
        upgradeItemStorage.insertItem(slotO, container, false);
	}
	
	private void work() {
		ItemStack gold = new ItemStack(Items.GOLD_NUGGET, random());
		ItemStack goldStack = ModItemHandlerHelp.insertItemStacked(upgradeItemStorage, gold, 0, 1, false);
		ItemStack bucketStack = ModItemHandlerHelp.insertItemStacked(upgradeItemStorage, Items.BUCKET.getDefaultInstance(), 1, 2, false);
		consumeStack(itemStorage, 0, 1);
		consumeEnergy(energyStorage, MiningConfig.WAVE_TABLE_USEPERTICK.get());
		drain(fluidStorage, MiningConfig.WAVE_TABLE_FLUID_USEPERTICK.get(), FluidAction.EXECUTE);
		setChanged();
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
        drops(level, worldPosition, itemStorage);
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
		return new ItemStackHandler(2) {
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
					return stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).isPresent() ? true : false;
				default:
					return false;
				}
			}
		};
	}
	
	@Override
	protected ItemStackHandler upgradeItemStorage() {
		return new ItemStackHandler(3) {
			@Override
			protected void onContentsChanged(int slot) {
				setChanged();
			}
			
			@Override
			public boolean isItemValid(int slot, @NotNull ItemStack stack) {
				switch(slot) {
				case 0:
					return stack.is(Items.GOLD_NUGGET) ? true : false;
				case 1:
					return stack.is(Items.BUCKET) ? true : false;
				case 2:
					return stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).isPresent() ? true : false;
				default:
					return false;
				}
			}
		};
	}
	
	@Override
	protected IItemHandler createCombinedItemHandler() {
		return new CombinedInvWrapper(itemStorage, upgradeItemStorage) {
			@Override
			public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
				int index = getIndexForSlot(slot);
				if (getHandlerFromIndex(index) == upgradeItemStorage) {
					return stack;
				}
				return super.insertItem(slot, stack, simulate);
			}
		};
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
			if (side == null) {
                return combinedItemHandler.cast();
            } else if(side == Direction.DOWN) {
            	return upgradeItemHandler.cast();
            } else {
                return itemStorageHandler.cast();
            }
        }
    	return super.getCapability(cap, side);
    }
	
	public void setHandler(ItemStackHandler itemStackHandler) {
		for (int i = 0; i < itemStackHandler.getSlots(); i++) {
            itemStorage.setStackInSlot(i, itemStackHandler.getStackInSlot(i));
        }
	}
}
