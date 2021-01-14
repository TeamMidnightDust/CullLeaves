package eu.midnightdust.cullleaves;

import eu.midnightdust.cullleaves.config.CullLeavesConfig;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;

public class CullLeavesClient implements ClientModInitializer {
    public static CullLeavesConfig CL_CONFIG;

    public void onInitializeClient() {
        AutoConfig.register(CullLeavesConfig.class, JanksonConfigSerializer::new);
        CL_CONFIG = AutoConfig.getConfigHolder(CullLeavesConfig.class).getConfig();

        FabricLoader.getInstance().getModContainer("cullleaves").ifPresent(modContainer -> {
            ResourceManagerHelper.registerBuiltinResourcePack(new Identifier("cullleaves:smartleaves"), "resourcepacks/smartleaves", modContainer, true);
        });
    }
}
