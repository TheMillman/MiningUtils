package com.the_millman.miningutils.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.the_millman.miningutils.common.containers.VerticalMinerContainer;
import com.the_millman.miningutils.core.util.MiningUtilsResources;
import com.the_millman.themillmanlib.client.screens.ItemEnergyScreen;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class VerticalMinerScreen extends ItemEnergyScreen<VerticalMinerContainer> {

	private ResourceLocation GUI = MiningUtilsResources.VERTICAL_MINER_GUI;
	private ResourceLocation OVERLAY = MiningUtilsResources.OFFSET;
	
	public VerticalMinerScreen(VerticalMinerContainer pMenu, Inventory pPlayerInventory, Component pTitle) {
		super(pMenu, pPlayerInventory, pTitle);
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
        this.blit(pPoseStack, relX, relY, 0, 0, 196, this.imageHeight);
        this.renderEnergyBar(pPoseStack, OVERLAY);
	}

}
