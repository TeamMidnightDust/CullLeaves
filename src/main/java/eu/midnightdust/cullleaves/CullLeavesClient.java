package eu.midnightdust.cullleaves;

import eu.midnightdust.cullleaves.config.CullLeavesConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.ModContainer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;

public class CullLeavesClient implements ClientModInitializer {

    public void onInitializeClient() {
        CullLeavesConfig.init("cullleaves", CullLeavesConfig.class);

        FabricLoader.getInstance().getModContainer("cullleaves").ifPresent(modContainer -> {
            ResourceManagerHelper.registerBuiltinResourcePack(new Identifier("cullleaves:smartleaves"), modContainer, ResourcePackActivationType.NORMAL);
        });
    }
}
