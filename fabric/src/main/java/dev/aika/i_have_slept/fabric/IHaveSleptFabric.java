package dev.aika.i_have_slept.fabric;

import dev.aika.i_have_slept.IHaveSlept;
import net.fabricmc.api.ModInitializer;

public final class IHaveSleptFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        IHaveSlept.init();
    }
}
