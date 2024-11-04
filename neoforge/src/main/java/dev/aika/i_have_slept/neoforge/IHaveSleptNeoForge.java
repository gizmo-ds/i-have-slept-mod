package dev.aika.i_have_slept.neoforge;

import dev.aika.i_have_slept.IHaveSlept;
import net.minecraft.world.GameRules;
import net.neoforged.fml.common.Mod;

@Mod(IHaveSlept.MOD_ID)
public final class IHaveSleptNeoForge {
    public IHaveSleptNeoForge() {
        IHaveSlept.BETTER_SERVER_SLEEP = GameRules.register("doBetterServerSleep", GameRules.Category.SPAWNING, GameRules.BooleanRule.create(true));
        IHaveSlept.init();
    }
}
