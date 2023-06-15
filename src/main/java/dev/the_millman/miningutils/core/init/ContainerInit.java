package dev.the_millman.miningutils.core.init;

import dev.the_millman.miningutils.MiningUtils;
import dev.the_millman.miningutils.common.containers.BlockBreakerContainer;
import dev.the_millman.miningutils.common.containers.BlockPlacerContainer;
import dev.the_millman.miningutils.common.containers.HogPanContainer;
import dev.the_millman.miningutils.common.containers.VerticalMinerContainer;
import dev.the_millman.miningutils.common.containers.WaveTableContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ContainerInit {
	public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, MiningUtils.MODID);
	
	public static final RegistryObject<MenuType<VerticalMinerContainer>> VERTICAL_MINER_CONTAINER = CONTAINERS.register("vertical_miner_container", () -> IForgeMenuType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        Level world = inv.player.getCommandSenderWorld();
        return new VerticalMinerContainer(windowId, world, pos, inv, inv.player);
    }));
	
	public static final RegistryObject<MenuType<BlockBreakerContainer>> BLOCK_BREAKER_CONTAINER = CONTAINERS.register("block_breaker_container", () -> IForgeMenuType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        Level world = inv.player.getCommandSenderWorld();
        return new BlockBreakerContainer(windowId, world, pos, inv, inv.player);
    }));
	
	public static final RegistryObject<MenuType<BlockPlacerContainer>> BLOCK_PLACER_CONTAINER = CONTAINERS.register("block_placer_container", () -> IForgeMenuType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        Level world = inv.player.getCommandSenderWorld();
        return new BlockPlacerContainer(windowId, world, pos, inv, inv.player);
    }));
	
	public static final RegistryObject<MenuType<HogPanContainer>> HOG_PAN_CONTAINER = CONTAINERS.register("hog_pan_container", () -> IForgeMenuType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        Level world = inv.player.getCommandSenderWorld();
        return new HogPanContainer(windowId, world, pos, inv, inv.player);
    }));
	
	public static final RegistryObject<MenuType<WaveTableContainer>> WAVE_TABLE_CONTAINER = CONTAINERS.register("wave_table_container", () -> IForgeMenuType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        Level world = inv.player.getCommandSenderWorld();
        return new WaveTableContainer(windowId, world, pos, inv, inv.player);
    }));
}
