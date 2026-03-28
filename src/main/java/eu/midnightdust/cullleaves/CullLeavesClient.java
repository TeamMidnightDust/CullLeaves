package eu.midnightdust.cullleaves;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import eu.midnightdust.cullleaves.config.CullLeavesConfig;
import eu.midnightdust.lib.config.MidnightConfig;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;

import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.MangroveRootsBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

//? fabric {
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
//? if >= 1.21.11 {
import net.fabricmc.fabric.api.resource.v1.pack.PackActivationType;
//?} else {
/*import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
*///?}
//? if >= 1.21.9 {
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
//?} else {
/*import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
*///?}

//?} neoforge {
/*import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.AddPackFindersEvent;
    //? if >= 1.21.4 {
    import net.neoforged.neoforge.client.event.AddClientReloadListenersEvent;
    //?} else {
    /^import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
    ^///?}
*///?}

//? forge {
/*import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forgespi.locating.IModFile;
import net.minecraftforge.network.NetworkConstants;
import net.minecraftforge.resource.PathPackResources;
*///?}

//? neoforge || forge
//@Mod(CullLeavesClient.MOD_ID)
public class CullLeavesClient /*? fabric {*/ implements ModInitializer /*?}*/ {
    public static final String MOD_ID = "cullleaves";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static boolean forceLeafCulling = false;
    public static boolean forceHideInnerLeaves = false;

    public static boolean shouldHideBlock(BlockAndTintGetter world, BlockPos pos) {
        if (CullLeavesClient.forceHideInnerLeaves) {
            boolean shouldForceCull = true;
            for (Direction dir : Direction.values()) {
                BlockState otherState = world.getBlockState(pos.relative(dir));
                if (!(otherState.getBlock() instanceof LeavesBlock) &&
                        !otherState.isFaceSturdy(world, pos, dir.getOpposite())) {
                    shouldForceCull = false;
                    break;
                }
            }
            return shouldForceCull;
        }
        return false;
    }

    public static boolean isLeafSideInvisible(BlockState neighborState) {
        if (CullLeavesConfig.enabled || CullLeavesClient.forceLeafCulling) {
            return neighborState.getBlock() instanceof LeavesBlock;
        } else return false;
    }

    public static boolean isRootSideInvisible(BlockState neighborState) {
        if (CullLeavesConfig.cullRoots) {
            return neighborState.getBlock() instanceof MangroveRootsBlock;
        } else return false;
    }

    public static class ReloadListener implements ResourceManagerReloadListener {
        public static final ReloadListener INSTANCE = new ReloadListener();

        private ReloadListener() {
        }

