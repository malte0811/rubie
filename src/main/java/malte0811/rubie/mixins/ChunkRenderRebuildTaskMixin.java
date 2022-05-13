package malte0811.rubie.mixins;

import malte0811.rubie.RubieConnectionRenderer;
import me.jellysquid.mods.sodium.client.gl.compile.ChunkBuildContext;
import me.jellysquid.mods.sodium.client.render.chunk.RenderSection;
import me.jellysquid.mods.sodium.client.render.chunk.compile.ChunkBuildResult;
import me.jellysquid.mods.sodium.client.render.chunk.tasks.ChunkRenderRebuildTask;
import me.jellysquid.mods.sodium.client.util.task.CancellationSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChunkRenderRebuildTask.class)
public class ChunkRenderRebuildTaskMixin {
    @Shadow
    @Final
    private RenderSection render;

    @Inject(
            method = "performBuild",
            at = @At(value = "INVOKE", target = "Ljava/util/EnumMap;<init>(Ljava/lang/Class;)V"),
            remap = false
    )
    public void renderIEWires(
            ChunkBuildContext buildContext,
            CancellationSource cancellationSource,
            CallbackInfoReturnable<ChunkBuildResult> cir
    ) {
        RubieConnectionRenderer.renderConnectionsInSection(
                buildContext.buffers, buildContext.cache.getWorldSlice(), render.getChunkPos()
        );
    }
}
