package eu.midnightdust.cullleaves;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import eu.midnightdust.cullleaves.config.CullLeavesConfig;
import eu.midnightdust.lib.config.MidnightConfig;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;

import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.MangroveRootsBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

//? fabric {
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
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
import net.neoforged.neoforge.client.event.AddClientReloadListenersEvent;
import net.neoforged.neoforge.event.AddPackFindersEvent;
*///?}

//? neoforge
/*@Mod(CullLeavesClient.MOD_ID)*/
public class CullLeavesClient /*? fabric {*/ implements ClientModInitializer /*?}*/ {
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
    public void onInitializeClient() {
        MidnightConfig.init(CullLeavesClient.MOD_ID, CullLeavesConfig.class);
        FabricLoader.getInstance().getModContainer("cullleaves").ifPresent(modContainer -> {
            ResourceManagerHelper.registerBuiltinResourcePack(ResourceLocation.fromNamespaceAndPath(CullLeavesClient.MOD_ID, "smartleaves"), modContainer, ResourcePackActivationType.NORMAL);
        });
        //? if >= 1.21.9 {
        ResourceLoader.get(PackType.CLIENT_RESOURCES).registerReloader(ResourceLocation.fromNamespaceAndPath(CullLeavesClient.MOD_ID, "resourcepack_options"), CullLeavesClient.ReloadListener.INSTANCE);
        //?} else {
        /*ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
            @Override
            public ResourceLocation getFabricId() {
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

    @EventBusSubscriber(modid = CullLeavesClient.MOD_ID, value = Dist.CLIENT/^? if <=1.21.5 {^//^, bus = EventBusSubscriber.Bus.MOD ^//^?}^/)
    public static class CullLeavesClientEvents {
        @SubscribeEvent
        public static void addPackFinders(AddPackFindersEvent event) {
            if (event.getPackType() == PackType.CLIENT_RESOURCES) {
                event.addPackFinders(ResourceLocation.fromNamespaceAndPath(CullLeavesClient.MOD_ID, "resourcepacks/smartleaves"), PackType.CLIENT_RESOURCES, Component.literal("cullleaves/smartleaves"), PackSource.BUILT_IN, false, Pack.Position.TOP);
            }
        }
        @SubscribeEvent
        public static void onResourceReload(AddClientReloadListenersEvent event) {
            event.addListener(ResourceLocation.fromNamespaceAndPath(CullLeavesClient.MOD_ID, "resourcepack_options"), CullLeavesClient.ReloadListener.INSTANCE);
        }
    }
    *///?}
}

