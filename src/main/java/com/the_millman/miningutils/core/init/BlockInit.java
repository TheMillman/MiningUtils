package com.the_millman.miningutils.core.init;

import com.the_millman.miningutils.MiningUtils;
import com.the_millman.miningutils.common.blocks.BlockBreakerBlock;
import com.the_millman.miningutils.common.blocks.BlockPlacerBlock;
import com.the_millman.miningutils.common.blocks.BucketBlock;
import com.the_millman.miningutils.common.blocks.HogPanBlock;
import com.the_millman.miningutils.common.blocks.VerticalMinerBlock;
import com.the_millman.miningutils.common.blocks.WaveTableBlock;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockInit {
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MiningUtils.MODID);
	
	public static final RegistryObject<Block> VERTICAL_MINER = BLOCKS.register("vertical_miner", () -> new VerticalMinerBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
	public static final RegistryObject<Block> BLOCK_BREAKER = BLOCKS.register("block_breaker", () -> new BlockBreakerBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
	public static final RegistryObject<Block> BLOCK_PLACER = BLOCKS.register("block_placer", () -> new BlockPlacerBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
	
	public static final RegistryObject<Block> HOG_PAN = BLOCKS.register("hog_pan", () -> new HogPanBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
	public static final RegistryObject<Block> BUCKET_BLOCK = BLOCKS.register("bucket_block", () -> new BucketBlock(BlockBehaviour.Properties.of(Material.STONE).strength(0.5F).sound(SoundType.BAMBOO)));
	public static final RegistryObject<Block> WAVE_TABLE = BLOCKS.register("wave_table", () -> new WaveTableBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
	
}
