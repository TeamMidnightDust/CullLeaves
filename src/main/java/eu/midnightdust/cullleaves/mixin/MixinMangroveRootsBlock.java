package eu.midnightdust.cullleaves.mixin;

import eu.midnightdust.cullleaves.CullLeavesClient;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.MangroveRootsBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = MangroveRootsBlock.class, priority = 1900)
public abstract class MixinMangroveRootsBlock extends Block {
    public MixinMangroveRootsBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean skipRendering(BlockState state, BlockState neighborState, Direction offset) {
        return CullLeavesClient.isRootSideInvisible(neighborState);
    }
}