package dev.aika.i_have_slept;

//? if >=1.21.11 {
import net.minecraft.world.level.gamerules.GameRuleCategory;
import net.minecraft.world.level.gamerules.GameRule; //?} else {
/*import dev.aika.i_have_slept.mixin.GameRulesAccessor;
import dev.aika.i_have_slept.mixin.GameRulesBooleanValueAccessor;
import net.minecraft.world.level.gamerules.GameRules; *///?}
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class IHaveSlept {
    public static final String MOD_ID = "i_have_slept";
    public static final String MOD_NAME = "I Have Slept";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    //? if >=1.21.11 {
    public static GameRule<Boolean> BETTER_SERVER_SLEEP; //?} else {
    /*public static GameRules.Key<GameRules.BooleanValue> BETTER_SERVER_SLEEP; *///?}

    public static void init() {
        //? if >=1.21.11 {
        BETTER_SERVER_SLEEP = dev.aika.i_have_slept./*? if fabric {*/fabric/*?} else {*//*neoforge*//*?}*/
                .IHaveSleptPlatform.registerBoolean("better_server_sleep", GameRuleCategory.PLAYER, true);//?} else {
        /*BETTER_SERVER_SLEEP = GameRulesAccessor.callRegister(
                "doBetterServerSleep",
                GameRules.Category.PLAYER,
                GameRulesBooleanValueAccessor.invokeCreate(true)
        );
        *///?}
    }
}