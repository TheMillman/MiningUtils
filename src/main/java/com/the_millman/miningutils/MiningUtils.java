package com.the_millman.miningutils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.the_millman.miningutils.core.init.BlockEntityInit;
import com.the_millman.miningutils.core.init.BlockInit;
import com.the_millman.miningutils.core.init.ContainerInit;
import com.the_millman.miningutils.core.init.ItemInit;
import com.the_millman.miningutils.core.networking.ModMessages;
import com.the_millman.miningutils.core.util.MiningConfig;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(MiningUtils.MODID)
public class MiningUtils
{
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MODID = "miningutils";
    public static final boolean DEBUG = false;
    
    public MiningUtils() {
    	IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        
    	ItemInit.ITEMS.register(bus);
    	BlockInit.BLOCKS.register(bus);
    	BlockEntityInit.BLOCK_ENTITIES.register(bus);
    	ContainerInit.CONTAINERS.register(bus);
    	
    	MiningConfig.init();
    	
    	bus.addListener(this::setup);
    	bus.addListener(this::commonSetup);
       
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        
    }
    
    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            ModMessages.register();
        });
    }
}
