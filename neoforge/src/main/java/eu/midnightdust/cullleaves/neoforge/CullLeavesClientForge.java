package eu.midnightdust.cullleaves.neoforge;

import eu.midnightdust.cullleaves.config.CullLeavesConfig;
import eu.midnightdust.lib.config.MidnightConfig;
import net.neoforged.fml.IExtensionPoint;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.ConfigScreenHandler;

import static net.neoforged.fml.IExtensionPoint.DisplayTest.IGNORESERVERONLY;

@Mod("cullleaves")
public class CullLeavesClientForge {
    public CullLeavesClientForge() {
        MidnightConfig.init("cullleaves", CullLeavesConfig.class);
        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> IGNORESERVERONLY, (remote, server) -> true));
        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () ->
                new ConfigScreenHandler.ConfigScreenFactory((client, parent) -> MidnightConfig.getScreen(parent, "cullleaves")));
    }
}