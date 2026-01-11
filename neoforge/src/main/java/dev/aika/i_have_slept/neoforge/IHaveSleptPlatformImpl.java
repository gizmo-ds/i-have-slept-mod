package dev.aika.i_have_slept.neoforge;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.serialization.Codec;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;
import net.minecraft.world.level.gamerules.GameRuleType;
import net.minecraft.world.level.gamerules.GameRuleTypeVisitor;

@SuppressWarnings("unused")
public class IHaveSleptPlatformImpl {
    public static GameRule<Boolean> registerBoolean(String name, GameRuleCategory category, boolean defaultValue) {
        GameRule<Boolean> rule = new GameRule<>(
                category, GameRuleType.BOOL, BoolArgumentType.bool(),
                GameRuleTypeVisitor::visitBoolean, Codec.BOOL, bool -> bool ? 1 : 0, defaultValue,
                FeatureFlagSet.of()
        );
        GameRuleRegistry.GAME_RULE_REGISTER.register(name, () -> rule);
        return rule;
    }
}
