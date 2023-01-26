package com.the_millman.miningutils.core.util;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public class MiningConfig {
	public static ForgeConfigSpec CLIENT_CONFIG;
	public static ForgeConfigSpec SERVER_CONFIG;
	
	public static ForgeConfigSpec.BooleanValue NEED_ENERGY;
	
	public static ForgeConfigSpec.IntValue VERTICAL_MINER_USEPERTICK;
    public static ForgeConfigSpec.IntValue VERTICAL_MINER_TICK;
    public static ForgeConfigSpec.IntValue VERTICAL_MINER_CAPACITY;
    
    public static ForgeConfigSpec.IntValue BLOCK_BREAKER_USEPERTICK;
    public static ForgeConfigSpec.IntValue BLOCK_BREAKER_CAPACITY;
    
    public static ForgeConfigSpec.IntValue BLOCK_PLACER_USEPERTICK;
    public static ForgeConfigSpec.IntValue BLOCK_PLACER_CAPACITY;
    
    public static ForgeConfigSpec.IntValue HOG_PAN_WATER_CAPACITY;
    public static ForgeConfigSpec.IntValue HOG_PAN_WATER_CONSUME;
    public static ForgeConfigSpec.IntValue HOG_PAN_TICK;
    
    public static ForgeConfigSpec.IntValue WAVE_TABLE_USEPERTICK;
    public static ForgeConfigSpec.IntValue WAVE_TABLE_CAPACITY;
    public static ForgeConfigSpec.IntValue WAVE_TABLE_FLUID_CAPACITY;
    public static ForgeConfigSpec.IntValue WAVE_TABLE_FLUID_USEPERTICK;
    public static ForgeConfigSpec.IntValue WAVE_TABLE_TICK;
    
    public static void init() {
    	initServer();
    	initClient();
    	
    	ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, SERVER_CONFIG);
    	ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CLIENT_CONFIG);
    }
    
    private static void initServer() {
    	ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
    	
    	builder.comment("Vertical Miner settings").push("vertical_miner");
    	VERTICAL_MINER_USEPERTICK = builder
                .comment("How much FE the vertical miner will consume per tick")
                .defineInRange("vertical_miner_usePerTick", 40, 0, Integer.MAX_VALUE);
        
    	VERTICAL_MINER_TICK = builder
                .comment("How many ticks must pass before the vertical miner works")
                .defineInRange("vertical_miner_tick", 40, 20, Integer.MAX_VALUE);
        
    	VERTICAL_MINER_CAPACITY = builder
                .comment("How much FE the vertical miner can store")
                .defineInRange("vertical_miner_capacity", 10000, 0, Integer.MAX_VALUE);
        builder.pop();
        
        builder.comment("Block Breaker settings").push("block_breaker");
        BLOCK_BREAKER_USEPERTICK = builder
                .comment("How much FE the block breaker will consume per tick")
                .defineInRange("block_breaker_usePerTick", 40, 0, Integer.MAX_VALUE);
        
        BLOCK_BREAKER_CAPACITY = builder
                .comment("How much FE the block breaker can store")
                .defineInRange("block_breaker_capacity", 10000, 0, Integer.MAX_VALUE);
        builder.pop();
        
        builder.comment("Block Placer settings").push("block_placer");
        BLOCK_PLACER_USEPERTICK = builder
                .comment("How much FE the block placer will consume per tick")
                .defineInRange("block_placer_usePerTick", 40, 0, Integer.MAX_VALUE);
        
        BLOCK_PLACER_CAPACITY = builder
                .comment("How much FE the block placer can store")
                .defineInRange("block_placer_capacity", 10000, 0, Integer.MAX_VALUE);
        builder.pop();
        
        builder.comment("Hog Pan settings").push("hog_pan");
        HOG_PAN_WATER_CAPACITY = builder
        		.comment("How much water can the hog pan store")
        		.defineInRange("hog_pan_water_capacity", 20000, 0, Integer.MAX_VALUE);
        
        HOG_PAN_WATER_CONSUME = builder
        		.comment("How much water the hog pan consume for block")
        		.defineInRange("hog_pan_water_consume", 1000, 0, Integer.MAX_VALUE);
        
        HOG_PAN_TICK = builder
        		.comment("How many ticks must pass before the vertical miner works")
        		.defineInRange("hog_pan_tick", 60, 10, Integer.MAX_VALUE);
        builder.pop();
        
        builder.comment("Wave Table settings").push("wave_table");
        WAVE_TABLE_CAPACITY = builder
        		.comment("How much FE can the wave table store")
        		.defineInRange("wave_table_capacity", 10000, 0, Integer.MAX_VALUE);
        
        WAVE_TABLE_USEPERTICK = builder
        		.comment("How much FE the wave table will consume per tick")
        		.defineInRange("wave_table_usepertick", 20, 0, Integer.MAX_VALUE);
        
        WAVE_TABLE_FLUID_CAPACITY = builder
        		.comment("How much mB the internal farmer can store")
        		.defineInRange("wave_table_fluid_capacity", 20000, 0, Integer.MAX_VALUE);
        
        WAVE_TABLE_FLUID_USEPERTICK = builder
        		.comment("How much water the wave table will consume per tick")
        		.defineInRange("wave_table_fluid_usepertick", 500, 0, Integer.MAX_VALUE);
        
        WAVE_TABLE_TICK = builder
        		.comment("How many ticks must pass before the wave table works")
        		.defineInRange("wave_table_tick", 200, 10, 1000);
        builder.pop();
        
    	SERVER_CONFIG = builder.build();
    }
    
    private static void initClient() {
    	ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
    	
    	CLIENT_CONFIG = builder.build();
    }
}
