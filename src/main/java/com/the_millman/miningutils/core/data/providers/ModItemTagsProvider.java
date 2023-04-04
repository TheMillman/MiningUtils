package com.the_millman.miningutils.core.data.providers;

import java.util.concurrent.CompletableFuture;

import org.jetbrains.annotations.Nullable;

import com.the_millman.miningutils.MiningUtils;
import com.the_millman.miningutils.core.init.ItemInit;
import com.the_millman.miningutils.core.util.MiningUtilsTags;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModItemTagsProvider extends ItemTagsProvider {

	public ModItemTagsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, BlockTagsProvider pBlockTagsProvider, @Nullable ExistingFileHelper existingFileHelper) {
		super(packOutput, lookupProvider, pBlockTagsProvider, MiningUtils.MODID, existingFileHelper);
	}

	@Override
	protected void addTags(Provider pProvider) {
		tag(MiningUtilsTags.ModItemTags.SIFTABLE_BLOCKS).add(Items.DIRT, Items.MUD);
		tag(MiningUtilsTags.ModItemTags.BLACK_LIST_UPGRADE).add(ItemInit.BLACK_LIST_UPGRADE.get());
	}
}
