package com.the_millman.miningutils.common.blocks;

import java.util.stream.Stream;

import com.the_millman.miningutils.common.items.HogPanMatItem;
import com.the_millman.miningutils.core.init.ItemInit;
import com.the_millman.themillmanlib.common.blocks.HorizontalDirectionBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BucketBlock extends HorizontalDirectionBlock {

	public static final BooleanProperty WATER = BooleanProperty.create("water");
	public static final BooleanProperty DIRTY = BooleanProperty.create("dirty");
	
	private static final VoxelShape SHAPE_N = Stream
			.of(Block.box(7, 3, 12.000000000000004, 9, 4, 13.000000000000004),
					Block.box(12, 7, 9.000000000000004, 13, 8, 10.000000000000004),
					Block.box(12, 6, 10.000000000000004, 13, 7, 12.000000000000004),
					Block.box(3, 6, 10.000000000000004, 4, 7, 12.000000000000004),
					Block.box(3, 7, 9.000000000000004, 4, 8, 10.000000000000004),
					Block.box(11, 5, 12.000000000000004, 13, 6, 13.000000000000004),
					Block.box(3, 5, 12.000000000000004, 5, 6, 13.000000000000004),
					Block.box(5, 4, 12.000000000000004, 7, 5, 13.000000000000004),
					Block.box(9, 4, 12.000000000000004, 11, 5, 13.000000000000004),
					Block.box(3, 8, 7.0000000000000036, 4, 9, 9.000000000000004),
					Block.box(4, 0, 4.0000000000000036, 12, 1, 12.000000000000004),
					Block.box(11, 1, 4.0000000000000036, 12, 11, 12.000000000000004),
					Block.box(4, 1, 4.0000000000000036, 5, 11, 12.000000000000004),
					Block.box(5, 1, 11.000000000000004, 11, 11, 12.000000000000004),
					Block.box(5, 1, 4.0000000000000036, 11, 11, 5.0000000000000036),
					Block.box(12, 8, 7.0000000000000036, 13, 9, 9.000000000000004))
			.reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
	
	private static final VoxelShape SHAPE_S = Stream.of(Block.box(7, 3, 3, 9, 4, 4), Block.box(3, 7, 6, 4, 8, 7),
			Block.box(3, 6, 4, 4, 7, 6), Block.box(12, 6, 4, 13, 7, 6), Block.box(12, 7, 6, 13, 8, 7),
			Block.box(3, 5, 3, 5, 6, 4), Block.box(11, 5, 3, 13, 6, 4), Block.box(9, 4, 3, 11, 5, 4),
			Block.box(5, 4, 3, 7, 5, 4), Block.box(12, 8, 7, 13, 9, 9), Block.box(4, 0, 4, 12, 1, 12),
			Block.box(4, 1, 4, 5, 11, 12), Block.box(11, 1, 4, 12, 11, 12), Block.box(5, 1, 4, 11, 11, 5),
			Block.box(5, 1, 11, 11, 11, 12), Block.box(3, 8, 7, 4, 9, 9))
			.reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

	private static final VoxelShape SHAPE_W = Stream
			.of(Block.box(12.000000000000002, 3, 7.000000000000002, 13.000000000000002, 4, 9.000000000000002),
					Block.box(9.000000000000002, 7, 3.0000000000000018, 10.000000000000002, 8, 4.000000000000002),
					Block.box(10.000000000000002, 6, 3.0000000000000018, 12.000000000000002, 7, 4.000000000000002),
					Block.box(10.000000000000002, 6, 12.000000000000002, 12.000000000000002, 7, 13.000000000000002),
					Block.box(9.000000000000002, 7, 12.000000000000002, 10.000000000000002, 8, 13.000000000000002),
					Block.box(12.000000000000002, 5, 3.0000000000000018, 13.000000000000002, 6, 5.000000000000002),
					Block.box(12.000000000000002, 5, 11.000000000000002, 13.000000000000002, 6, 13.000000000000002),
					Block.box(12.000000000000002, 4, 9.000000000000002, 13.000000000000002, 5, 11.000000000000002),
					Block.box(12.000000000000002, 4, 5.000000000000002, 13.000000000000002, 5, 7.000000000000002),
					Block.box(7.000000000000002, 8, 12.000000000000002, 9.000000000000002, 9, 13.000000000000002),
					Block.box(4.000000000000002, 0, 4.000000000000002, 12.000000000000002, 1, 12.000000000000002),
					Block.box(4.000000000000002, 1, 4.000000000000002, 12.000000000000002, 11, 5.000000000000002),
					Block.box(4.000000000000002, 1, 11.000000000000002, 12.000000000000002, 11, 12.000000000000002),
					Block.box(11.000000000000002, 1, 5.000000000000002, 12.000000000000002, 11, 11.000000000000002),
					Block.box(4.000000000000002, 1, 5.000000000000002, 5.000000000000002, 11, 11.000000000000002),
					Block.box(7.000000000000002, 8, 3.0000000000000018, 9.000000000000002, 9, 4.000000000000002))
			.reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

	private static final VoxelShape SHAPE_E = Stream
			.of(Block.box(2.9999999999999982, 3, 7.000000000000002, 3.9999999999999982, 4, 9.000000000000002),
					Block.box(5.999999999999998, 7, 12.000000000000002, 6.999999999999998, 8, 13.000000000000002),
					Block.box(3.9999999999999982, 6, 12.000000000000002, 5.999999999999998, 7, 13.000000000000002),
					Block.box(3.9999999999999982, 6, 3.0000000000000018, 5.999999999999998, 7, 4.000000000000002),
					Block.box(5.999999999999998, 7, 3.0000000000000018, 6.999999999999998, 8, 4.000000000000002),
					Block.box(2.9999999999999982, 5, 11.000000000000002, 3.9999999999999982, 6, 13.000000000000002),
					Block.box(2.9999999999999982, 5, 3.0000000000000018, 3.9999999999999982, 6, 5.000000000000002),
					Block.box(2.9999999999999982, 4, 5.000000000000002, 3.9999999999999982, 5, 7.000000000000002),
					Block.box(2.9999999999999982, 4, 9.000000000000002, 3.9999999999999982, 5, 11.000000000000002),
					Block.box(6.999999999999998, 8, 3.0000000000000018, 8.999999999999998, 9, 4.000000000000002),
					Block.box(3.9999999999999982, 0, 4.000000000000002, 11.999999999999998, 1, 12.000000000000002),
					Block.box(3.9999999999999982, 1, 11.000000000000002, 11.999999999999998, 11, 12.000000000000002),
					Block.box(3.9999999999999982, 1, 4.000000000000002, 11.999999999999998, 11, 5.000000000000002),
					Block.box(3.9999999999999982, 1, 5.000000000000002, 4.999999999999998, 11, 11.000000000000002),
					Block.box(10.999999999999998, 1, 5.000000000000002, 11.999999999999998, 11, 11.000000000000002),
					Block.box(6.999999999999998, 8, 12.000000000000002, 8.999999999999998, 9, 13.000000000000002))
			.reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
	
	public BucketBlock(Properties pProperties) {
		super(pProperties);
		this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH).setValue(WATER, false).setValue(DIRTY, false));
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
	public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand,
			BlockHitResult pHit) {
		ItemStack itemStack = pPlayer.getItemInHand(pHand);

		if (pState.getValue(WATER) == false && pState.getValue(DIRTY) == false) {
			if (itemStack.getItem() == Items.WATER_BUCKET) {
				pLevel.setBlock(pPos, pState.setValue(WATER, true), UPDATE_ALL);
				pPlayer.setItemInHand(pHand, Items.BUCKET.getDefaultInstance());
				return InteractionResult.SUCCESS;
			} else if (itemStack.getItem() == ItemInit.DIRTY_WATER_BUCKET.get()) {
				pLevel.setBlock(pPos, pState.setValue(DIRTY, true), UPDATE_ALL);
				pPlayer.setItemInHand(pHand, Items.BUCKET.getDefaultInstance());
				return InteractionResult.SUCCESS;
			}
		}

		if (itemStack.hasTag()) {
			CompoundTag contentTag = itemStack.getTag().copy();
			if (pState.getValue(WATER) == true && pState.getValue(DIRTY) == false) {
				if (itemStack.getItem() instanceof HogPanMatItem && contentTag.getInt("miningutils_content") >= 100) {
					itemStack.getTag().putInt("miningutils_content", 0);
					pLevel.setBlock(pPos, pState.setValue(WATER, false).setValue(DIRTY, true), UPDATE_ALL);
					return InteractionResult.SUCCESS;
				}
			}
		}

		if (pState.getValue(WATER) == true && pState.getValue(DIRTY) == false) {
			if (itemStack.getItem() == Items.BUCKET) {
				pLevel.setBlock(pPos, pState.setValue(WATER, false), UPDATE_ALL);
				itemStack.shrink(1);
				pPlayer.addItem(Items.WATER_BUCKET.getDefaultInstance());
				return InteractionResult.SUCCESS;
			}
		}

		if (pState.getValue(DIRTY) == true && pState.getValue(WATER) == false) {
			if (itemStack.getItem() == Items.BUCKET) {
				itemStack.shrink(1);
				pPlayer.addItem(ItemInit.DIRTY_WATER_BUCKET.get().getDefaultInstance());
				pLevel.setBlock(pPos, pState.setValue(WATER, false).setValue(DIRTY, false), UPDATE_ALL);
				return InteractionResult.SUCCESS;
			}
		}
		return InteractionResult.FAIL;
	}

	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> pBuilder) {
		pBuilder.add(WATER, DIRTY);
		super.createBlockStateDefinition(pBuilder);
	}
}
