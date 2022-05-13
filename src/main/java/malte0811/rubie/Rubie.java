package malte0811.rubie;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod(Rubie.MODID)
@Mod.EventBusSubscriber(modid = Rubie.MODID, value = Dist.CLIENT)
public class Rubie {
    public static final String MODID = "rubie";

    @SubscribeEvent
    public static void registerReloadListener(AddReloadListenerEvent ev) {
        ev.addListener(new RubieConnectionRenderer());
    }
}
