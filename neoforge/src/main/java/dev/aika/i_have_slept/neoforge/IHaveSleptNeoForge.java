package dev.aika.i_have_slept.neoforge;

import dev.aika.i_have_slept.IHaveSlept;
import net.minecraft.world.level.GameRules;
import net.neoforged.fml.common.Mod;

@Mod(IHaveSlept.MOD_ID)
public final class IHaveSleptNeoForge {
    public IHaveSleptNeoForge() {
        IHaveSlept.BETTER_SERVER_SLEEP = GameRules.register(
                "doBetterServerSleep",
                GameRules.Category.SPAWNING,
                GameRules.BooleanValue.create(true)
        );
        IHaveSlept.init();
    }
}
