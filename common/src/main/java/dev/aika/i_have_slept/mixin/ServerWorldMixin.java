package dev.aika.i_have_slept.mixin;

import dev.aika.i_have_slept.IHaveSlept;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.Supplier;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin extends World implements StructureWorldAccess {
    protected ServerWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, RegistryEntry<DimensionType> dimension, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long seed, int maxChainedNeighborUpdates) {
        super(properties, registryRef, dimension, profiler, isClient, debugWorld, seed, maxChainedNeighborUpdates);
    }

    @Shadow
    public abstract List<ServerPlayerEntity> getPlayers();

    @Inject(method = "tick", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/server/world/ServerWorld;wakeSleepingPlayers()V",
            shift = At.Shift.AFTER))
    private void onWakeSleepingPlayersTick(CallbackInfo ci) {
        if (!this.getGameRules().getBoolean(IHaveSlept.BETTER_SERVER_SLEEP)) {
            return;
        }
        this.getPlayers().forEach((player) -> {
            IHaveSlept.LOGGER.info("Resetting time since rest for player {}", player.getName().getString());
            player.resetStat(Stats.CUSTOM.getOrCreateStat(Stats.TIME_SINCE_REST));
        });
    }
}
