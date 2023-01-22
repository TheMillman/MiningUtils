package com.the_millman.miningutils.common.blockentity;

import org.jetbrains.annotations.NotNull;

import com.the_millman.miningutils.common.items.HogPanMatItem;
import com.the_millman.miningutils.core.init.BlockEntityInit;
import com.the_millman.miningutils.core.networking.FluidSyncS2CPacket;
import com.the_millman.miningutils.core.networking.ModMessages;
import com.the_millman.miningutils.core.util.MiningConfig;
import com.the_millman.miningutils.core.util.MiningUtilsTags.ModItemTags;
import com.the_millman.themillmanlib.common.blockentity.ItemFluidBlockEntity;

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

		if (getStackInSlot(itemStorage, 1).getCount() < 16) {
			transferItemFluidToFluidTank(itemStorage, fluidStorage, 0, 1);
		}

		if (!level.isClientSide()) {
			if (getStackInSlot(itemStorage, 2).is(ModItemTags.SIFTABLE_BLOCKS)
					&& fluidStorage.getFluidAmount() >= MiningConfig.HOG_PAN_WATER_CONSUME.get()
					&& getStackInSlot(itemStorage, 3).getItem() instanceof HogPanMatItem && this.work) {
				tick++;
				if (tick >= MiningConfig.HOG_PAN_TICK.get()) {
					tick = 0;
					ItemStack panItem = getPanItemStack();
					if (panItem.hasTag()) {
						if (panItem.getTag().getInt("miningutils_content") < 100) {
							this.panContent = panItem.getTag().getInt("miningutils_content");
							addContentToPanItem(panContent+checkInput());
							consumeStack(itemStorage, 2, 1);
							drain(fluidStorage, MiningConfig.HOG_PAN_WATER_CONSUME.get(), FluidAction.EXECUTE);
							if (panContent >= 100) {
								this.work = false;
							} else
								this.work = true;
						}
					} if(panItem.hasTag() == false) {
						addContentToPanItem(0);
					}
				}
			}
		}
	}
	
	private void addContentToPanItem(int content) {
		ItemStack panItem = getStackInSlot(itemStorage, 3);
		if(panItem.getItem() instanceof HogPanMatItem) {
			CompoundTag contentTag = new CompoundTag();
			contentTag.putInt("miningutils_content", content);
			panItem.setTag(contentTag);
		}
	}
	
	private ItemStack getPanItemStack() {
		ItemStack panItem = getStackInSlot(itemStorage, 3);
		if(panItem.getItem() instanceof HogPanMatItem) {
			return panItem;
		}
		return null;
	}
	
	private int checkInput() {
		if(getStackInSlot(itemStorage, 2).is(Items.MUD)) {
			return 5;
		}
		return 1;
	}
	
	public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemStorage.getSlots());
        for (int i = 0; i < itemStorage.getSlots(); i++) {
            inventory.setItem(i, itemStorage.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
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
		return new ItemStackHandler(4) {
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
					case 2:
						return stack.is(ModItemTags.SIFTABLE_BLOCKS) ? true : false;
					case 3:
						return stack.getItem() instanceof HogPanMatItem ? true : false;
					default : 
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
