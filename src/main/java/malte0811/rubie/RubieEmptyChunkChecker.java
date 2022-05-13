package malte0811.rubie;

import blusunrize.immersiveengineering.api.wires.GlobalWireNetwork;
import me.jellysquid.mods.sodium.client.render.chunk.RenderSection;
import me.jellysquid.mods.sodium.client.render.chunk.tasks.ChunkRenderBuildTask;
import me.jellysquid.mods.sodium.client.render.chunk.tasks.ChunkRenderRebuildTask;
import me.jellysquid.mods.sodium.client.world.cloned.ChunkRenderContext;
import me.jellysquid.mods.sodium.client.world.cloned.ClonedChunkSection;
import me.jellysquid.mods.sodium.client.world.cloned.ClonedChunkSectionCache;
import net.minecraft.client.Minecraft;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

import java.util.Arrays;

public class RubieEmptyChunkChecker {
    public static boolean hasWires(SectionPos origin) {
        var globalNet = GlobalWireNetwork.getNetwork(Minecraft.getInstance().level);
        var wiresInSection = globalNet.getCollisionData().getWiresIn(origin);
        return wiresInSection != null && !wiresInSection.isEmpty();
    }

    public static ChunkRenderBuildTask makeEmptyRebuildTask(
            ClonedChunkSectionCache sectionCache, SectionPos origin, RenderSection render, int frame
    ) {
        var sections = new ClonedChunkSection[64];
        // TODO rethink this mess, and possibly call release?
        var centerSection = sectionCache.acquire(origin.x(), origin.y(), origin.z());
        Arrays.fill(sections, centerSection);
        var sectionBB = new BoundingBox(
                origin.minBlockX() - 2, origin.minBlockY() - 2, origin.minBlockZ() - 2,
                origin.maxBlockX() + 2, origin.maxBlockY() + 2, origin.maxBlockZ() + 2
        );
        var context = new ChunkRenderContext(origin, sections, sectionBB);
        return new ChunkRenderRebuildTask(render, context, frame);
    }
}
