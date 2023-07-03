package dev.the_millman.miningutils.data;

import java.util.concurrent.CompletableFuture;

import dev.the_millman.miningutils.MiningUtils;
import dev.the_millman.miningutils.data.providers.ModBlockTagsProvider;
import dev.the_millman.miningutils.data.providers.ModItemModelProvider;
import dev.the_millman.miningutils.data.providers.ModItemTagsProvider;
import dev.the_millman.miningutils.data.providers.ModRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = MiningUtils.MODID, bus = Bus.MOD)
public class DataGenerators {
	private DataGenerators() {
	}

	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator gen = event.getGenerator();
		PackOutput packOutput = gen.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        
		gen.addProvider(event.includeServer(), new ModRecipeProvider(packOutput));
//		gen.addProvider(event.includeServer(), new LootTableProvider(packOutput, Collections.emptySet(),
//                List.of(new LootTableProvider.SubProviderEntry(ModLootTableProvider::new, LootContextParamSets.BLOCK))));
		ModBlockTagsProvider tagProvider = new ModBlockTagsProvider(packOutput, lookupProvider, event.getExistingFileHelper());
		gen.addProvider(event.includeServer(), tagProvider);
		gen.addProvider(event.includeServer(), new ModItemTagsProvider(packOutput, lookupProvider, tagProvider.contentsGetter(), event.getExistingFileHelper()));

		gen.addProvider(event.includeClient(), new ModItemModelProvider(packOutput, event.getExistingFileHelper()));
		// gen.addProvider(event.includeClient(), new BlockStateProviders(gen,
		// exFileHelper));
	}
}
