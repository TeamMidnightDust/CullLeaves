package eu.midnightdust.cullleaves.forge;

import eu.midnightdust.cullleaves.CullLeavesClient;
import eu.midnightdust.lib.config.MidnightConfig;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;

@Mod("cullleaves")
public class CullLeavesClientForge {
    public CullLeavesClientForge() {
        CullLeavesClient.onInitializeClient();
        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () ->
                new ConfigScreenHandler.ConfigScreenFactory((client, parent) -> MidnightConfig.getScreen(parent, "cullleaves")));
    }
}
