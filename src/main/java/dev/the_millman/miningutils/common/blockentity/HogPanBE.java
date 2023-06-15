package dev.the_millman.miningutils.common.blockentity;

import java.util.Random;

import org.jetbrains.annotations.NotNull;

import dev.the_millman.miningutils.common.items.HogPanMatItem;
import dev.the_millman.miningutils.core.init.BlockEntityInit;
import dev.the_millman.miningutils.core.networking.FluidSyncS2CPacket;
import dev.the_millman.miningutils.core.networking.ModMessages;
import dev.the_millman.miningutils.core.util.MiningConfig;
import dev.the_millman.miningutils.core.util.MiningUtilsTags.ModItemTags;
import dev.the_millman.themillmanlib.common.blockentity.ItemFluidBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

public class HogPanBE extends ItemFluidBlockEntity {

	private int tick;
	private int panContent;
	boolean initialized = false;
	boolean work = true;
	
	public HogPanBE(BlockPos pPos, BlockState pBlockState) {
		super(BlockEntityInit.HOG_PAN.get(), pPos, pBlockState);
	}
	
	protected void init() {
		this.initialized = true;
		tick = 0;
		this.work = true;
	}
	
	@Override
	public void tickServer() {
		if (!initialized)
			init();

		if (getStackInSlot(upgradeItemStorage, 1).getCount() < 16) {
			transferItemFluidToFluidTank(upgradeItemStorage, fluidStorage, 0, 1);
		}
		
		if(getStackInSlot(itemStorage, 0).is(ModItemTags.SIFTABLE_BLOCKS)) {
			if(getFluidAmount(fluidStorage) >= MiningConfig.HOG_PAN_WATER_CONSUME.get()) {
				if(getStackInSlot(itemStorage, 1).getItem() instanceof HogPanMatItem) {
					this.work = checkWork();
					if(this.work) {
						tick++;
						if(this.tick >= getMaxProgress()) {
							tick = 0;
							if(!level.isClientSide()) {
								ItemStack panItem = getPanItemStack();
								if (panItem.hasTag()) {
									if (panItem.getTag().getInt("miningutils_content") < 100) {
										this.panContent = panItem.getTag().getInt("miningutils_content");
										if(panContent + checkInput() <= 100) {
											addContentToPanItem(panContent + checkInput());
											consumeStack(itemStorage, 0, 1);
											drain(fluidStorage, MiningConfig.HOG_PAN_WATER_CONSUME.get(), FluidAction.EXECUTE);
											this.work = true;
										} else if(panContent + checkInput() >= 100) {
											addContentToPanItem(100);
											this.work = false;
										}
									}
								} else if (panItem.hasTag() == false) {
									addContentToPanItem(0);
								}
							}
						}
					} 
				}
			} 
		} if (getStackInSlot(itemStorage, 0).isEmpty() || getStackInSlot(itemStorage, 1).isEmpty()) {
			this.tick = 0;
		}
	}
	
	private void addContentToPanItem(int content) {
		ItemStack panItem = getStackInSlot(itemStorage, 1);
		if(panItem.getItem() instanceof HogPanMatItem) {
			CompoundTag contentTag = new CompoundTag();
			contentTag.putInt("miningutils_content", content);
			panItem.setTag(contentTag);
		}
	}
	
	private ItemStack getPanItemStack() {
		ItemStack panItem = getStackInSlot(itemStorage, 1);
		if(panItem.getItem() instanceof HogPanMatItem) {
			return panItem;
		}
		return ItemStack.EMPTY;
	}
	
	private int checkInput() {
		if(getStackInSlot(itemStorage, 0).is(Items.MUD)) {
			return new Random().nextInt(3, 7);
		}
		return new Random().nextInt(1, 4);
	}
	
	private boolean checkWork() {
		ItemStack panItem = getPanItemStack();
		if (panItem.hasTag()) {
			if (panItem.getTag().getInt("miningutils_content") < 100) {
				return true;
			}
			else 
				return false;
		} else if (panItem.hasTag() == false) {
			addContentToPanItem(0);
			return true;
		}
		return false;
	}
	
	public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemStorage.getSlots());
        for (int i = 0; i < itemStorage.getSlots(); i++) {
            inventory.setItem(i, itemStorage.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }
	
	public int getProgress() {
		return this.tick;
	}
	
	public int getMaxProgress() {
		return MiningConfig.HOG_PAN_TICK.get();
	}
	
	@Override
	public boolean isValidBlock(ItemStack stack) {
		return false;
	}
	
	public void setFluid(FluidStack fluidStack) {
		setFluid(fluidStorage, fluidStack);
	}
	
	@Override
	public ItemStackHandler itemStorage() {
		return new ItemStackHandler(2) {
			@Override
			protected void onContentsChanged(int slot) {
				setChanged();
			}
			
			@Override
			public boolean isItemValid(int slot, @NotNull ItemStack stack) {
				switch(slot) {
					case 0:
						return stack.is(ModItemTags.SIFTABLE_BLOCKS) ? true : false;
					case 1:
						return stack.getItem() instanceof HogPanMatItem ? true : false;
					default : 
						return false;
				}
			}
		};
	}
	
	@Override
	protected ItemStackHandler upgradeItemStorage() {
		return new ItemStackHandler(2) {
			@Override
			protected void onContentsChanged(int slot) {
				setChanged();
			}
			
			@Override
			public boolean isItemValid(int slot, @NotNull ItemStack stack) {
				switch(slot) {
					case 0:
						return stack.is(Items.WATER_BUCKET) ? true : false;
					case 1:
						return stack.is(Items.BUCKET) ? true : false;
					default:
						return false;
				}
			}
		};
	}
	
	@Override
	protected IItemHandler createCombinedItemHandler() {
		return new CombinedInvWrapper(upgradeItemStorage, itemStorage) {
			
		};
	}

	@Override
	public FluidTank fluidStorage() {
		return new FluidTank(MiningConfig.HOG_PAN_WATER_CAPACITY.get()) {
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
		if (cap == ForgeCapabilities.ITEM_HANDLER) {
			if(side == null) {
				upgradeItemHandler.cast();
				return combinedItemHandler.cast();
			} else if(side == Direction.UP || side == Direction.DOWN) {
				return upgradeItemHandler.cast();
			}
		}
		return super.getCapability(cap, side);
    }
}
