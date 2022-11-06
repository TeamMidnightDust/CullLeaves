package eu.midnightdust.cullleaves.quilt;

import eu.midnightdust.cullleaves.config.CullLeavesConfig;
import eu.midnightdust.lib.config.MidnightConfig;
import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.quiltmc.qsl.resource.loader.api.ResourceLoader;
import org.quiltmc.qsl.resource.loader.api.ResourcePackActivationType;

public class CullLeavesClientQuilt implements ClientModInitializer {
    @Override
    public void onInitializeClient(ModContainer mod) {
        MidnightConfig.init("cullleaves", CullLeavesConfig.class);
        ResourceLoader.registerBuiltinResourcePack(new Identifier("cullleaves:smartleaves"), mod, ResourcePackActivationType.NORMAL);
    }
}
