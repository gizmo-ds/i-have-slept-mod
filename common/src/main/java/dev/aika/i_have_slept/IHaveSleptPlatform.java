package dev.aika.i_have_slept;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;

public class IHaveSleptPlatform {
    @ExpectPlatform
    public static GameRule<Boolean> registerBoolean(String name, GameRuleCategory category, boolean defaultValue) {
        throw new AssertionError();
    }
}
