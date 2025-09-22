package dev.aika.i_have_slept.fabric;

import dev.aika.i_have_slept.IHaveSlept;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.level.GameRules;

public final class IHaveSleptFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        IHaveSlept.BETTER_SERVER_SLEEP = GameRuleRegistry.register(
                "doBetterServerSleep",
                GameRules.Category.SPAWNING,
                GameRuleFactory.createBooleanRule(true)
        );
        IHaveSlept.init();
    }
}
