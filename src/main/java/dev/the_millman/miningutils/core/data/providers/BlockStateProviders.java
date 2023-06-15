package dev.the_millman.miningutils.core.data.providers;

import dev.the_millman.miningutils.MiningUtils;
import dev.the_millman.miningutils.core.init.BlockInit;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockStateProviders extends BlockStateProvider {

	public BlockStateProviders(PackOutput packOutput, ExistingFileHelper exFileHelper) {
		super(packOutput, MiningUtils.MODID, exFileHelper);
	}

	@Override
	protected void registerStatesAndModels() {
		horizontalBlock(BlockInit.VERTICAL_MINER.get(), models().orientableWithBottom("vertical_miner", modLoc("block/vertical_miner_top"), modLoc("block/vertical_miner_side"), modLoc("block/vertical_miner"), modLoc("block/vertical_miner_top")));
		
	}

}
