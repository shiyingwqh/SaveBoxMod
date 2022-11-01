package com.shiying.savebox;

import com.mojang.logging.LogUtils;
import com.shiying.savebox.blocks.SaveBoxBlock;
import com.shiying.savebox.entity.SaveBoxBlockEntity;
import com.shiying.savebox.screen.SaveBoxScreenHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.slf4j.LoggerFactory;

public class Savebox implements ModInitializer {
    public static final String MOD_ID = "save_box";
    public static final Identifier SAVE_BOX = new Identifier(MOD_ID, "save_box");
    public static final Identifier SAVE_BOX_PACKET_ID = new Identifier(MOD_ID,"save_box_packet");
    public static final Block SAVE_BOX_BLOCK = Registry.register(Registry.BLOCK, SAVE_BOX, new SaveBoxBlock(FabricBlockSettings.copy(Blocks.CHEST)));

    public static final BlockItem SAVE_BOX_BLOCK_ITEM = Registry.register(Registry.ITEM, SAVE_BOX ,new BlockItem(SAVE_BOX_BLOCK, new Item.Settings().group(ItemGroup.MISC)));
    public static final BlockEntityType<SaveBoxBlockEntity> SAVE_BOX_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, SAVE_BOX, FabricBlockEntityTypeBuilder.create(SaveBoxBlockEntity::new, SAVE_BOX_BLOCK).build());

    public static final ScreenHandlerType<SaveBoxScreenHandler> SAVE_BOX_SCREEN_HANDLER_TYPE = ScreenHandlerRegistry.registerExtended(SAVE_BOX, SaveBoxScreenHandler::new);



    @Override
    public void onInitialize() {

    }
}
