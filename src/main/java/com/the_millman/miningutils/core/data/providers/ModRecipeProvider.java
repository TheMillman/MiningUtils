package com.the_millman.miningutils.core.data.providers;

import java.util.function.Consumer;

import com.the_millman.miningutils.core.init.BlockInit;
import com.the_millman.miningutils.core.init.ItemInit;
import com.the_millman.themillmanlib.core.init.LibItemInit;

import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;

public class ModRecipeProvider extends RecipeProvider {

	public ModRecipeProvider(PackOutput packOutput) {
		super(packOutput);
	}
	
	@Override
	protected void buildRecipes(Consumer<FinishedRecipe> pWriter) {
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockInit.VERTICAL_MINER.get())
        .pattern("iii")
        .pattern("ipi")
        .pattern("iri")
        .define('p', Items.DIAMOND_PICKAXE)
        .define('i', Tags.Items.INGOTS_IRON)
        .define('r', Items.REDSTONE)
        .unlockedBy("diamond_pickaxe", InventoryChangeTrigger.TriggerInstance.hasItems(Items.DIAMOND_PICKAXE))
        .save(pWriter);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockInit.BLOCK_BREAKER.get())
        .pattern("iii")
        .pattern("ipi")
        .pattern("iri")
        .define('p', Items.DIAMOND_PICKAXE)
        .define('i', Tags.Items.INGOTS_IRON)
        .define('r', Items.REDSTONE_BLOCK)
        .unlockedBy("diamond_pickaxe", InventoryChangeTrigger.TriggerInstance.hasItems(Items.DIAMOND_PICKAXE))
        .save(pWriter);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockInit.BLOCK_PLACER.get())
        .pattern("iii")
        .pattern("ipi")
        .pattern("iri")
        .define('p', Items.PISTON)
        .define('i', Tags.Items.INGOTS_IRON)
        .define('r', Items.REDSTONE)
        .unlockedBy("piston", InventoryChangeTrigger.TriggerInstance.hasItems(Items.PISTON))
        .save(pWriter);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockInit.HOG_PAN.get())
        .pattern("b  ")
        .pattern("iii")
        .define('b', Tags.Items.STORAGE_BLOCKS_IRON)
        .define('i', Tags.Items.INGOTS_IRON)
        .unlockedBy("iron", InventoryChangeTrigger.TriggerInstance.hasItems(Items.IRON_INGOT))
        .save(pWriter);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockInit.WAVE_TABLE.get())
        .pattern("w  ")
        .pattern("iii")
        .pattern("i i")
        .define('w', Items.WHITE_WOOL)
        .define('i', Tags.Items.INGOTS_IRON)
        .unlockedBy("iron", InventoryChangeTrigger.TriggerInstance.hasItems(Items.IRON_INGOT))
        .save(pWriter);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemInit.HOG_PAN_MAT.get())
        .pattern("ppp")
        .define('p', LibItemInit.PLASTIC.get())
        .unlockedBy("plastic", InventoryChangeTrigger.TriggerInstance.hasItems(LibItemInit.PLASTIC.get()))
        .save(pWriter);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockInit.BUCKET_BLOCK.get())
        .pattern("p p")
        .pattern(" p ")
        .define('p', LibItemInit.PLASTIC.get())
        .unlockedBy("plastic", InventoryChangeTrigger.TriggerInstance.hasItems(LibItemInit.PLASTIC.get()))
        .save(pWriter);
	}
}
