package eu.midnightdust.cullleaves.quilt;

import eu.midnightdust.cullleaves.CullLeavesClient;
import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.quiltmc.qsl.resource.loader.api.ResourceLoader;
import org.quiltmc.qsl.resource.loader.api.ResourcePackActivationType;

public class CullLeavesClientQuilt implements ClientModInitializer {
    @Override
    public void onInitializeClient(ModContainer mod) {
        CullLeavesClient.onInitializeClient();
        ResourceLoader.registerBuiltinResourcePack(new Identifier("cullleaves:smartleaves"), mod, ResourcePackActivationType.NORMAL);
    }
}
