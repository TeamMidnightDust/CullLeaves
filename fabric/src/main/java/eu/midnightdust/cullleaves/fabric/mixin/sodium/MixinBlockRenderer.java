package eu.midnightdust.cullleaves.fabric.mixin.sodium;

import eu.midnightdust.cullleaves.CullLeavesClient;
import net.caffeinemc.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderer;
import net.caffeinemc.mods.sodium.client.render.frapi.render.AbstractBlockRenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockRenderer.class)
public abstract class MixinBlockRenderer extends AbstractBlockRenderContext {
    @Inject(at = @At("HEAD"), method = "renderModel", cancellable = true)
    public void cullleaves$cancelRendering(BakedModel model, BlockState state, BlockPos pos, BlockPos origin, CallbackInfo ci) {
        if (state.getBlock() instanceof LeavesBlock && CullLeavesClient.shouldHideBlock(this.level, pos))
            ci.cancel();
    }
}
