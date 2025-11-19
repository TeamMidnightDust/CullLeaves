package eu.midnightdust.cullleaves.mixin.sodium;

import eu.midnightdust.cullleaves.CullLeavesClient;
import net.caffeinemc.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderer;
import net.caffeinemc.mods.sodium.client.render.frapi.render.AbstractBlockRenderContext;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockRenderer.class)
public abstract class MixinBlockRenderer extends AbstractBlockRenderContext {
    @Inject(at = @At("HEAD"), method = "renderModel", cancellable = true)
    public void cullleaves$cancelRendering(BlockStateModel model, BlockState state, BlockPos pos, BlockPos origin, CallbackInfo ci) {
        if (state.getBlock() instanceof LeavesBlock && CullLeavesClient.shouldHideBlock(this.level, pos))
            ci.cancel();
    }
}
