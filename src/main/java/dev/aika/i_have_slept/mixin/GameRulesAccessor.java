package dev.aika.i_have_slept.mixin;

//? if <1.21.11 {
/*import net.minecraft.world.level.gamerules.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(GameRules.class)
public interface GameRulesAccessor {
    @Invoker("register")
    static <T extends GameRules.Value<T>> GameRules.Key<T> callRegister(String name, GameRules.Category category, GameRules.Type<T> type) {
        throw new AssertionError("Oops!");
    }
}
*///?} else {
@org.spongepowered.asm.mixin.Mixin(net.minecraft.world.level.Level.class)
public abstract class GameRulesAccessor {
}
//?}