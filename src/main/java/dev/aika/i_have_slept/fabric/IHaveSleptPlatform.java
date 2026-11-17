package dev.aika.i_have_slept.fabric;

//? if fabric && >=1.21.11 {
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.serialization.Codec;
import dev.aika.i_have_slept.IHaveSlept;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;
import net.minecraft.world.level.gamerules.GameRuleType;
import net.minecraft.world.level.gamerules.GameRuleTypeVisitor;

public class IHaveSleptPlatform {
    public static GameRule<Boolean> registerBoolean(String name, GameRuleCategory category, boolean defaultValue) {
        return Registry.register(
                BuiltInRegistries.GAME_RULE,
                Identifier.fromNamespaceAndPath(IHaveSlept.MOD_ID, name),
                new GameRule<>(
                        category, GameRuleType.BOOL, BoolArgumentType.bool(), GameRuleTypeVisitor::visitBoolean,
                        Codec.BOOL, bool -> bool ? 1 : 0, defaultValue, FeatureFlagSet.of()
                )
        );
    }
}
//?}