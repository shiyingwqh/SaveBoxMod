package com.shiying.savebox.screen;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.widget.EditBoxWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.function.BiConsumer;

/**
 * @author Wuqihang
 */
public class InputScreen extends ConfirmScreen {
    private EditBoxWidget password;

    public InputScreen(BooleanConsumer callback, Text title, Text message, Text yesText, Text noText) {
        super(callback, title, message, yesText, noText);
    }

    @Override
    protected void init() {
        super.init();
        password = new EditBoxWidget(textRenderer, this.width / 2, this.height / 2, 100, 20, Text.of(""), Text.of(""));
        this.addDrawable(password);
        this.addSelectableChild(password);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);
    }

    public String getPassword() {
        return password.getText();
    }
}
