package com.the_millman.miningutils.core.data;

import java.util.concurrent.CompletableFuture;

import com.the_millman.miningutils.MiningUtils;
import com.the_millman.miningutils.core.data.providers.ModBlockTagsProvider;
import com.the_millman.miningutils.core.data.providers.ModItemModelProvider;
import com.the_millman.miningutils.core.data.providers.ModItemTagsProvider;
import com.the_millman.miningutils.core.data.providers.ModRecipeProvider;

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
		gen.addProvider(event.includeServer(), new ModItemTagsProvider(packOutput, lookupProvider, tagProvider, event.getExistingFileHelper()));

		gen.addProvider(event.includeClient(), new ModItemModelProvider(packOutput, event.getExistingFileHelper()));
		// gen.addProvider(event.includeClient(), new BlockStateProviders(gen,
		// exFileHelper));
	}
}
