package com.the_millman.miningutils.core.data.providers;

import java.util.function.BiConsumer;

import com.the_millman.miningutils.core.init.BlockEntityInit;
import com.the_millman.miningutils.core.init.BlockInit;
import com.the_millman.themillmanlib.core.data.LibLootTables;

import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable.Builder;

public class ModLootTableProvider implements LootTableSubProvider  {

//	protected void addTables() {
//		lootTables.put(BlockInit.VERTICAL_MINER.get(), createItemEnergyTable("vertical_miner", BlockInit.VERTICAL_MINER.get(), BlockEntityInit.VERTICAL_MINER.get()));
//		lootTables.put(BlockInit.BLOCK_BREAKER.get(), createItemEnergyTable("block_breaker", BlockInit.BLOCK_BREAKER.get(), BlockEntityInit.BLOCK_BREAKER.get()));
//		lootTables.put(BlockInit.BLOCK_PLACER.get(), createItemEnergyTable("block_placer", BlockInit.BLOCK_PLACER.get(), BlockEntityInit.BLOCK_PLACER.get()));
//		
//	}
	
	@Override
	public void generate(BiConsumer<ResourceLocation, Builder> builder) {
		builder.accept(BlockInit.VERTICAL_MINER.getId(), LibLootTables.createItemEnergyTable("vertical_miner", BlockInit.VERTICAL_MINER.get(), BlockEntityInit.VERTICAL_MINER.get()));
		builder.accept(BlockInit.BLOCK_BREAKER.getId(), LibLootTables.createItemEnergyTable("block_breaker", BlockInit.BLOCK_BREAKER.get(), BlockEntityInit.BLOCK_BREAKER.get()));
		builder.accept(BlockInit.BLOCK_PLACER.getId(), LibLootTables.createItemEnergyTable("block_placer", BlockInit.BLOCK_PLACER.get(), BlockEntityInit.BLOCK_PLACER.get()));
		builder.accept(BlockInit.HOG_PAN.getId(), LibLootTables.createSimpleTable("hog_pan", BlockInit.HOG_PAN.get()));
		builder.accept(BlockInit.BUCKET_BLOCK.getId(), LibLootTables.createSimpleTable("bucket_block", BlockInit.BUCKET_BLOCK.get()));
		builder.accept(BlockInit.WAVE_TABLE.getId(), LibLootTables.createSimpleTable("wave_table", BlockInit.WAVE_TABLE.get()));
		
	}
}
