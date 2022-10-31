package com.shiying.savebox;

import com.shiying.savebox.blocks.SaveBoxBlock;
import com.shiying.savebox.entity.SaveBoxBlockEntity;
import com.shiying.savebox.screen.SaveBoxScreenHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.slf4j.LoggerFactory;

public class Savebox implements ModInitializer {
    public static final String MOD_ID = "save_box";
    public static final Identifier SAVE_BOX = new Identifier(MOD_ID, "save_box");
    public static final Block SAVE_BOX_BLOCK = Registry.register(Registry.BLOCK, SAVE_BOX, new SaveBoxBlock(FabricBlockSettings.copy(Blocks.CHEST)));
    public static final BlockEntityType<SaveBoxBlockEntity> SAVE_BOX_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, SAVE_BOX, FabricBlockEntityTypeBuilder.create(SaveBoxBlockEntity::new, SAVE_BOX_BLOCK).build());

    public static final ScreenHandlerType<SaveBoxScreenHandler> SAVE_BOX_SCREEN_HANDLER_TYPE = ScreenHandlerRegistry.registerExtended(SAVE_BOX, SaveBoxScreenHandler::new);

    @Override
    public void onInitialize() {
        LoggerFactory.getLogger(Savebox.class).info("AAA");
    }
}
