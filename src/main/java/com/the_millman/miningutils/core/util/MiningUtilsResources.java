package com.the_millman.miningutils.core.util;

import com.the_millman.miningutils.MiningUtils;
import com.the_millman.themillmanlib.core.util.LibResources;
import com.the_millman.themillmanlib.core.util.ResourcesRegister;

import net.minecraft.resources.ResourceLocation;

public class MiningUtilsResources extends ResourcesRegister {
	public static final ResourceLocation VERTICAL_MINER_GUI = modLoc(MiningUtils.MODID, "vertical_miner_gui");
	public static final ResourceLocation BLOCK_BREAKER_GUI = LibResources.THREE_FOR_THREE_GUI;
	public static final ResourceLocation BLOCK_PLACER_GUI = LibResources.THREE_FOR_THREE_GUI;
	public static final ResourceLocation HOG_PAN_GUI = modLoc(MiningUtils.MODID, "hog_pan_gui");
	public static final ResourceLocation WAVE_TABLE_GUI = modLoc(MiningUtils.MODID, "wave_table_gui");
	
	public static final ResourceLocation OFFSET = LibResources.OFFSETS;
}
