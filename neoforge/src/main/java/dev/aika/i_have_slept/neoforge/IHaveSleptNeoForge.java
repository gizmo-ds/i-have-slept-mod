package dev.aika.i_have_slept.neoforge;

import dev.aika.i_have_slept.IHaveSlept;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(IHaveSlept.MOD_ID)
public final class IHaveSleptNeoForge {
    public IHaveSleptNeoForge(IEventBus modBus) {
        IHaveSlept.init();
        GameRuleRegistry.GAME_RULE_REGISTER.register(modBus);
    }
}
