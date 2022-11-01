package com.shiying.savebox.screen;

import com.shiying.savebox.SaveBoxLocker;
import com.shiying.savebox.Savebox;
import com.shiying.savebox.entity.SaveBoxBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.gui.screen.ingame.AnvilScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.*;
import net.minecraft.screen.slot.CraftingResultSlot;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;

public class SaveBoxScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    private boolean locked;
    private BlockPos pos;

    public SaveBoxScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf byteBuf) {
        this(syncId, playerInventory, new SimpleInventory(27));
        pos = byteBuf.readBlockPos();
        locked = byteBuf.readBoolean();
    }

    public SaveBoxScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        super(Savebox.SAVE_BOX_SCREEN_HANDLER_TYPE, syncId);
        checkSize(inventory, 27);
        this.inventory = inventory;
        inventory.onOpen(playerInventory.player);
        int m;
        int l;
        for (m = 0; m < 3; ++m) {
            for (l = 0; l < 9; ++l) {
                this.addSlot(new Slot(inventory, l + m * 9, 8 + l * 18, 18 + m * 18));
            }
        }

        for (m = 0; m < 3; ++m) {
            for (l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + m * 9 + 9, 8 + l * 18, 84 + m * 18));
            }
        }
        // 玩家快捷栏
        for (m = 0; m < 9; ++m) {
            this.addSlot(new Slot(playerInventory, m, 8 + m * 18, 142));
        }
    }


    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (index < this.inventory.size()) {
                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(originalStack, 0, this.inventory.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }

        return newStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public BlockPos getPos() {
        return pos;
    }
}
