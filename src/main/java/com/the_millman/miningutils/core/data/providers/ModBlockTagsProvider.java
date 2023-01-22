package com.the_millman.miningutils.core.data.providers;

import java.util.concurrent.CompletableFuture;

import org.jetbrains.annotations.Nullable;

import com.the_millman.miningutils.MiningUtils;
import com.the_millman.miningutils.core.init.BlockInit;
import com.the_millman.miningutils.core.util.MiningUtilsTags.ModBlockTags;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockTagsProvider extends BlockTagsProvider {

	public ModBlockTagsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
		super(packOutput, lookupProvider, MiningUtils.MODID, existingFileHelper);
	}

	@Override
	protected void addTags(Provider pProvider) {
		tag(ModBlockTags.SIFTABLE_BLOCKS).add(Blocks.DIRT).add(Blocks.MUD);
		
		tag(ModBlockTags.MINEABLE_PICKAXE).add(BlockInit.VERTICAL_MINER.get(), BlockInit.BLOCK_BREAKER.get(), BlockInit.BLOCK_PLACER.get(), 
				BlockInit.HOG_PAN.get()).add(BlockInit.HOG_PAN.get()).add(BlockInit.WAVE_TABLE.get());
		
		tag(BlockTags.MINEABLE_WITH_PICKAXE)	
			.addTag(ModBlockTags.MINEABLE_PICKAXE);
		
		tag(BlockTags.NEEDS_STONE_TOOL)
			.addTag(ModBlockTags.MINEABLE_PICKAXE);
	}
	
	@Override
	public String getName() {
		return "MiningUtils Block Tags";
	}
}
