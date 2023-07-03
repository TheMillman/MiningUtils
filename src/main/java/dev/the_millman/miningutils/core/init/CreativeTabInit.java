package dev.the_millman.miningutils.core.init;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

import dev.the_millman.miningutils.MiningUtils;
import dev.the_millman.themillmanlib.core.init.LibItemInit;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = MiningUtils.MODID, bus = Bus.MOD, value = Dist.CLIENT)
public class CreativeTabInit {
	public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MiningUtils.MODID);
	
	public static final List<Supplier<? extends ItemLike>> MINING_TAB_LIST = new ArrayList<>();
	
	public static final RegistryObject<CreativeModeTab> MINING_UTILS_TAB = CREATIVE_TABS
		.register("mining_utils_tab", () -> CreativeModeTab.builder()
			.title(Component.translatable("itemGroup.mining_utils_tab"))
			.icon(ItemInit.VERTICAL_MINER.get()::getDefaultInstance)
			.displayItems((displayParams, output) -> MINING_TAB_LIST.forEach(itemLike -> 
				output.accept(itemLike.get())))
			.build());
	
	public static <T extends Item> RegistryObject<T> addToTab(RegistryObject<T> itemLike) {
		MINING_TAB_LIST.add(itemLike);
		return itemLike;
	}
	
	@SubscribeEvent
	public static void customTab(BuildCreativeModeTabContentsEvent event) {
		if (event.getTab() == MINING_UTILS_TAB.get()) {
			Stream<Item> hello = LibItemInit.ITEMS.getEntries().stream().map(RegistryObject::get);
			hello.forEach(item -> {
				event.accept(item);
			});
		}
	}
}
