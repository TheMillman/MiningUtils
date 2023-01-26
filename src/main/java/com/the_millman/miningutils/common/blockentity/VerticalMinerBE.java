package com.the_millman.miningutils.common.blockentity;

import org.jetbrains.annotations.NotNull;

import com.the_millman.miningutils.common.blocks.BlockBreakerBlock;
import com.the_millman.miningutils.common.blocks.VerticalMinerBlock;
import com.the_millman.miningutils.core.init.BlockEntityInit;
import com.the_millman.miningutils.core.util.MiningConfig;
import com.the_millman.themillmanlib.common.blockentity.ItemEnergyBlockEntity;
import com.the_millman.themillmanlib.core.energy.ModEnergyStorage;
import com.the_millman.themillmanlib.core.util.LibTags;

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

public class VerticalMinerBE extends ItemEnergyBlockEntity {

	private int x, y, z, tick;
	int pX, pY, pZ;
	boolean initialized = false;
	int range;
	boolean needRedstone = false;
	boolean pickupDrops = true;
	boolean stop;
	
	public VerticalMinerBE(BlockPos pWorldPosition, BlockState pBlockState) {
		super(BlockEntityInit.VERTICAL_MINER.get(), pWorldPosition, pBlockState);
	}

	@Override
	protected void init() {
		initialized = true;
		
		Direction facing = getBlockState().getValue(BlockBreakerBlock.FACING);
		switch(facing) {
		case NORTH:
			this.x = getBlockPos().getX()-1;
			this.z = getBlockPos().getZ() - 3;
		case SOUTH: 
			this.x = getBlockPos().getX() -1;
			this.z = getBlockPos().getZ() + 1;
		case WEST:
			this.x = getBlockPos().getX() - 3;
			this.z = getBlockPos().getZ() - 1;
		case EAST:
			this.x = getBlockPos().getX() + 1;
			this.z = getBlockPos().getZ()-1;
		default:
			this.x = getBlockPos().getX()-1;
			this.z = getBlockPos().getZ() - 3;
		}
		
//		if (facing == Direction.NORTH) {
//			this.x = getBlockPos().getX()-1;
//			this.z = getBlockPos().getZ() - 3;
//		} else if (facing == Direction.SOUTH) {
//			this.x = getBlockPos().getX() -1;
//			this.z = getBlockPos().getZ() + 1;
//		} else if (facing == Direction.WEST) {
//			this.x = getBlockPos().getX() - 3;
//			this.z = getBlockPos().getZ() - 1;
//		} else if (facing == Direction.EAST) {
//			this.x = getBlockPos().getX() + 1;
//			this.z = getBlockPos().getZ()-1;
//		}
		
		this.y= getBlockPos().getY() -1;
		tick = 0;

		this.range = 3;
		
		this.needRedstone = false;
		this.pickupDrops = true;
	}
	
	@Override
	public void tickServer() {
		if (!initialized) {
			init();
		}

		energyStorage.addEnergy(100);
		if (!level.isClientSide()) {
			if (!getStop()) {
				if (hasPowerToWork(energyStorage, MiningConfig.VERTICAL_MINER_USEPERTICK.get())) {
					tick++;
					if (tick == 2) {
						tick = 0;
						redstoneUpgrade();
						if (canWork()) {
							rangeUpgrade();
							BlockPos posToBreak = new BlockPos(this.x + this.pX, this.y + this.pY, this.z + this.pZ);
							destroyBlock(posToBreak, false);
							setChanged();

							pX++;
							if (pX >= this.range) {
								this.pX = 0;
								this.pZ++;
								if (this.pZ >= this.range) {
									this.pX = 0;
									this.pZ = 0;
									this.pY--;
									int limitY = (this.y + this.pY);
									if (limitY <= (level.dimensionType().minY())) {
										setStop(true);
										this.pY = 0;
									}
								}
							}
						}
					}
				}
			}
		}
	}

