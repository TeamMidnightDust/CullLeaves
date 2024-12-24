package eu.midnightdust.cullleaves.fabric;

import eu.midnightdust.cullleaves.CullLeavesClient;
import eu.midnightdust.cullleaves.config.CullLeavesConfig;
import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
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
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
            @Override
            public Identifier getFabricId() {
                return Identifier.of(CullLeavesClient.MOD_ID, "resourcepack_options");
            }
            @Override
            public void reload(ResourceManager manager) {
                CullLeavesClient.ReloadListener.INSTANCE.reload(manager);
            }
        });
    }
}