        @Override
        public void onResourceManagerReload(ResourceManager manager) {
            CullLeavesClient.forceLeafCulling = false;
            CullLeavesClient.forceHideInnerLeaves = false;
            manager.listResources("options", path -> path.toString().startsWith("cullleaves") && path.toString().endsWith("options.json")).forEach((id, resource) -> {
                try {
                    JsonObject json = JsonParser.parseReader(resource.openAsReader()).getAsJsonObject();
                    if (json.has("forceLeafCulling")) {
                        CullLeavesClient.forceLeafCulling = json.get("forceLeafCulling").getAsBoolean();
                        LOGGER.info("Forcing leaf culling as requested by resourcepack");
                    }
                    if (json.has("forceHideInnerLeaves")) {
                        CullLeavesClient.forceHideInnerLeaves = json.get("forceHideInnerLeaves").getAsBoolean();
                        LOGGER.info("Not rendering inner leaves as requested by resourcepack");
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    //? fabric {
    @Override
    public void onInitialize() {
        MidnightConfig.init(CullLeavesClient.MOD_ID, CullLeavesConfig.class);
        FabricLoader.getInstance().getModContainer("cullleaves").ifPresent(modContainer -> {
            //? if >= 1.21.11 {
            ResourceLoader.registerBuiltinPack(Identifier.fromNamespaceAndPath(CullLeavesClient.MOD_ID, "smartleaves"), modContainer, PackActivationType.NORMAL);
            //?} else {
            //ResourceManagerHelper.registerBuiltinResourcePack(ResourceLocation.fromNamespaceAndPath(CullLeavesClient.MOD_ID, "smartleaves"), modContainer, ResourcePackActivationType.NORMAL);
            //?}
        });
        //? if >= 1.21.9 {
        //~ if >= 26.1 '.registerReloader' -> '.registerReloadListener'
        ResourceLoader.get(PackType.CLIENT_RESOURCES).registerReloadListener(Identifier.fromNamespaceAndPath(CullLeavesClient.MOD_ID, "resourcepack_options"), CullLeavesClient.ReloadListener.INSTANCE);
        //?} else {
        /*ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
            @Override
            public Identifier getFabricId() {
                return ResourceLocation.fromNamespaceAndPath(CullLeavesClient.MOD_ID, "resourcepack_options");
            }
            @Override
            public void onResourceManagerReload(ResourceManager manager) {
                CullLeavesClient.ReloadListener.INSTANCE.onResourceManagerReload(manager);
            }
        });
        *///?}
    }
    //?} else if neoforge {
    /*public CullLeavesClient() {
        MidnightConfig.init(CullLeavesClient.MOD_ID, CullLeavesConfig.class);
    }

    @EventBusSubscriber(modid = CullLeavesClient.MOD_ID, value = Dist.CLIENT/^? if <=1.21.5 {^/, bus = EventBusSubscriber.Bus.MOD /^?}^/)
    public static class CullLeavesClientEvents {
        @SubscribeEvent
        public static void addPackFinders(AddPackFindersEvent event) {
            if (event.getPackType() == PackType.CLIENT_RESOURCES) {
                event.addPackFinders(Identifier.fromNamespaceAndPath(CullLeavesClient.MOD_ID, "resourcepacks/smartleaves"), PackType.CLIENT_RESOURCES, Component.literal("cullleaves/smartleaves"), PackSource.BUILT_IN, false, Pack.Position.TOP);
            }
        }
        @SubscribeEvent
        public static void onResourceReload(/^? if >= 1.21.4 {^/ AddClientReloadListenersEvent /^?} else {^//^RegisterClientReloadListenersEvent ^//^?}^/ event) {
            event. /^? if >= 1.21.4 {^/ addListener(Identifier.fromNamespaceAndPath(CullLeavesClient.MOD_ID, "resourcepack_options"), /^?} else {^/ /^registerReloadListener( ^//^?}^/ CullLeavesClient.ReloadListener.INSTANCE);
        }
    }
    *///?} else if forge {
    /*public CullLeavesClient() {
        MidnightConfig.init(MOD_ID, CullLeavesConfig.class);
        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> NetworkConstants.IGNORESERVERONLY, (remote, server) -> true));
        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () ->
                new ConfigScreenHandler.ConfigScreenFactory((client, parent) -> MidnightConfig.getScreen(parent, "cullleaves")));
        //MinecraftForge.EVENT_BUS.register(new CullLeavesClientEvents());
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class CullLeavesClientEvents {
        @SubscribeEvent
        public static void addPackFinders(AddPackFindersEvent event) {
            if (event.getPackType() == PackType.CLIENT_RESOURCES) {
                registerResourcePack(event, ResourceLocation.fromNamespaceAndPath(MOD_ID, "smartleaves"), false);
            }
        }
        private static void registerResourcePack(AddPackFindersEvent event, Identifier id, boolean alwaysEnabled) {
            event.addRepositorySource((profileAdder -> {
                IModFile file = ModList.get().getModFileById(id.getNamespace()).getFile();
                try (PathPackResources pack = new PathPackResources(id.toString(), true, file.findResource("resourcepacks/"+id.getPath()))) {
                    profileAdder.accept(Pack.readMetaAndCreate(id.toString(), Component.literal(id.getNamespace()+"/"+id.getPath()), alwaysEnabled, a -> pack, PackType.CLIENT_RESOURCES, Pack.Position.TOP, PackSource.BUILT_IN));
                } catch (NullPointerException e) {e.printStackTrace();}
            }));
        }
    }
    *///?}
}

