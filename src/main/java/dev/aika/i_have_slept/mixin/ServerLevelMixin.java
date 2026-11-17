package dev.aika.i_have_slept.mixin;

import dev.aika.i_have_slept.IHaveSlept;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin {
    @SuppressWarnings("DataFlowIssue")
    @Inject(method = "tick", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/server/level/ServerLevel;wakeUpAllPlayers()V",
            shift = At.Shift.AFTER))
    private void onTick(CallbackInfo ci) {
        if (!((ServerLevel) (Object) this).getGameRules().get(IHaveSlept.BETTER_SERVER_SLEEP)) return;
        for (var player : ((ServerLevel) (Object) this).players()) {
            IHaveSlept.LOGGER.info("Resetting time since rest for player {}", player.getName().getString());
            player.resetStat(Stats.CUSTOM.get(Stats.TIME_SINCE_REST));
        }
    }
}