	//TODO Migliorare, magari fare un solo metodo per tutti gli upgrade.
	private void redstoneUpgrade() {
		int slot = getUpgradeSlot(upgradeItemStorage, LibTags.Items.REDSTONE_UPGRADE, 0, 2);
		boolean isUpgrade = getStackInSlot(upgradeItemStorage, slot).is(LibTags.Items.REDSTONE_UPGRADE);
		if (isUpgrade) {
			this.needRedstone = true;
		} else if (!isUpgrade) {
			this.needRedstone = false;
		}
	}
	
//	private void rangeUpgrade() {
//		int slot = getUpgrade(LibTags.Items.RANGE_UPGRADE, 18, 20);
//		ItemStack upgradeSlot = getStackInSlot(slot);
//		if (upgradeSlot.is(LibTags.Items.IRON_RANGE_UPGRADE)) {
//			this.x = getBlockPos().getX() - 2;
//			this.z = getBlockPos().getZ() - 2;
//			this.range = 5;
//			resetMiningPos();
//		} else if (upgradeSlot.is(LibTags.Items.GOLD_RANGE_UPGRADE)) {
//			this.x = getBlockPos().getX() - 3;
//			this.z = getBlockPos().getZ() - 3;
//			this.range = 7;
//			resetMiningPos();
//		} else if (upgradeSlot.is(LibTags.Items.DIAMOND_RANGE_UPGRADE)) {
//			this.x = getBlockPos().getX() - 4;
//			this.z = getBlockPos().getZ() - 4;
//			this.range = 9;
//			resetMiningPos();
//		} else {
//			this.x = getBlockPos().getX() - 1;
//			this.z = getBlockPos().getZ() - 1;
//			this.range = 3;
//		}
//	}
	
	private void rangeUpgrade() {
		Direction facing = getBlockState().getValue(VerticalMinerBlock.FACING);
		int slot = getUpgradeSlot(upgradeItemStorage, LibTags.Items.RANGE_UPGRADE, 0, 2);
		ItemStack upgradeSlot = getStackInSlot(upgradeItemStorage, slot);
//		System.out.println(facing);
		if(facing == Direction.NORTH) {
			if (upgradeSlot.is(LibTags.Items.IRON_RANGE_UPGRADE)) {
				this.x = getBlockPos().getX() -2;
				this.z = getBlockPos().getZ() -5;
				this.range = 5;
			} else if (upgradeSlot.is(LibTags.Items.GOLD_RANGE_UPGRADE)) {
				this.x = getBlockPos().getX() -3;
				this.z = getBlockPos().getZ() -7;
				this.range = 7;
			} else if (upgradeSlot.is(LibTags.Items.DIAMOND_RANGE_UPGRADE)) {
				this.x = getBlockPos().getX() -4;
				this.z = getBlockPos().getZ() - 9;
				this.range = 9;
			} else {
				this.x = getBlockPos().getX() -1;
				this.z = getBlockPos().getZ() -3;
				this.range = 3;
			}
		} else if(facing == Direction.SOUTH) {
			if (upgradeSlot.is(LibTags.Items.IRON_RANGE_UPGRADE)) {
				this.x = getBlockPos().getX() -2;
				this.z = getBlockPos().getZ() +1;
				this.range = 5;
			} else if (upgradeSlot.is(LibTags.Items.GOLD_RANGE_UPGRADE)) {
				this.x = getBlockPos().getX() -3;
				this.z = getBlockPos().getZ() +1;
				this.range = 7;
			} else if (upgradeSlot.is(LibTags.Items.DIAMOND_RANGE_UPGRADE)) {
				this.x = getBlockPos().getX() -4;
				this.z = getBlockPos().getZ() +1;
				this.range = 9;
			} else {
				this.x = getBlockPos().getX() -1;
				this.z = getBlockPos().getZ() +1;
				this.range = 3;
			}
		} else if(facing == Direction.WEST) {
			if (upgradeSlot.is(LibTags.Items.IRON_RANGE_UPGRADE)) {
				this.x = getBlockPos().getX() -5;
				this.z = getBlockPos().getZ() -2;
				this.range = 5;
			} else if (upgradeSlot.is(LibTags.Items.GOLD_RANGE_UPGRADE)) {
				this.x = getBlockPos().getX() -7;
				this.z = getBlockPos().getZ() -3;
				this.range = 7;
			} else if (upgradeSlot.is(LibTags.Items.DIAMOND_RANGE_UPGRADE)) {
				this.x = getBlockPos().getX() -9;
				this.z = getBlockPos().getZ() -4;
				this.range = 9;
			} else {
				this.x = getBlockPos().getX() -3;
				this.z = getBlockPos().getZ() -1;
				this.range = 3;
			}
		} else if(facing == Direction.EAST) {
			if (upgradeSlot.is(LibTags.Items.IRON_RANGE_UPGRADE)) {
				this.x = getBlockPos().getX() +1;
				this.z = getBlockPos().getZ() -2;
				this.range = 5;
			} else if (upgradeSlot.is(LibTags.Items.GOLD_RANGE_UPGRADE)) {
				this.x = getBlockPos().getX() +1;
				this.z = getBlockPos().getZ() -3;
				this.range = 7;
			} else if (upgradeSlot.is(LibTags.Items.DIAMOND_RANGE_UPGRADE)) {
				this.x = getBlockPos().getX() +1;
				this.z = getBlockPos().getZ() -4;
				this.range = 9;
			} else {
				this.x = getBlockPos().getX() +1;
				this.z = getBlockPos().getZ() -1;
				this.range = 3;
			}
		}
	}

