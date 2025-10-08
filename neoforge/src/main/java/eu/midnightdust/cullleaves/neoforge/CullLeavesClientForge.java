package eu.midnightdust.cullleaves.neoforge;

import eu.midnightdust.cullleaves.CullLeavesClient;
import eu.midnightdust.cullleaves.config.CullLeavesConfig;
import eu.midnightdust.lib.config.MidnightConfig;
import net.neoforged.fml.common.Mod;

@Mod(CullLeavesClient.MOD_ID)
public class CullLeavesClientForge {
    public CullLeavesClientForge() {
        MidnightConfig.init(CullLeavesClient.MOD_ID, CullLeavesConfig.class);
    }
}