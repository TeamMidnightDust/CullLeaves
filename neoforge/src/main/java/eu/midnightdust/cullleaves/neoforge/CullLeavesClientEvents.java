package eu.midnightdust.cullleaves.neoforge;

import net.minecraft.resource.*;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforgespi.locating.IModFile;

import java.util.Optional;

@EventBusSubscriber(modid = "cullleaves", bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class CullLeavesClientEvents {
    @SubscribeEvent
    public static void addPackFinders(AddPackFindersEvent event) {
        if (event.getPackType() == ResourceType.CLIENT_RESOURCES) {
            registerResourcePack(event, Identifier.of("cullleaves", "smartleaves"), false);
        }
    }
    private static void registerResourcePack(AddPackFindersEvent event, Identifier id, boolean alwaysEnabled) {
        event.addRepositorySource(((profileAdder) -> {
            IModFile file = ModList.get().getModFileById(id.getNamespace()).getFile();
            try {
                ResourcePackProfile.PackFactory pack = new DirectoryResourcePack.DirectoryBackedFactory(file.findResource("resourcepacks/" + id.getPath()));
                ResourcePackInfo info = new ResourcePackInfo(id.toString(), Text.of(id.getNamespace()+"/"+id.getPath()), ResourcePackSource.BUILTIN, Optional.empty());
                ResourcePackProfile packProfile = ResourcePackProfile.create(info, pack, ResourceType.CLIENT_RESOURCES, new ResourcePackPosition(alwaysEnabled, ResourcePackProfile.InsertionPosition.TOP, false));
                if (packProfile != null) {
                    profileAdder.accept(packProfile);
                }
            } catch (NullPointerException e) {e.fillInStackTrace();}
        }));
    }
}
