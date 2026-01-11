package dev.aika.i_have_slept.mixin;

import dev.aika.i_have_slept.IHaveSlept;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.level.gamerules.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin {
    public abstract @Shadow List<ServerPlayer> getPlayers(Predicate<? super ServerPlayer> predicate);

    public abstract @Shadow GameRules getGameRules();

    @Inject(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerLevel;wakeUpAllPlayers()V",
                    shift = At.Shift.AFTER
            )
    )
    private void onTick(BooleanSupplier hasTimeLeft, CallbackInfo ci) {
        if (!this.getGameRules().get(IHaveSlept.BETTER_SERVER_SLEEP)) {
            return;
        }
        this.getPlayers(player -> {
            IHaveSlept.LOGGER.info("Resetting time since rest for player {}", player.getName().getString());
            player.resetStat(Stats.CUSTOM.get(Stats.TIME_SINCE_REST));
            return true;
        });
    }
}
