package dev.aika.i_have_slept.mixin;

import net.minecraft.world.level.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(GameRules.BooleanValue.class)
public interface GameRulesBooleanValueAccessor {
    @Invoker
    static GameRules.Type<GameRules.BooleanValue> invokeCreate(boolean defaultValue) {
        throw new AssertionError("Oops!");
    }
}
