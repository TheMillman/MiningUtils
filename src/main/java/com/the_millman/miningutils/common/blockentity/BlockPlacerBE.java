package com.the_millman.miningutils.common.blockentity;

import org.jetbrains.annotations.NotNull;

import com.the_millman.miningutils.MiningUtils;
import com.the_millman.miningutils.common.blocks.BlockBreakerBlock;
import com.the_millman.miningutils.common.blocks.BlockPlacerBlock;
import com.the_millman.miningutils.core.init.BlockEntityInit;
import com.the_millman.miningutils.core.util.MiningConfig;
import com.the_millman.themillmanlib.common.blockentity.ItemEnergyBlockEntity;
import com.the_millman.themillmanlib.core.energy.ModEnergyStorage;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

public class BlockPlacerBE extends ItemEnergyBlockEntity {

	int x, y, z;
	boolean initialized = false, changes = true;
	public BlockPlacerBE(BlockPos pWorldPosition, BlockState pBlockState) {
		super(BlockEntityInit.BLOCK_PLACER.get(), pWorldPosition, pBlockState);
	}

	@Override
	protected void init() {
		initialized = true;

		Direction facing = getBlockState().getValue(BlockBreakerBlock.FACING);
		if (facing == Direction.NORTH) {
			this.x = getBlockPos().getX();
			this.z = getBlockPos().getZ() - 1;
		} else if (facing == Direction.SOUTH) {
			this.x = getBlockPos().getX();
			this.z = getBlockPos().getZ() + 1;
		} else if (facing == Direction.WEST) {
			this.x = getBlockPos().getX() - 1;
			this.z = getBlockPos().getZ();
		} else if (facing == Direction.EAST) {
			this.x = getBlockPos().getX() + 1;
			this.z = getBlockPos().getZ();
		}

		this.y = getBlockPos().getY();
	}
	
	@Override
	public void tickServer() {
		if(!initialized) init();
		
		energyDebug(MiningUtils.DEBUG);
		
		if(hasRedstoneSignal()) {
			if(hasPowerToWork(energyStorage, MiningConfig.BLOCK_PLACER_USEPERTICK.get())) {
				BlockPos posToPlace = new BlockPos(this.x, this.y, this.z);
				placeBlock(posToPlace);
				setChanged();
			}
		}
	}

	private boolean hasRedstoneSignal() {
		Boolean powered = getBlockState().getValue(BlockPlacerBlock.POWERED);
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
	
	private boolean placeBlock(BlockPos pos) {
		int slot = getSlot(itemStorage, 8);

		BlockState state = level.getBlockState(pos);
		ItemStack stack = getStackInSlot(itemStorage, slot);
		if (state.is(Blocks.WATER) || state.is(Blocks.LAVA) || state.isAir()) {
			if (isValidBlock(stack)) {
				if (stack.getItem()instanceof BlockItem blockItem) {
					if (!level.isClientSide()) {
						Block block = blockItem.getBlock();
						level.setBlock(pos, block.defaultBlockState(), Block.UPDATE_ALL);
						level.playSound(null, pos, SoundEvents.STONE_PLACE, SoundSource.BLOCKS, 1F, 1F);
						consumeStack(itemStorage, slot, 1);
						consumeEnergy(energyStorage, MiningConfig.BLOCK_PLACER_USEPERTICK.get());
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public boolean isValidBlock(ItemStack stack) {
		if (stack.is(Items.BEDROCK))
			return false;
		return true;
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
				return stack.getItem() instanceof BlockItem ? true : false;
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
		return new ModEnergyStorage(true, MiningConfig.BLOCK_PLACER_CAPACITY.get(), MiningConfig.BLOCK_PLACER_USEPERTICK.get()*2) {
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
		if (pTag.contains("mining_changes"))
			changes = pTag.getBoolean("mining_changes");

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
