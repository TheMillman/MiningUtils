package dev.the_millman.miningutils.data.providers;

import java.util.concurrent.CompletableFuture;

import org.jetbrains.annotations.Nullable;

import dev.the_millman.miningutils.MiningUtils;
import dev.the_millman.miningutils.core.init.ItemInit;
import dev.the_millman.miningutils.core.util.MiningUtilsTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModItemTagsProvider extends ItemTagsProvider {

	public ModItemTagsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> pBlockTagsProvider, @Nullable ExistingFileHelper existingFileHelper) {
		super(packOutput, lookupProvider, pBlockTagsProvider, MiningUtils.MODID, existingFileHelper);
	}

	@Override
	protected void addTags(Provider pProvider) {
		tag(MiningUtilsTags.ModItemTags.SIFTABLE_BLOCKS).add(Items.DIRT, Items.MUD);
		tag(MiningUtilsTags.ModItemTags.BLACK_LIST_UPGRADE).add(ItemInit.BLACK_LIST_UPGRADE.get());
	}
}
