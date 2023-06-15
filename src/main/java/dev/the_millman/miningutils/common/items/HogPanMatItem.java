package dev.the_millman.miningutils.common.items;

import java.util.List;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class HogPanMatItem extends Item {
	
	public HogPanMatItem(Properties pProperties) {
		super(pProperties);
	}
	
	@Override
	public void appendHoverText(ItemStack pStack, Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
		if(pStack.hasTag()) {
			int contentTag = pStack.getTag().getInt("miningutils_content");
			pTooltipComponents.add(Component.translatable(contentTag + "%"));
		}
		
		super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
	}
}
