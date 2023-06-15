package dev.the_millman.miningutils.core.init;

import dev.the_millman.miningutils.MiningUtils;
import dev.the_millman.miningutils.common.blockentity.BlockBreakerBE;
import dev.the_millman.miningutils.common.blockentity.BlockPlacerBE;
import dev.the_millman.miningutils.common.blockentity.HogPanBE;
import dev.the_millman.miningutils.common.blockentity.VerticalMinerBE;
import dev.the_millman.miningutils.common.blockentity.WaveTableBE;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntityInit {
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MiningUtils.MODID);
	
	public static final RegistryObject<BlockEntityType<VerticalMinerBE>> VERTICAL_MINER = BLOCK_ENTITIES.register("vertical_miner", () -> BlockEntityType.Builder.of(VerticalMinerBE::new, BlockInit.VERTICAL_MINER.get()).build(null));
	public static final RegistryObject<BlockEntityType<BlockBreakerBE>> BLOCK_BREAKER = BLOCK_ENTITIES.register("block_breaker", () -> BlockEntityType.Builder.of(BlockBreakerBE::new, BlockInit.BLOCK_BREAKER.get()).build(null));
	public static final RegistryObject<BlockEntityType<BlockPlacerBE>> BLOCK_PLACER = BLOCK_ENTITIES.register("block_placer", () -> BlockEntityType.Builder.of(BlockPlacerBE::new, BlockInit.BLOCK_PLACER.get()).build(null));
	public static final RegistryObject<BlockEntityType<HogPanBE>> HOG_PAN = BLOCK_ENTITIES.register("hog_pan", () -> BlockEntityType.Builder.of(HogPanBE::new, BlockInit.HOG_PAN.get()).build(null));
	public static final RegistryObject<BlockEntityType<WaveTableBE>> WAVE_TABLE = BLOCK_ENTITIES.register("wave_table", () -> BlockEntityType.Builder.of(WaveTableBE::new, BlockInit.WAVE_TABLE.get()).build(null));
	
}
