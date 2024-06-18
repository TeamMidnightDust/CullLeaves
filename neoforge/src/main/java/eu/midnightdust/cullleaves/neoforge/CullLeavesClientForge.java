package eu.midnightdust.cullleaves.neoforge;

import eu.midnightdust.cullleaves.config.CullLeavesConfig;
import eu.midnightdust.lib.config.MidnightConfig;
import net.neoforged.fml.common.Mod;

@Mod("cullleaves")
public class CullLeavesClientForge {
    public CullLeavesClientForge() {
        MidnightConfig.init("cullleaves", CullLeavesConfig.class);
    }
}