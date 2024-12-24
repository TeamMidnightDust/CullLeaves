package eu.midnightdust.cullleaves.mixin;

import eu.midnightdust.cullleaves.CullLeavesClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = MangroveRootsBlock.class, priority = 1900)
@Environment(EnvType.CLIENT)
public abstract class MixinMangroveRootsBlock extends Block {

    public MixinMangroveRootsBlock(Settings settings) {
        super(settings);
    }

    @Override
    public boolean isSideInvisible(BlockState state, BlockState neighborState, Direction offset) {
        return CullLeavesClient.isRootSideInvisible(neighborState);
    }
}