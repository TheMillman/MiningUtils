package dev.the_millman.miningutils.client.screens;

import dev.the_millman.miningutils.common.containers.VerticalMinerContainer;
import dev.the_millman.miningutils.core.util.MiningUtilsResources;
import dev.the_millman.themillmanlib.client.screens.ItemEnergyScreen;
import net.minecraft.client.gui.GuiGraphics;
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
	public void render(GuiGraphics guiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
		this.renderBackground(guiGraphics);
		super.render(guiGraphics, pMouseX, pMouseY, pPartialTick);
		this.renderTooltip(guiGraphics, pMouseX, pMouseY);
		this.renderEnergyLevel(guiGraphics, pMouseX, pMouseY);
	}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
		int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;
        guiGraphics.blit(GUI, relX, relY, 0, 0, 196, this.imageHeight);
        this.renderEnergyBar(guiGraphics, OVERLAY);
	}

}
