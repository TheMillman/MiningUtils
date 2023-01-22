package com.the_millman.miningutils.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.the_millman.miningutils.common.containers.WaveTableContainer;
import com.the_millman.miningutils.core.util.MiningConfig;
import com.the_millman.miningutils.core.util.MiningUtilsResources;
import com.the_millman.themillmanlib.client.screens.ItemEnergyFluidScreen;
import com.the_millman.themillmanlib.core.util.LibResources;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class WaveTableScreen extends ItemEnergyFluidScreen<WaveTableContainer> {

	private ResourceLocation GUI = MiningUtilsResources.WAVE_TABLE_GUI;
	private ResourceLocation OVERLAY = LibResources.OFFSETS;
	
	public WaveTableScreen(WaveTableContainer pMenu, Inventory pPlayerInventory, Component pTitle) {
		super(pMenu, pPlayerInventory, pTitle);
	}

	@Override
	protected void init() {
		super.init();
		initFluidRenderer(MiningConfig.WAVE_TABLE_FLUID_CAPACITY.get(), true, 13, 52);
	}
	
	//TODO Spostare in lib
	@Override
	protected void renderLabels(PoseStack pPoseStack, int pMouseX, int pMouseY) {
		int xPos = (width - imageWidth) / 2;
        int yPos = (height - imageHeight) / 2;
        super.renderLabels(pPoseStack, pMouseX, pMouseY);
        renderFluidTooltip(pPoseStack, pMouseX, pMouseY, 134, 146, 18, 69, menu.getFluidStack(), xPos, yPos, "tooltip.themillmanlib.fluid_amount_capacity", "tooltip.themillmanlib.fluid_amount");
	}
	
	@Override
	public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
		this.renderBackground(pPoseStack);
		super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
		this.renderTooltip(pPoseStack, pMouseX, pMouseY);
		this.renderEnergyLevel(pPoseStack, pMouseX, pMouseY);
	}
	
	@Override
	protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
		RenderSystem.setShaderTexture(0, GUI);
        int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;
        this.blit(pPoseStack, relX, relY, 0, 0, this.imageWidth, this.imageHeight);
        renderer.render(pPoseStack, relX + 134, relY + 18, menu.getFluidStack());
        this.renderEnergyBar(pPoseStack, OVERLAY);
        renderProgress(pPoseStack);
	}

	private void renderProgress(PoseStack pPoseStack) {
		RenderSystem.setShaderTexture(0, GUI);
		int gL = this.getGuiLeft();
		int gT = this.getGuiTop();
		int progress = (int)(23*menu.getProgress()/(float)menu.getMaxProgress());
		this.blit(pPoseStack, gL + 67, gT + 36, 176, 0, progress+1, 16);
	}
}
