package eu.midnightdust.cullleaves;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import eu.midnightdust.cullleaves.config.CullLeavesConfig;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.MangroveRootsBlock;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.SynchronousResourceReloader;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class CullLeavesClient {
    public static final String MOD_ID = "cullleaves";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static boolean forceLeafCulling = false;
    public static boolean forceHideInnerLeaves = false;

    public static boolean shouldHideBlock(BlockView world, BlockPos pos) {
        if (CullLeavesClient.forceHideInnerLeaves) {
            boolean shouldForceCull = true;
            for (Direction dir : Direction.values()) {
                BlockState otherState = world.getBlockState(pos.offset(dir));
                if (!(otherState.getBlock() instanceof LeavesBlock) &&
                        !otherState.isSideSolidFullSquare(world, pos, dir.getOpposite())) {
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
        }
        else return false;
    }
    public static boolean isRootSideInvisible(BlockState neighborState) {
        if (CullLeavesConfig.cullRoots) {
            return neighborState.getBlock() instanceof MangroveRootsBlock;
        }
        else return false;
    }

    public static class ReloadListener implements SynchronousResourceReloader {
        public static final ReloadListener INSTANCE = new ReloadListener();

        private ReloadListener() {}

        @Override
        public void reload(ResourceManager manager) {
            CullLeavesClient.forceLeafCulling = false;
            CullLeavesClient.forceHideInnerLeaves = false;
            manager.findResources("options", path -> path.toString().startsWith("cullleaves") && path.toString().endsWith("options.json")).forEach((id, resource) -> {
                try {
                    JsonObject json = JsonParser.parseReader(resource.getReader()).getAsJsonObject();
                    if (json.has("forceLeafCulling")) {
                        CullLeavesClient.forceLeafCulling = json.get("forceLeafCulling").getAsBoolean();
                        LOGGER.info("Forcing leaf culling as requested by resourcepack");
                    }
                    if (json.has("forceHideInnerLeaves")) {
                        CullLeavesClient.forceHideInnerLeaves = json.get("forceHideInnerLeaves").getAsBoolean();
                        LOGGER.info("Not rendering inner leaves as requested by resourcepack");
                    }
                } catch (IOException e) { throw new RuntimeException(e); }
            });
        }
    }
}
