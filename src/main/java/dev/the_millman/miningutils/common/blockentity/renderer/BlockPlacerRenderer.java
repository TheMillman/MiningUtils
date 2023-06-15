package dev.the_millman.miningutils.common.blockentity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;

import dev.the_millman.miningutils.common.blockentity.BlockPlacerBE;
import dev.the_millman.miningutils.common.blocks.BlockBreakerBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;

public class BlockPlacerRenderer implements BlockEntityRenderer<BlockPlacerBE> {

	public BlockPlacerRenderer(BlockEntityRendererProvider.Context context) {
		
	}
	
	@Override
	public void render(BlockPlacerBE pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
		ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
		ItemStack itemStack = pBlockEntity.getRenderStack();
		pPoseStack.pushPose();
//		pPoseStack.translate(0.0f, 0.0f, 0.0f);
		pPoseStack.scale(0.4F, 0.4F, 0.4F);
		
		Direction direction = pBlockEntity.getBlockState().getValue(BlockBreakerBlock.FACING);
		if (direction == Direction.NORTH) {
			pPoseStack.translate(1.25f, 0.9f, 1.6f);
		} else if (direction == Direction.SOUTH) {
			pPoseStack.translate(1.25f, 0.9f, 0.85f);
		} else if (direction == Direction.WEST) {
			pPoseStack.translate(1.65f, 0.9f, 1.25f);
		} else {
			pPoseStack.translate(0.85f, 0.9f, 1.25f);
		}
		
		itemRenderer.renderStatic(itemStack, ItemDisplayContext.FIXED,
				getLightLevel(pBlockEntity.getLevel(), pBlockEntity.getBlockPos()), OverlayTexture.NO_OVERLAY,
				pPoseStack, pBufferSource, pBlockEntity.getLevel(), 1);
		pPoseStack.popPose();
	}

	@SuppressWarnings("unused")
	private int getLightLevel(Level level, BlockPos pos) {
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(15, sLight);
    }
}
