package eu.midnightdust.cullleaves.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import eu.midnightdust.cullleaves.CullLeavesClient;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ModelBlockRenderer.class)
public class MixinBlockModelRenderer {
    @Inject(at = @At("HEAD"), method = "tesselateBlock", cancellable = true)
    private void cullleaves$cancelRendering(BlockAndTintGetter world, List<BlockModelPart> parts, BlockState state, BlockPos pos, PoseStack matrices, VertexConsumer vertexConsumer, boolean cull, int overlay, CallbackInfo ci) {
        if (state.getBlock() instanceof LeavesBlock &&
                CullLeavesClient.shouldHideBlock(world, pos)) ci.cancel();
    }
}
