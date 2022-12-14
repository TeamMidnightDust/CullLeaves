package eu.midnightdust.cullleaves.forge;

import net.minecraft.resource.*;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forgespi.locating.IModFile;
import net.minecraftforge.resource.PathPackResources;

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
            try (PathPackResources pack = new PathPackResources(id.toString(), true, file.findResource("resourcepacks/"+id.getPath()))) {
                profileAdder.accept(ResourcePackProfile.create(id.toString(), Text.of(id.getNamespace()+"/"+id.getPath()), alwaysEnabled, a -> pack, ResourceType.CLIENT_RESOURCES, ResourcePackProfile.InsertionPosition.TOP, ResourcePackSource.BUILTIN));
            } catch (NullPointerException e) {e.printStackTrace();}
        }));
    }
}
