package dev.the_millman.miningutils.core.data.providers;

import dev.the_millman.miningutils.MiningUtils;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {

	public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
		super(output, MiningUtils.MODID, existingFileHelper);
	}

	@Override
	protected void registerModels() {
		singleTexture("hog_pan_mat", mcLoc("item/generated"), "layer0", modLoc("item/hog_pan_mat"));
		singleTexture("dirty_water_bucket", mcLoc("item/generated"), "layer0", modLoc("item/dirty_water_bucket"));
		singleTexture("black_list_upgrade", mcLoc("item/generated"), "layer0", modLoc("item/black_list_upgrade"));
	}

}
