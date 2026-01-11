package dev.aika.i_have_slept.mixin;

import dev.aika.i_have_slept.IHaveSlept;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;
import java.util.function.Supplier;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin extends Level {
    protected ServerLevelMixin(WritableLevelData levelData, ResourceKey<Level> dimension, Holder<DimensionType> dimensionTypeRegistration, Supplier<ProfilerFiller> profiler, boolean isClientSide, boolean isDebug, long biomeZoomSeed, int maxChainedNeighborUpdates) {
        super(levelData, dimension, dimensionTypeRegistration, profiler, isClientSide, isDebug, biomeZoomSeed, maxChainedNeighborUpdates);
    }

    @Shadow
    public abstract List<ServerPlayer> getPlayers(Predicate<? super ServerPlayer> predicate);

    @Inject(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerLevel;wakeUpAllPlayers()V",
                    shift = At.Shift.AFTER
            )
    )
    private void onTick(BooleanSupplier hasTimeLeft, CallbackInfo ci) {
        if (!this.getGameRules().getBoolean(IHaveSlept.BETTER_SERVER_SLEEP)) {
            return;
        }
        this.getPlayers(player -> {
            IHaveSlept.LOGGER.info("Resetting time since rest for player {}", player.getName().getString());
            player.resetStat(Stats.CUSTOM.get(Stats.TIME_SINCE_REST));
            return true;
        });
    }
}
