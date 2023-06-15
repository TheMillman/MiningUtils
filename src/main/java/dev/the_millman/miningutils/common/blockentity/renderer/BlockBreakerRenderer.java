package dev.the_millman.miningutils.common.blockentity.renderer;

import org.joml.Quaternionf;

import com.mojang.blaze3d.vertex.PoseStack;

import dev.the_millman.miningutils.common.blockentity.BlockBreakerBE;
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
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;

public class BlockBreakerRenderer implements BlockEntityRenderer<BlockBreakerBE> {

	public BlockBreakerRenderer(BlockEntityRendererProvider.Context context) {
		
	}

	@Override
	public void render(BlockBreakerBE pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
		ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
		ItemStack itemStack = Items.DIAMOND_PICKAXE.getDefaultInstance();
		pPoseStack.pushPose();
		pPoseStack.translate(0.0f, 0.0f, 0.0f);
		pPoseStack.scale(0.5F, 0.5F, 0.5F);
		
		Direction direction = pBlockEntity.getBlockState().getValue(BlockBreakerBlock.FACING);
		
		Quaternionf XP = new Quaternionf(1.0F, 0.0F, 0.0F, 1.0F);
		Quaternionf YP = new Quaternionf(0.0F, 1.0F, 0.0F, 1.0F);
		
		if (direction == Direction.NORTH) {
			pPoseStack.mulPose(XP.rotationX(0));
			pPoseStack.translate(0.9f, 0.65f, 1.35f);
		} else if (direction == Direction.SOUTH) {
			pPoseStack.mulPose(XP.rotationX(0));
			pPoseStack.translate(1.0f, 0.65f, 0.55f);
		} else if (direction == Direction.WEST) {
			pPoseStack.mulPose(XP.rotationX(0.0F));
			pPoseStack.mulPose(YP.rotationY(4.7F));
			pPoseStack.translate(1f, 0.65f, -1.35f);
		} else {
			pPoseStack.mulPose(XP.rotationX(0));
			pPoseStack.mulPose(YP.rotationY(4.7F));
			pPoseStack.translate(0.9f, 0.65f, -0.65f);
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
