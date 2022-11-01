package com.shiying.savebox.client;

import com.shiying.savebox.Savebox;
import com.shiying.savebox.screen.SaveBoxScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.item.BookItem;

@Environment(EnvType.CLIENT)
public class SaveboxClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {

        ScreenRegistry.register(Savebox.SAVE_BOX_SCREEN_HANDLER_TYPE, SaveBoxScreen::new);
    }
}
