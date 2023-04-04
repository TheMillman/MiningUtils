package com.the_millman.miningutils.common.blockentity;

import org.jetbrains.annotations.NotNull;

import com.the_millman.miningutils.common.blocks.BlockBreakerBlock;
import com.the_millman.miningutils.core.init.BlockEntityInit;
import com.the_millman.miningutils.core.util.MiningConfig;
import com.the_millman.themillmanlib.common.blockentity.ItemEnergyBlockEntity;
import com.the_millman.themillmanlib.core.energy.ModEnergyStorage;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

public class BlockBreakerBE extends ItemEnergyBlockEntity {

	int x, y, z;
	boolean initialized = false, changes = true;
	
	public BlockBreakerBE(BlockPos pWorldPosition, BlockState pBlockState) {
		super(BlockEntityInit.BLOCK_BREAKER.get(), pWorldPosition, pBlockState);
	}

	@Override
	protected void init() {
		initialized = true;
		
		Direction facing = getBlockState().getValue(BlockBreakerBlock.FACING);	
		if(facing == Direction.NORTH) {
			this.x = getBlockPos().getX();
			this.z = getBlockPos().getZ() - 1;
		} else if(facing == Direction.SOUTH) {
			this.x = getBlockPos().getX();
			this.z = getBlockPos().getZ() + 1;
		} else if(facing == Direction.WEST) {
			this.x = getBlockPos().getX() - 1;
			this.z = getBlockPos().getZ();
		} else if(facing == Direction.EAST) {
			this.x = getBlockPos().getX() + 1;
			this.z = getBlockPos().getZ();
		}
		
		this.y = getBlockPos().getY();
	}
	
	// TODO Aggiungere parte upgrade
	@Override
	public void tickServer() {
		if (!initialized) {
			init();
		}
		
		if (hasRedstoneSignal()) {
			if (hasPowerToWork(energyStorage, MiningConfig.BLOCK_BREAKER_USEPERTICK.get())) {
				BlockPos posToBreak = new BlockPos(this.x, this.y, this.z);
				destroyBlock(posToBreak, false);
				setChanged();
			}
		}
	}

	private boolean hasRedstoneSignal() {
		Boolean powered = getBlockState().getValue(BlockBreakerBlock.POWERED);
		if(powered && this.changes == true) {
			this.changes = false;
			return true;
		} else if(!powered && this.changes == true) {
			this.changes = true;
			return false;
		} else if(!powered && this.changes == false) {
			this.changes = true;
			return false;
		}
		return false;
	}
	
	//TODO Aggiungere parte upgrade
	private boolean destroyBlock(BlockPos posToBreak, boolean dropBlock) {
		BlockState state = level.getBlockState(posToBreak);
		
		if(state.isAir()) {
			return false;
		} else if(getDestBlock(state)) {
			if(!level.isClientSide()) {
				collectDrops(level, itemStorage, posToBreak, 0, 9);
				level.destroyBlock(posToBreak, dropBlock);
				consumeEnergy(energyStorage, MiningConfig.BLOCK_BREAKER_USEPERTICK.get());
				return true;
			}
		}
		return false;
	}
	
	private boolean getDestBlock(BlockState state) {
		if (!state.is(Blocks.BEDROCK)) {
			return true;
		}
		return false;
	}
	
	@Override
	public boolean isValidBlock(ItemStack stack) {
		return false;
	}

	@Override
	protected ItemStackHandler itemStorage() {
		return new ItemStackHandler(9) {
			@Override
			protected void onContentsChanged(int slot) {
				setChanged();
			}
			
			@Override
			public boolean isItemValid(int slot, ItemStack stack) {
				return true;
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
				return isValidUpgrade(stack) ? true : false;
			}
		};
	}
	
	@Override
	protected IItemHandler createCombinedItemHandler() {
		return new CombinedInvWrapper(itemStorage, upgradeItemStorage) {
			
		};
	}

	@Override
	protected ModEnergyStorage createEnergy() {
		return new ModEnergyStorage(true, MiningConfig.BLOCK_BREAKER_CAPACITY.get(), MiningConfig.BLOCK_BREAKER_USEPERTICK.get()*2) {
			@Override
			protected void onEnergyChanged() {
				boolean newHasPower = hasPowerToWork(energyStorage, MiningConfig.BLOCK_BREAKER_USEPERTICK.get());
				if (newHasPower) {
					level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
				}
				setChanged();
			}
		};
	}
	
	@Override
	public void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		tag.putBoolean("mining_changes", changes);
	}
	
	@Override
	public void load(CompoundTag pTag) {
		if(pTag.contains("mining_changes")) {
			changes = pTag.getBoolean("mining_changes");
		}
		
		super.load(pTag);
	}
	
	@Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    	if(cap == ForgeCapabilities.ITEM_HANDLER) {
			if (side == null) {
            	upgradeItemHandler.cast();
                return combinedItemHandler.cast();
            } else {
                return itemStorageHandler.cast();
            }
        }
    	return super.getCapability(cap, side);
    }
}