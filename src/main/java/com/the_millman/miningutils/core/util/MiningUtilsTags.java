package com.the_millman.miningutils.core.util;

import com.the_millman.miningutils.MiningUtils;
import com.the_millman.themillmanlib.core.util.LibTags;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class MiningUtilsTags {
	public class ModBlockTags extends LibTags.Blocks {
		public static final TagKey<Block> MINEABLE_PICKAXE = mod("mineable/pickaxe");
		public static final TagKey<Block> SIFTABLE_BLOCKS = mod("siftable_blocks");
		
		private static TagKey<Block> mod(String name) {
			return modLoc(MiningUtils.MODID, name);
		}
	}
	
	public class ModItemTags extends LibTags.Items{
		public static final TagKey<Item> SIFTABLE_BLOCKS = mod("siftable_blocks");
		
		private static TagKey<Item> mod(String name) {
			return modLoc(MiningUtils.MODID, name);
		}
	}
}