package com.the_millman.miningutils.core.init;

import com.the_millman.miningutils.MiningUtils;
import com.the_millman.miningutils.common.items.HogPanMatItem;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemInit {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MiningUtils.MODID);
//	private static final MiningUtilsTab TAB = MiningUtilsTab.MINING_UTILS;
	
	public static final RegistryObject<Item> VERTICAL_MINER = ITEMS.register("vertical_miner", () -> new BlockItem(BlockInit.VERTICAL_MINER.get(), new Item.Properties())); //.tab(TAB)));
	public static final RegistryObject<Item> BLOCK_BREAKER = ITEMS.register("block_breaker", () -> new BlockItem(BlockInit.BLOCK_BREAKER.get(), new Item.Properties())); //.tab(TAB)));
	public static final RegistryObject<Item> BLOCK_PLACER = ITEMS.register("block_placer", () -> new BlockItem(BlockInit.BLOCK_PLACER.get(), new Item.Properties())); //.tab(TAB)));
	public static final RegistryObject<Item> HOG_PAN = ITEMS.register("hog_pan", () -> new BlockItem(BlockInit.HOG_PAN.get(), new Item.Properties()));
	public static final RegistryObject<Item> BUCKET_BLOCK = ITEMS.register("bucket_block", () -> new BlockItem(BlockInit.BUCKET_BLOCK.get(), new Item.Properties()));
	public static final RegistryObject<Item> WAVE_TABLE = ITEMS.register("wave_table", () -> new BlockItem(BlockInit.WAVE_TABLE.get(), new Item.Properties()));
	
	public static final RegistryObject<Item> HOG_PAN_MAT = ITEMS.register("hog_pan_mat", () -> new HogPanMatItem(new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> DIRTY_WATER_BUCKET = ITEMS.register("dirty_water_bucket", () -> new Item(new Item.Properties().stacksTo(1)));
	
}
