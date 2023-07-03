package dev.the_millman.miningutils.client;

import dev.the_millman.miningutils.MiningUtils;
import dev.the_millman.miningutils.client.screens.BlockBreakerScreen;
import dev.the_millman.miningutils.client.screens.BlockPlacerScreen;
import dev.the_millman.miningutils.client.screens.HogPanScreen;
import dev.the_millman.miningutils.client.screens.VerticalMinerScreen;
import dev.the_millman.miningutils.client.screens.WaveTableScreen;
import dev.the_millman.miningutils.common.blockentity.renderer.BlockBreakerRenderer;
import dev.the_millman.miningutils.common.blockentity.renderer.BlockPlacerRenderer;
import dev.the_millman.miningutils.core.init.BlockEntityInit;
import dev.the_millman.miningutils.core.init.BlockInit;
import dev.the_millman.miningutils.core.init.ContainerInit;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientEvents {

	@Mod.EventBusSubscriber(modid = MiningUtils.MODID, bus = Bus.MOD, value = Dist.CLIENT)
	public class ClientSetup {
		@SubscribeEvent
		public static void screensSetup(FMLClientSetupEvent event) {
			MenuScreens.register(ContainerInit.VERTICAL_MINER_CONTAINER.get(), VerticalMinerScreen::new);
			MenuScreens.register(ContainerInit.BLOCK_BREAKER_CONTAINER.get(), BlockBreakerScreen::new);
			MenuScreens.register(ContainerInit.BLOCK_PLACER_CONTAINER.get(), BlockPlacerScreen::new);
			MenuScreens.register(ContainerInit.HOG_PAN_CONTAINER.get(), HogPanScreen::new);
			MenuScreens.register(ContainerInit.WAVE_TABLE_CONTAINER.get(), WaveTableScreen::new);
		}
		
		@SubscribeEvent
		public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
			event.registerBlockEntityRenderer(BlockEntityInit.BLOCK_BREAKER.get(), BlockBreakerRenderer::new);
			event.registerBlockEntityRenderer(BlockEntityInit.BLOCK_PLACER.get(), BlockPlacerRenderer::new);
		}
		
		@SubscribeEvent
		public static void blockColorsEvent(final RegisterColorHandlersEvent.Block event) {
			event.register((state, world, pos, tintIndex) -> BiomeColors.getAverageWaterColor(world, pos), BlockInit.BUCKET_BLOCK.get(), BlockInit.WAVE_TABLE.get());
		}
	}
}