	private boolean canWork() {
		if(this.needRedstone) {
			if(getBlockState().getValue(VerticalMinerBlock.POWERED)) {
				return true;
			}
			return false;
		} else if(!this.needRedstone) {
			return true;
		}
		return true;
	}
	
	private boolean getStop() {
		return this.stop;
	}
	
	private void setStop(boolean hasStopped) {
		this.stop = hasStopped;
	}

	private boolean destroyBlock(BlockPos pos, boolean dropBlock) {
		BlockState state = level.getBlockState(pos);
		
		if (state.isAir()) {
			return false;
		} else if (getDestBlock(state)) {
			if (!level.isClientSide) {
				if (this.pickupDrops) {
					collectDrops(level, itemStorage, pos, 0, 18);
					level.destroyBlock(pos, dropBlock);
					consumeEnergy(energyStorage, MiningConfig.VERTICAL_MINER_USEPERTICK.get());
					return true;
				} else if(!this.pickupDrops) {
					level.destroyBlock(pos, true);
					consumeEnergy(energyStorage, MiningConfig.VERTICAL_MINER_USEPERTICK.get());
					return true;
				}
				return false;
			}
			return false;
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
		return new ItemStackHandler(18) {
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
		return new ModEnergyStorage(true, MiningConfig.VERTICAL_MINER_CAPACITY.get(), MiningConfig.VERTICAL_MINER_USEPERTICK.get() * 2) {
			@Override
			protected void onEnergyChanged() {
				boolean newHasPower = hasPowerToWork(energyStorage, MiningConfig.VERTICAL_MINER_USEPERTICK.get());
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
		tag.putBoolean("mining_stop", this.stop);
		tag.putInt("mining_pX", this.pX);
		tag.putInt("mining_pY", this.pY);
		tag.putInt("mining_pZ", this.pZ);
	}
	
	@Override
	public void load(CompoundTag pTag) {
		if(pTag.contains("mining_stop")) {
			this.stop = pTag.getBoolean("mining_stop");
		}
		
		if(pTag.contains("mining_pX")) {
			this.pX = pTag.getInt("mining_pX");
		}
		
		if(pTag.contains("mining_pY")) {
			this.pY = pTag.getInt("mining_pY");
		}
		
		if(pTag.contains("mining_pZ")) {
			this.pZ = pTag.getInt("mining_pZ");
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
