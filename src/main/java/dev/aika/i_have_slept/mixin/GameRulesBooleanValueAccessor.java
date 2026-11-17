package dev.aika.i_have_slept.mixin;

//? if <1.21.11 {
/*import net.minecraft.world.level.gamerules.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(GameRules.BooleanValue.class)
public interface GameRulesBooleanValueAccessor {
    @Invoker("create")
    static GameRules.Type<GameRules.BooleanValue> invokeCreate(boolean defaultValue) {
        throw new AssertionError("Oops!");
    }
}
*///?} else {
@org.spongepowered.asm.mixin.Mixin(net.minecraft.world.level.Level.class)
public abstract class GameRulesBooleanValueAccessor {
}
//?}