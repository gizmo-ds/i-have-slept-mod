package dev.aika.i_have_slept;

import net.minecraft.world.GameRules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class IHaveSlept {
    public static final String MOD_ID = "i_have_slept";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static GameRules.Key<GameRules.BooleanRule> BETTER_SERVER_SLEEP;

    public static void init() {
    }
}
