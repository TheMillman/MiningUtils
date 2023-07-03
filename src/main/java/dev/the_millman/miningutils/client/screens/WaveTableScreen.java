package dev.the_millman.miningutils.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;

import dev.the_millman.miningutils.common.containers.WaveTableContainer;
import dev.the_millman.miningutils.core.util.MiningConfig;
import dev.the_millman.miningutils.core.util.MiningUtilsResources;
import dev.the_millman.themillmanlib.client.screens.ItemEnergyFluidScreen;
import dev.the_millman.themillmanlib.core.util.LibResources;
import net.minecraft.client.gui.GuiGraphics;
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
	
	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int pMouseX, int pMouseY) {
		int xPos = (width - imageWidth) / 2;
        int yPos = (height - imageHeight) / 2;
        super.renderLabels(guiGraphics, pMouseX, pMouseY);
        renderFluidTooltip(guiGraphics, pMouseX, pMouseY, 134, 146, 18, 69, menu.getFluidStack(), xPos, yPos, "tooltip.themillmanlib.fluid_amount_capacity", "tooltip.themillmanlib.fluid_amount");
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
		RenderSystem.setShaderTexture(0, GUI);
        int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;
        guiGraphics.blit(GUI, relX, relY, 0, 0, this.imageWidth, this.imageHeight);
        renderer.render(guiGraphics.pose(), relX + 134, relY + 18, menu.getFluidStack());
        this.renderEnergyBar(guiGraphics, OVERLAY);
        renderProgress(guiGraphics);
	}

	private void renderProgress(GuiGraphics guiGraphics) {
		int gL = this.getGuiLeft();
		int gT = this.getGuiTop();
		int progress = (int)(23*menu.getProgress()/(float)menu.getMaxProgress());
		guiGraphics.blit(GUI, gL + 67, gT + 36, 176, 0, progress+1, 16);
	}
}
