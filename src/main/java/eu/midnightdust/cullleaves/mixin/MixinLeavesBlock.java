package eu.midnightdust.cullleaves.mixin;

import eu.midnightdust.cullleaves.CullLeavesClient;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = LeavesBlock.class, priority = 1900)
public abstract class MixinLeavesBlock extends Block {
    public MixinLeavesBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean skipRendering(BlockState state, BlockState neighborState, Direction offset) {
        return CullLeavesClient.isLeafSideInvisible(neighborState);
    }
}