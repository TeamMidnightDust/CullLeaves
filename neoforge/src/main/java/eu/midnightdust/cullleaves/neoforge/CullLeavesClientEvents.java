package eu.midnightdust.cullleaves.neoforge;

import eu.midnightdust.cullleaves.CullLeavesClient;
import net.minecraft.network.packet.PacketType;
import net.minecraft.resource.*;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.AddClientReloadListenersEvent;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.resource.ResourcePackLoader;
import net.neoforged.neoforgespi.locating.IModFile;

import java.util.Optional;

@EventBusSubscriber(modid = CullLeavesClient.MOD_ID, value = Dist.CLIENT)
public class CullLeavesClientEvents {
    @SubscribeEvent
    public static void addPackFinders(AddPackFindersEvent event) {
        if (event.getPackType() == ResourceType.CLIENT_RESOURCES) {
            event.addPackFinders(Identifier.of(CullLeavesClient.MOD_ID, "resourcepacks/smartleaves"), ResourceType.CLIENT_RESOURCES, Text.of("cullleaves/smartleaves"), ResourcePackSource.BUILTIN, false, ResourcePackProfile.InsertionPosition.TOP);
        }
    }
    @SubscribeEvent
    public static void onResourceReload(AddClientReloadListenersEvent event) {
        event.addListener(Identifier.of(CullLeavesClient.MOD_ID, "resourcepack_options"), CullLeavesClient.ReloadListener.INSTANCE);
    }
}
