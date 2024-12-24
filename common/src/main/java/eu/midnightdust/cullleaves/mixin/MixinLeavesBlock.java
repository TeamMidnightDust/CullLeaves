package eu.midnightdust.cullleaves.mixin;

import eu.midnightdust.cullleaves.CullLeavesClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = LeavesBlock.class, priority = 1900)
@Environment(EnvType.CLIENT)
public abstract class MixinLeavesBlock extends Block {

    public MixinLeavesBlock(Settings settings) {
        super(settings);
    }

    @Override
    public boolean isSideInvisible(BlockState state, BlockState neighborState, Direction offset) {
        return CullLeavesClient.isLeafSideInvisible(neighborState);
    }
}