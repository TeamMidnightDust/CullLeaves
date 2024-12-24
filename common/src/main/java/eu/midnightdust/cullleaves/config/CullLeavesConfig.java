package eu.midnightdust.cullleaves.config;

import eu.midnightdust.lib.config.MidnightConfig;
import net.minecraft.client.MinecraftClient;

public class CullLeavesConfig extends MidnightConfig {
    @Entry //  Enable/Disable the mod. Requires Chunk Reload (F3 + A).
    public static boolean enabled = true;
    @Entry //  Enable/Disable culling on Mangrove Roots.
    public static boolean cullRoots = true;

    @Override
    public void writeChanges(String modid) {
        MinecraftClient.getInstance().worldRenderer.reload();
        super.writeChanges(modid);
    }
}
