package dev.aika.i_have_slept.neoforge;

import dev.aika.i_have_slept.IHaveSlept;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.gamerules.GameRule;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

public class GameRuleRegistry {
    public static final DeferredRegister<@NotNull GameRule<?>> GAME_RULE_REGISTER =
            DeferredRegister.create(Registries.GAME_RULE, IHaveSlept.MOD_ID);
}
