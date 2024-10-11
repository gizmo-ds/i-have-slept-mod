package dev.aika.i_have_slept.forge;

import dev.aika.i_have_slept.IHaveSlept;
import net.minecraftforge.fml.common.Mod;

@Mod(IHaveSlept.MOD_ID)
public final class IHaveSleptForge {
    public IHaveSleptForge() {
        // Run our common setup.
        IHaveSlept.init();
    }
}
