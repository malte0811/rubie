package malte0811.rubie.mixins;

import malte0811.rubie.RubieEmptyChunkChecker;
import me.jellysquid.mods.sodium.client.render.chunk.RenderSection;
import me.jellysquid.mods.sodium.client.render.chunk.RenderSectionManager;
import me.jellysquid.mods.sodium.client.render.chunk.tasks.ChunkRenderBuildTask;
import me.jellysquid.mods.sodium.client.world.cloned.ClonedChunkSectionCache;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.chunk.LevelChunkSection;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderSectionManager.class)
public class RenderSectionManagerMixin {
    @Shadow
    private int currentFrame;

    @Shadow
    @Final
    private ClonedChunkSectionCache sectionCache;

    @Inject(
            method = "createRebuildTask",
            remap = false,
            cancellable = true,
            at = @At(value = "INVOKE", target = "Lme/jellysquid/mods/sodium/client/render/chunk/tasks/ChunkRenderEmptyBuildTask;<init>(Lme/jellysquid/mods/sodium/client/render/chunk/RenderSection;I)V")
    )
    public void considerNonEmptyIfWires(RenderSection render, CallbackInfoReturnable<ChunkRenderBuildTask> cir) {
        if (RubieEmptyChunkChecker.hasWires(render.getChunkPos())) {
            cir.setReturnValue(RubieEmptyChunkChecker.makeEmptyRebuildTask(
                    sectionCache, render.getChunkPos(), render, currentFrame
            ));
        }
    }

    // TODO look into making this non-invasive. Probably not really possible though
    @Redirect(
            method = "loadSection",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/chunk/LevelChunkSection;hasOnlyAir()Z")
    )
    private boolean wiresAreNotAir(LevelChunkSection instance, int x, int y, int z) {
        return instance.hasOnlyAir() && !RubieEmptyChunkChecker.hasWires(SectionPos.of(x, y, z));
    }
}
