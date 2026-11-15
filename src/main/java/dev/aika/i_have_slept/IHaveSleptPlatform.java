package dev.aika.i_have_slept;

import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;

public class IHaveSleptPlatform {
    public static GameRule<Boolean> registerBoolean(String name, GameRuleCategory category, boolean defaultValue) {
        return dev.aika.i_have_slept.
                /*? if fabric {*/fabric/*?} else {*//*neoforge*//*?}*/
                .IHaveSleptPlatformImpl.registerBoolean(name, category, defaultValue);
    }
}