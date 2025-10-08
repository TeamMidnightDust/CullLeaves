package eu.midnightdust.cullleaves.fabric;

import eu.midnightdust.cullleaves.CullLeavesClient;
import eu.midnightdust.cullleaves.config.CullLeavesConfig;
import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

public class CullLeavesClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        MidnightConfig.init(CullLeavesClient.MOD_ID, CullLeavesConfig.class);
        FabricLoader.getInstance().getModContainer("cullleaves").ifPresent(modContainer -> {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of(CullLeavesClient.MOD_ID,"smartleaves"), modContainer, ResourcePackActivationType.NORMAL);
        });
        ResourceLoader.get(ResourceType.CLIENT_RESOURCES).registerReloader(Identifier.of(CullLeavesClient.MOD_ID, "resourcepack_options"), CullLeavesClient.ReloadListener.INSTANCE);
    }
}
