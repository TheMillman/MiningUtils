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
import dev.the_millman.miningutils.core.init.ItemInit;
import dev.the_millman.themillmanlib.core.init.LibItemInit;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.event.CreativeModeTabEvent;
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
	
	@Mod.EventBusSubscriber(modid = MiningUtils.MODID, bus = Bus.MOD)
	public static class CommonDistEvent {
		public static final String TAB_NAME = "mining_utils_tab";

		@SubscribeEvent
		public static void customTab(CreativeModeTabEvent.Register event) {
			event.registerCreativeModeTab(new ResourceLocation(MiningUtils.MODID, "mining_utils_tab"), builder -> {
				builder.title(Component.translatable("itemGroup." + TAB_NAME))
						.icon(() -> new ItemStack(ItemInit.VERTICAL_MINER.get()))
						.displayItems((enabledFeatures, output) -> {
							// Blocks
							output.accept(ItemInit.VERTICAL_MINER.get());
							output.accept(ItemInit.BLOCK_BREAKER.get());
							output.accept(ItemInit.BLOCK_PLACER.get());
							output.accept(ItemInit.HOG_PAN.get());
							output.accept(ItemInit.BUCKET_BLOCK.get());
							output.accept(ItemInit.WAVE_TABLE.get());
							
							// Items
							output.accept(ItemInit.HOG_PAN_MAT.get());
							output.accept(ItemInit.DIRTY_WATER_BUCKET.get());
							output.accept(ItemInit.BLACK_LIST_UPGRADE.get());
							output.accept(LibItemInit.PLASTIC.get());
							output.accept(LibItemInit.IRON_UPGRADE.get());
							output.accept(LibItemInit.GOLD_UPGRADE.get());
							output.accept(LibItemInit.DIAMOND_UPGRADE.get());
							output.accept(LibItemInit.REDSTONE_UPGRADE.get());
						});
			});
		}
	}
}
