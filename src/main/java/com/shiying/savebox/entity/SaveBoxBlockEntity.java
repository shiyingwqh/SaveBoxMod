package com.shiying.savebox.entity;

import com.shiying.savebox.ImplementedInventory;
import com.shiying.savebox.Savebox;
import com.shiying.savebox.screen.SaveBoxScreenHandler;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import static com.shiying.savebox.Savebox.SAVE_BOX_PACKET_ID;
import static com.shiying.savebox.blocks.SaveBoxBlock.LOCKED;

public class SaveBoxBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory, ImplementedInventory, SidedInventory {
    private String playerId = "";
    private boolean locked = false;
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(27, ItemStack.EMPTY);

    static {
        ServerPlayNetworking.registerGlobalReceiver(SAVE_BOX_PACKET_ID, (server, player, handler, buf, responseSender) -> {
            BlockPos target = buf.readBlockPos();
            boolean locked = buf.readBoolean();
            server.execute(() -> {
                BlockEntity blockEntity = player.getWorld().getBlockEntity(target);
                if (!(blockEntity instanceof SaveBoxBlockEntity)) {
                    return;
                }
                if (locked) {
                    ((SaveBoxBlockEntity) blockEntity).unlock(player);
                } else {
                    ((SaveBoxBlockEntity) blockEntity).lock(player);
                }
            });
        });
    }

    public SaveBoxBlockEntity(BlockPos pos, BlockState state) {
        super(Savebox.SAVE_BOX_ENTITY, pos, state);
    }


    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public Text getDisplayName() {
        return MutableText.of(new TranslatableTextContent(getCachedState().getBlock().getTranslationKey()));
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new SaveBoxScreenHandler(syncId, inv, this);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        nbt.putString("player_id", playerId);
        nbt.putBoolean("locked", locked);
        Inventories.writeNbt(nbt, inventory);
        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, inventory);
        nbt.getBoolean("locked");
        nbt.getString("player_id");
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeBoolean(locked);
    }

    @Override
    public int[] getAvailableSlots(Direction side) {
        return new int[0];
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        return false;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return false;
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }


    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    public boolean playerCanUse(PlayerEntity player) {
        return Objects.equals(player.getName().getString(), this.playerId);
    }

    public void lock(PlayerEntity player) {
        assert world != null;
        if (world.isClient) return;
        world.setBlockState(pos, world.getBlockState(pos).with(LOCKED, true));
        this.playerId = player.getName().getString();
        locked = true;
        ((ServerWorld) world).getChunkManager().markForUpdate(pos);
    }

    private void unlock(ServerPlayerEntity player) {
        assert world != null;
        if (world.isClient) return;
        world.setBlockState(pos, world.getBlockState(pos).with(LOCKED, false));
        locked = false;
        ((ServerWorld) world).getChunkManager().markForUpdate(pos);
    }
}
