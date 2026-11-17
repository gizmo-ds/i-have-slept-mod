package dev.aika.i_have_slept.neoforge;

//? if neoforge && >=1.21.11 {
/*import com.mojang.serialization.Codec;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;
import net.minecraft.world.level.gamerules.GameRuleType;
import net.minecraft.world.level.gamerules.GameRuleTypeVisitor;

public class IHaveSleptPlatform {
    public static GameRule<Boolean> registerBoolean(String name, GameRuleCategory category, boolean defaultValue) {
        GameRule<Boolean> rule = new GameRule<>(
                category, GameRuleType.BOOL, //? if >26.3 {
                net.minecraft.commands.arguments.BooleanArgument.INSTANCE, //?} else {
                /^com.mojang.brigadier.arguments.BoolArgumentType.bool(), ^///?}
                GameRuleTypeVisitor::visitBoolean, Codec.BOOL, bool -> bool ? 1 : 0, defaultValue,
                FeatureFlagSet.of()
        );
        GameRuleRegistry.GAME_RULE_REGISTER.register(name, () -> rule);
        return rule;
    }
}
*///?}