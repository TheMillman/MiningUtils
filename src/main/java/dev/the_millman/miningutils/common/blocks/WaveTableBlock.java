package dev.the_millman.miningutils.common.blocks;

import java.util.stream.Stream;

import dev.the_millman.miningutils.common.blockentity.WaveTableBE;
import dev.the_millman.miningutils.common.containers.WaveTableContainer;
import dev.the_millman.miningutils.core.networking.FluidSyncS2CPacket;
import dev.the_millman.miningutils.core.networking.ModMessages;
import dev.the_millman.themillmanlib.common.blocks.HorizontalDirectionBlock;
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
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;

public class WaveTableBlock extends HorizontalDirectionBlock implements EntityBlock {

	public static final BooleanProperty WATER = BooleanProperty.create("water");
	
	private static final VoxelShape SHAPE_N = Stream.of(Block.box(6, 13, 0, 10, 15, 1),
			Block.box(14, 0, 15, 16, 10, 16), Block.box(0, 0, 15, 2, 10, 16), Block.box(0, 0, 0, 2, 10, 1),
			Block.box(14, 0, 0, 16, 10, 1), Block.box(15, 0, 14, 16, 10, 15), Block.box(0, 0, 14, 1, 10, 15),
			Block.box(0, 0, 1, 1, 10, 2), Block.box(15, 0, 1, 16, 10, 2), Block.box(0, 10, 0, 16, 11, 16),
			Block.box(0, 11, 15, 16, 13, 16), Block.box(0, 11, 0, 16, 13, 1), Block.box(15, 11, 1, 16, 13, 15),
			Block.box(0, 11, 1, 1, 13, 15), Block.box(3, 12, 2, 13, 15, 6), Block.box(2, 14, 6, 14, 16, 7),
			Block.box(2, 14, 1, 14, 16, 2), Block.box(13, 14, 2, 14, 16, 6), Block.box(2, 14, 2, 3, 16, 6))
			.reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

	private static final VoxelShape SHAPE_S = Stream.of(Block.box(6, 13, 15, 10, 15, 16), Block.box(0, 0, 0, 2, 10, 1),
			Block.box(14, 0, 0, 16, 10, 1), Block.box(14, 0, 15, 16, 10, 16), Block.box(0, 0, 15, 2, 10, 16),
			Block.box(0, 0, 1, 1, 10, 2), Block.box(15, 0, 1, 16, 10, 2), Block.box(15, 0, 14, 16, 10, 15),
			Block.box(0, 0, 14, 1, 10, 15), Block.box(0, 10, 0, 16, 11, 16), Block.box(0, 11, 0, 16, 13, 1),
			Block.box(0, 11, 15, 16, 13, 16), Block.box(0, 11, 1, 1, 13, 15), Block.box(15, 11, 1, 16, 13, 15),
			Block.box(3, 12, 10, 13, 15, 14), Block.box(2, 14, 9, 14, 16, 10), Block.box(2, 14, 14, 14, 16, 15),
			Block.box(2, 14, 10, 3, 16, 14), Block.box(13, 14, 10, 14, 16, 14))
			.reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

	private static final VoxelShape SHAPE_W = Stream.of(Block.box(0, 13, 6, 1, 15, 10), Block.box(15, 0, 0, 16, 10, 2),
			Block.box(15, 0, 14, 16, 10, 16), Block.box(0, 0, 14, 1, 10, 16), Block.box(0, 0, 0, 1, 10, 2),
			Block.box(14, 0, 0, 15, 10, 1), Block.box(14, 0, 15, 15, 10, 16), Block.box(1, 0, 15, 2, 10, 16),
			Block.box(1, 0, 0, 2, 10, 1), Block.box(0, 10, 0, 16, 11, 16), Block.box(15, 11, 0, 16, 13, 16),
			Block.box(0, 11, 0, 1, 13, 16), Block.box(1, 11, 0, 15, 13, 1), Block.box(1, 11, 15, 15, 13, 16),
			Block.box(2, 12, 3, 6, 15, 13), Block.box(6, 14, 2, 7, 16, 14), Block.box(1, 14, 2, 2, 16, 14),
			Block.box(2, 14, 2, 6, 16, 3), Block.box(2, 14, 13, 6, 16, 14))
			.reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
	
	private static final VoxelShape SHAPE_E = Stream.of(Block.box(15, 13, 6, 16, 15, 10), Block.box(0, 0, 14, 1, 10, 16), 
			Block.box(0, 0, 0, 1, 10, 2), Block.box(15, 0, 0, 16, 10, 2), Block.box(15, 0, 14, 16, 10, 16), 
			Block.box(1, 0, 15, 2, 10, 16), Block.box(1, 0, 0, 2, 10, 1), Block.box(14, 0, 0, 15, 10, 1), 
			Block.box(14, 0, 15, 15, 10, 16), Block.box(0, 10, 0, 16, 11, 16), Block.box(0, 11, 0, 1, 13, 16), 
			Block.box(15, 11, 0, 16, 13, 16), Block.box(1, 11, 15, 15, 13, 16), Block.box(1, 11, 0, 15, 13, 1), 
			Block.box(10, 12, 3, 14, 15, 13), Block.box(9, 14, 2, 10, 16, 14), Block.box(14, 14, 2, 15, 16, 14), 
			Block.box(10, 14, 13, 14, 16, 14), Block.box(10, 14, 2, 14, 16, 3))
			.reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
	
	public WaveTableBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH).setValue(WATER, false));
	}

	@Override
	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		switch(pState.getValue(FACING)) {
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
            if (blockEntity instanceof WaveTableBE tile) {
                MenuProvider containerProvider = new MenuProvider() {
                    @Override
                    public Component getDisplayName() {
                        return Component.translatable("screen.miningutils.wave_table");
                    }

                    @Override
                    public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player playerEntity) {
                    	if(!pLevel.isClientSide()) {
        	                ModMessages.sendToClients(new FluidSyncS2CPacket(tile.getFluidStack(), tile.getBlockPos()));
        	            }
                    	
                        return new WaveTableContainer(windowId, pLevel, pPos, playerInventory, playerEntity);
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
            if (blockEntity instanceof WaveTableBE) {
                ((WaveTableBE) blockEntity).drops();
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
		return new WaveTableBE(pPos, pState);
	}
	
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
		return (level1, pos, state1, tile) -> {
			if (tile instanceof WaveTableBE blockEntity) {
				blockEntity.tickServer();
				pLevel.setBlock(pos, pState.setValue(WATER, blockEntity.getWater()), UPDATE_ALL);
			}
		};
	}
	
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> pBuilder) {
		super.createBlockStateDefinition(pBuilder);
		pBuilder.add(WATER);
	}
}
