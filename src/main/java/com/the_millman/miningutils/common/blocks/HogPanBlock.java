package com.the_millman.miningutils.common.blocks;

import java.util.stream.Stream;

import com.the_millman.miningutils.common.blockentity.HogPanBE;
import com.the_millman.miningutils.common.containers.HogPanContainer;
import com.the_millman.miningutils.core.networking.FluidSyncS2CPacket;
import com.the_millman.miningutils.core.networking.ModMessages;
import com.the_millman.themillmanlib.common.blocks.HorizontalDirectionBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;

public class HogPanBlock extends HorizontalDirectionBlock implements EntityBlock {

	private final VoxelShape SHAPE_N = Stream.of(Block.box(3, 1, 15, 13, 4.25, 16), Block.box(2.25, 0, 12, 3.25, 6, 13),
			Block.box(12.75, 0, 12, 13.75, 6, 13), Block.box(2.25, 4, 7, 2.75, 10, 8),
			Block.box(13.25, 4, 7, 13.75, 10, 8), Block.box(2, 10, 6, 14, 11, 16), Block.box(2.25, 7, 9, 13.75, 13, 10),
			Block.box(2, 11, 15, 14, 16, 16), Block.box(2, 11, 5, 3, 16, 15), Block.box(13, 11, 5, 14, 16, 15),
			Block.box(2, 1, 0, 3, 4.25, 16), Block.box(2, 0, 0, 14, 1, 16), Block.box(13, 1, 0, 14, 4.25, 16))
			.reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

	private final VoxelShape SHAPE_S = Stream.of(Block.box(3, 4.5, 2, 13, 7.75, 3), Block.box(12.75, 0, 3, 13.75, 6, 4),
			Block.box(2.25, 0, 3, 3.25, 6, 4), Block.box(13.25, 5, 8, 13.75, 11, 9), Block.box(2.25, 5, 8, 2.75, 11, 9),
			Block.box(2, 10, 1, 14, 11, 11), Block.box(2.25, 11, 9, 13.75, 17, 10), Block.box(2, 11, 1, 14, 16, 2),
			Block.box(13, 11, 2, 14, 16, 12), Block.box(2, 11, 2, 3, 16, 12), Block.box(13, 4.5, 2, 14, 7.75, 18),
			Block.box(2, 3.5, 2, 14, 4.5, 18), Block.box(2, 4.5, 2, 3, 7.75, 18))
			.reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

	private final VoxelShape SHAPE_W = Stream.of(Block.box(13, 4.5, 3, 14, 7.75, 13),
			Block.box(12, 0, 12.75, 13, 6, 13.75), Block.box(12, 0, 2.25, 13, 6, 3.25),
			Block.box(7, 5, 13.25, 8, 11, 13.75), Block.box(7, 5, 2.25, 8, 11, 2.75), Block.box(5, 10, 2, 15, 11, 14),
			Block.box(6, 11, 2.25, 7, 17, 13.75), Block.box(14, 11, 2, 15, 16, 14), Block.box(4, 11, 13, 14, 16, 14),
			Block.box(4, 11, 2, 14, 16, 3), Block.box(-2, 4.5, 13, 14, 7.75, 14), Block.box(-2, 3.5, 2, 14, 4.5, 14),
			Block.box(-2, 4.5, 2, 14, 7.75, 3)).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

	private final VoxelShape SHAPE_E = Stream
			.of(Block.box(2, 4.5, 3, 3, 7.75, 13), Block.box(3, 0, 2.25, 4, 6, 3.25),
					Block.box(3, 0, 12.75, 4, 6, 13.75), Block.box(8, 5, 2.25, 9, 11, 2.75),
					Block.box(8, 5, 13.25, 9, 11, 13.75), Block.box(1, 10, 2, 11, 11, 14),
					Block.box(9, 11, 2.25, 10, 17, 13.75), Block.box(1, 11, 2, 2, 16, 14),
					Block.box(2, 11, 2, 12, 16, 3), Block.box(2, 11, 13, 12, 16, 14), Block.box(2, 4.5, 2, 18, 7.75, 3),
					Block.box(2, 3.5, 2, 18, 4.5, 14), Block.box(2, 4.5, 13, 18, 7.75, 14))
			.reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
	
	public HogPanBlock(Properties pProperties) {
		super(pProperties);
		this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));
	}
	
	@Override
	public VoxelShape getOcclusionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
		switch(pState.getValue(FACING))
		{
		case NORTH:
			return SHAPE_N;
		case SOUTH:
			return SHAPE_S;
		case WEST:
			return SHAPE_W;
		case EAST:
			return SHAPE_E;
		default:
			return SHAPE_N;
		}
	}
	
	@Override
	public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
		if (!pLevel.isClientSide) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof HogPanBE tile) {
                MenuProvider containerProvider = new MenuProvider() {
                    @Override
                    public Component getDisplayName() {
                        return Component.translatable("screen.miningutils.hog_pan");
                    }

                    @Override
                    public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player playerEntity) {
                    	if(!pLevel.isClientSide()) {
        	                ModMessages.sendToClients(new FluidSyncS2CPacket(tile.getFluidStack(), tile.getBlockPos()));
        	            }
                    	
                        return new HogPanContainer(windowId, pLevel, pPos, playerInventory, playerEntity);
                    }
                };
                NetworkHooks.openScreen((ServerPlayer) pPlayer, containerProvider, blockEntity.getBlockPos());
            } else {
                throw new IllegalStateException("Our named container provider is missing!");
            }
        }
        return InteractionResult.SUCCESS;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
		if (pState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof HogPanBE) {
                ((HogPanBE) blockEntity).drops();
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
		return new HogPanBE(pPos, pState);
	}
	
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
		return (level1, pos, state1, tile) -> {
			if (tile instanceof HogPanBE blockEntity) {
				blockEntity.tickServer();
			}
		};
	}
}
