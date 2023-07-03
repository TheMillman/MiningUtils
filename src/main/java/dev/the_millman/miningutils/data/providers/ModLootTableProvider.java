package dev.the_millman.miningutils.data.providers;

import java.util.function.BiConsumer;

import dev.the_millman.miningutils.core.init.BlockEntityInit;
import dev.the_millman.miningutils.core.init.BlockInit;
import dev.the_millman.themillmanlib.data.LibLootTables;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTable.Builder;
import net.minecraft.world.level.storage.loot.entries.DynamicLoot;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyNameFunction;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.functions.SetContainerContents;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

public class ModLootTableProvider implements LootTableSubProvider  {

//	protected void addTables() {
//		lootTables.put(BlockInit.VERTICAL_MINER.get(), createItemEnergyTable("vertical_miner", BlockInit.VERTICAL_MINER.get(), BlockEntityInit.VERTICAL_MINER.get()));
//		lootTables.put(BlockInit.BLOCK_BREAKER.get(), createItemEnergyTable("block_breaker", BlockInit.BLOCK_BREAKER.get(), BlockEntityInit.BLOCK_BREAKER.get()));
//		lootTables.put(BlockInit.BLOCK_PLACER.get(), createItemEnergyTable("block_placer", BlockInit.BLOCK_PLACER.get(), BlockEntityInit.BLOCK_PLACER.get()));
//		
//	}
	
	@Override
	public void generate(BiConsumer<ResourceLocation, Builder> builder) {
		builder.accept(BlockInit.VERTICAL_MINER.getId(), createSpecialItemEnergyTable("vertical_miner", BlockInit.VERTICAL_MINER.get(), BlockEntityInit.VERTICAL_MINER.get(), "FilterInventory", "BlockEntityTag.FilterInventory"));
		builder.accept(BlockInit.BLOCK_BREAKER.getId(), LibLootTables.createItemEnergyTable("block_breaker", BlockInit.BLOCK_BREAKER.get(), BlockEntityInit.BLOCK_BREAKER.get()));
		builder.accept(BlockInit.BLOCK_PLACER.getId(), LibLootTables.createItemEnergyTable("block_placer", BlockInit.BLOCK_PLACER.get(), BlockEntityInit.BLOCK_PLACER.get()));
		builder.accept(BlockInit.HOG_PAN.getId(), LibLootTables.createSimpleTable("hog_pan", BlockInit.HOG_PAN.get()));
		builder.accept(BlockInit.BUCKET_BLOCK.getId(), LibLootTables.createSimpleTable("bucket_block", BlockInit.BUCKET_BLOCK.get()));
		builder.accept(BlockInit.WAVE_TABLE.getId(), LibLootTables.createSimpleTable("wave_table", BlockInit.WAVE_TABLE.get()));
	}
	
	public static LootTable.Builder createSpecialItemEnergyTable(String name, Block block, BlockEntityType<?> type, String keyName, String keyName2) {
        LootPool.Builder builder = LootPool.lootPool()
                .name(name)
                .setRolls(ConstantValue.exactly(1))
                .add(LootItem.lootTableItem(block)
                        .apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY))
                        .apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY)
                                .copy("Info", "BlockEntityTag.Info", CopyNbtFunction.MergeStrategy.REPLACE)
                                .copy("Inventory", "BlockEntityTag.Inventory", CopyNbtFunction.MergeStrategy.REPLACE)
                                .copy("UpgradeInventory", "BlockEntityTag.UpgradeInventory", CopyNbtFunction.MergeStrategy.REPLACE)
                                .copy("Energy", "BlockEntityTag.Energy", CopyNbtFunction.MergeStrategy.REPLACE)
                                .copy(keyName, keyName2, CopyNbtFunction.MergeStrategy.REPLACE))
                        .apply(SetContainerContents.setContents(type)
                                .withEntry(DynamicLoot.dynamicEntry(new ResourceLocation("minecraft", "contents"))))
                );
        return LootTable.lootTable().withPool(builder);
    }
}
