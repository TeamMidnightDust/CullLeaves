package eu.midnightdust.cullleaves.neoforge.mixin.sodium;

import eu.midnightdust.cullleaves.CullLeavesClient;
import net.caffeinemc.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderer;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockRenderer.class)
public abstract class MixinBlockRenderer {
    // Unfortunately, we cannot use the level view from AbstractBlockRenderContext on NeoForge because of method conflicts
    @Unique private static final MinecraftClient cullleaves$client = MinecraftClient.getInstance();

    @Inject(at = @At("HEAD"), method = "renderModel", cancellable = true)
    public void cullleaves$cancelRendering(BakedModel model, BlockState state, BlockPos pos, BlockPos origin, CallbackInfo ci) {
        if (CullLeavesClient.forceHideInnerLeaves && state.getBlock() instanceof LeavesBlock && CullLeavesClient.shouldHideBlock(cullleaves$client.world, pos))
            ci.cancel();
    }
}
