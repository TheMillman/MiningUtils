package com.the_millman.miningutils.common.containers;

import com.the_millman.miningutils.common.blockentity.WaveTableBE;
import com.the_millman.miningutils.core.init.BlockInit;
import com.the_millman.miningutils.core.init.ContainerInit;
import com.the_millman.themillmanlib.common.containers.ItemEnergyContainer;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class WaveTableContainer extends ItemEnergyContainer {
	
	protected WaveTableBE blockEntity;
	protected Player playerEntity;
	protected IItemHandler playerInventory;
	protected FluidStack fluidStack;
	
	public WaveTableContainer(int windowId, Level world, BlockPos pos, Inventory playerInventory, Player player) {
		super(ContainerInit.WAVE_TABLE_CONTAINER.get(), windowId, world, pos, playerInventory, player);
		BlockEntity entity = world.getBlockEntity(pos);
		this.blockEntity = (WaveTableBE) entity;
        this.playerEntity = player;
        this.playerInventory = new InvWrapper(playerInventory);
        this.fluidStack = blockEntity.getFluidStack();
        
        if (blockEntity != null) {
			blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(h -> {
				this.addSlot(new SlotItemHandler(h, 0, 44, 36));
				this.addSlot(new SlotItemHandler(h, 1, 98, 27));
				this.addSlot(new SlotItemHandler(h, 2, 98, 45));
				this.addSlot(new SlotItemHandler(h, 3, 152, 18));
				this.addSlot(new SlotItemHandler(h, 4, 152, 54));
			});
        }
        
        layoutPlayerInventorySlots(this.playerInventory, 8, 84);
	}

	@Override
	public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(pIndex);
		if (slot != null && slot.hasItem()) {
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();
			if (pIndex < 5) {
				if (!this.moveItemStackTo(itemstack1, 5, 41, true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.moveItemStackTo(itemstack1, 0, 5, false)) {
				return ItemStack.EMPTY;
			}

			if (itemstack1.isEmpty()) {
				slot.set(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}

			if (itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTake(pPlayer, itemstack1);
		}

		return itemstack;
	}

	@Override
	public boolean stillValid(Player arg0) {
		return stillValid(ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos()), playerEntity, BlockInit.WAVE_TABLE.get());
	}

	public BlockEntity getBlockEntity() {
		return this.blockEntity;
	}

	public void setFluid(FluidStack fluidStack) {
		this.fluidStack = fluidStack;
	}

	public FluidStack getFluidStack() {
		return this.fluidStack;
	}
	
	public int getProgress() {
		return blockEntity.getProgress();
	}
	
	public int getMaxProgress() {
		return blockEntity.getMaxProgress();
	}
}
