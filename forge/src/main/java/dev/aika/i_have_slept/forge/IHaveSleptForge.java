package dev.aika.i_have_slept.forge;

import dev.aika.i_have_slept.IHaveSlept;
import net.minecraft.world.GameRules;
import net.minecraftforge.fml.common.Mod;

@Mod(IHaveSlept.MOD_ID)
public final class IHaveSleptForge {
    public IHaveSleptForge() {
        IHaveSlept.BETTER_SERVER_SLEEP = GameRules.register("doBetterServerSleep", GameRules.Category.SPAWNING, GameRules.BooleanRule.create(true));
        IHaveSlept.init();
    }
}
