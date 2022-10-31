package com.shiying.savebox.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.shiying.savebox.SaveBoxLocker;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class SaveBoxScreen extends HandledScreen<ScreenHandler> {
    private static final Identifier TEXTURE = new Identifier("save_box", "textures/gui/save_box_1");
    private static final Identifier TEXTURE2 = new Identifier("save_box", "textures/gui/save_box_2");

    private TextFieldWidget pwdField;
    private ButtonWidget button;

    private String password = "";

    private boolean locked = true;

    private final SaveBoxLocker locker;

    public SaveBoxScreen(ScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        locker = ((SaveBoxScreenHandler) handler).getSaveBoxLocker();
    }

    @Override
    protected void init() {
        super.init();
        assert this.client != null;
        this.client.keyboard.setRepeatEvents(true);
        int i = (this.width - this.backgroundWidth) / 2;
        int j = (this.height - this.backgroundHeight) / 2;
        this.button = new ButtonWidget(i + 62, j + 24 + 15, 50, 20, Text.of("Check"), button -> {
            if (!locker.isLocked()) {
                this.client.setScreen(new ConfirmScreen(t -> {
                    if (t) {
                        locker.setPassword(password);
                        locked = false;
                    }
                    SaveBoxScreen.this.client.setScreen(SaveBoxScreen.this);
                }, Text.translatable("confirm"), Text.of("Confirm to Set Password: " + SaveBoxScreen.this.password)));
            } else {
                locked = !locker.checkPassword(password);
            }
            if (!locked) {
                this.remove(this.pwdField);
                this.remove(this.button);
            }
        });
        this.addSelectableChild(this.button);
        this.pwdField = new TextFieldWidget(this.textRenderer, i + 62, j + 24, 50, 12, Text.translatable("container.repair"));
        this.pwdField.setFocusUnlocked(false);
        this.pwdField.setEditableColor(-1);
        this.pwdField.setUneditableColor(-1);
        this.pwdField.setDrawsBackground(true);
        this.pwdField.setMaxLength(50);
        this.pwdField.setChangedListener(this::onChange);
        this.pwdField.setText("");
        this.addSelectableChild(this.pwdField);
        this.setInitialFocus(this.pwdField);
        this.pwdField.setEditable(true);
    }

    private void onChange(String s) {
        password = s;
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if (isUnLocked()) {
            super.render(matrices, mouseX, mouseY, delta);
        } else {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShaderTexture(0, TEXTURE2);
            int x = (width - backgroundWidth) / 2;
            int y = (height - backgroundHeight) / 2;
            drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);
            this.textRenderer.draw(matrices, this.title, x + 2, y + 2, 0x404040);
            this.pwdField.render(matrices, mouseX, mouseY, delta);
            this.button.render(matrices, mouseX, mouseY, delta);
        }
    }

    public boolean isUnLocked() {
        return !locked;
    }


}
