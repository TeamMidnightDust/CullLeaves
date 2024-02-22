package eu.midnightdust.cullleaves.neoforge;

import net.minecraft.resource.DirectoryResourcePack;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ResourcePackSource;
import net.minecraft.resource.ResourceType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforgespi.locating.IModFile;

@Mod.EventBusSubscriber(modid = "cullleaves", bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class CullLeavesClientEvents {
    @SubscribeEvent
    public static void addPackFinders(AddPackFindersEvent event) {
        if (event.getPackType() == ResourceType.CLIENT_RESOURCES) {
            registerResourcePack(event, new Identifier("cullleaves", "smartleaves"), false);
        }
    }
    private static void registerResourcePack(AddPackFindersEvent event, Identifier id, boolean alwaysEnabled) {
        event.addRepositorySource((profileAdder -> {
            IModFile file = ModList.get().getModFileById(id.getNamespace()).getFile();
            try {
                ResourcePackProfile.PackFactory pack = new DirectoryResourcePack.DirectoryBackedFactory(file.findResource("resourcepacks/" + id.getPath()), true);
                profileAdder.accept(ResourcePackProfile.create(id.toString(), Text.of(id.getNamespace()+"/"+id.getPath()), alwaysEnabled, pack, ResourceType.CLIENT_RESOURCES, ResourcePackProfile.InsertionPosition.TOP, ResourcePackSource.BUILTIN));
            } catch (NullPointerException e) {e.fillInStackTrace();}
        }));
    }
}
