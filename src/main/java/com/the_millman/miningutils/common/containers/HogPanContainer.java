package com.the_millman.miningutils.common.containers;

import com.the_millman.miningutils.common.blockentity.HogPanBE;
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

public class HogPanContainer extends ItemEnergyContainer {

	protected HogPanBE blockEntity;
	protected Player playerEntity;
	protected IItemHandler playerInventory;
	protected FluidStack fluidStack;
	
	public HogPanContainer(int windowId, Level world, BlockPos pos, Inventory playerInventory, Player player) {
		super(ContainerInit.HOG_PAN_CONTAINER.get(), windowId, world, pos, playerInventory, player);

		BlockEntity entity = world.getBlockEntity(pos);
		this.blockEntity = (HogPanBE) entity;
        this.playerEntity = player;
        this.playerInventory = new InvWrapper(playerInventory);
        this.fluidStack = blockEntity.getFluidStack();
        
        if (blockEntity != null) {
			blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(h -> {
				this.addSlot(new SlotItemHandler(h, 0, 26, 18));
				this.addSlot(new SlotItemHandler(h, 1, 26, 54));
				this.addSlot(new SlotItemHandler(h, 2, 62, 36));
				this.addSlot(new SlotItemHandler(h, 3, 116, 36));
			});
        }
        
        //TODO Ricordarsi di rimmettere dopo prossimo aggiornamento
        layoutPlayerInventorySlots(this.playerInventory, 8, 84);
	}

	@Override
	public boolean stillValid(Player pPlayer) {
		return stillValid(ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos()), playerEntity, BlockInit.HOG_PAN.get());
	}
	
	@Override
	public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(pIndex);
		if (slot != null && slot.hasItem()) {
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();
			if (pIndex < 4) {
				if (!this.moveItemStackTo(itemstack1, 4, 40, true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.moveItemStackTo(itemstack1, 0, 4, false)) {
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
