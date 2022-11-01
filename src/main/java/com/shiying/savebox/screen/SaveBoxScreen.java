package com.shiying.savebox.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.LockButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import static com.shiying.savebox.Savebox.MOD_ID;
import static com.shiying.savebox.Savebox.SAVE_BOX_PACKET_ID;

public class SaveBoxScreen extends HandledScreen<SaveBoxScreenHandler> {
    private static final Identifier TEXTURE = new Identifier(MOD_ID, "textures/gui/container/save_box_1.png");
    private LockButtonWidget btn;

    public SaveBoxScreen(SaveBoxScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        btn = new LockButtonWidget(10, 10, this::onClick);
        addSelectableChild(btn);
        btn.setLocked(handler.isLocked());
    }

    private void onClick(ButtonWidget buttonWidget) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBlockPos(handler.getPos());
        buf.writeBoolean(btn.isLocked());
        boolean locked = btn.isLocked();
        btn.setLocked(!locked);
        handler.setLocked(!locked);
        ClientPlayNetworking.send(SAVE_BOX_PACKET_ID, buf);
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);
        btn.render(matrices, mouseX, mouseY, delta);
    }

}
