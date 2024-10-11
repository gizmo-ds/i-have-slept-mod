package dev.aika.i_have_slept.mixin;

import dev.aika.i_have_slept.IHaveSlept;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin {
    @Shadow
    public abstract List<ServerPlayerEntity> getPlayers();

    @Inject(method = "tick", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/server/world/ServerWorld;wakeSleepingPlayers()V",
            shift = At.Shift.AFTER))
    private void onWakeSleepingPlayersTick(CallbackInfo ci) {
        this.getPlayers().forEach((player) -> {
            IHaveSlept.LOGGER.info("Resetting time since rest for player {}", player.getName().getString());
            player.resetStat(Stats.CUSTOM.getOrCreateStat(Stats.TIME_SINCE_REST));
        });
    }
}
