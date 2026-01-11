package dev.aika.i_have_slept;

import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class IHaveSlept {
    public static final String MOD_ID = "i_have_slept";
    public static final String MOD_NAME = "I Have Slept";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static GameRule<Boolean> BETTER_SERVER_SLEEP;

    public static void init() {
        BETTER_SERVER_SLEEP = IHaveSleptPlatform.registerBoolean("better_server_sleep", GameRuleCategory.PLAYER, true);
    }
}
