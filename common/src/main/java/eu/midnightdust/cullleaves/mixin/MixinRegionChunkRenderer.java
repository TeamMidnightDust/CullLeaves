package eu.midnightdust.cullleaves.mixin;

import eu.midnightdust.cullleaves.config.CullLeavesConfig;
import me.jellysquid.mods.sodium.client.gl.device.RenderDevice;
import me.jellysquid.mods.sodium.client.gl.util.ElementRange;
import me.jellysquid.mods.sodium.client.model.quad.properties.ModelQuadFacing;
import me.jellysquid.mods.sodium.client.model.vertex.type.ChunkVertexType;
import me.jellysquid.mods.sodium.client.render.chunk.*;
import me.jellysquid.mods.sodium.client.render.chunk.passes.BlockRenderPass;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = RegionChunkRenderer.class, remap = false)
public abstract class MixinRegionChunkRenderer extends ShaderChunkRenderer {
    @Shadow @Final private boolean isBlockFaceCullingEnabled;
    @Shadow protected abstract void addDrawCall(ElementRange part, long baseIndexPointer, int baseVertexIndex);
    @Unique private boolean doFix = false;

    public MixinRegionChunkRenderer(RenderDevice device, ChunkVertexType vertexType) {
        super(device, vertexType);
    }

    @Inject(method = "buildDrawBatches", at = @At(value = "INVOKE", target = "Lme/jellysquid/mods/sodium/client/render/chunk/RenderSection;getBounds()Lme/jellysquid/mods/sodium/client/render/chunk/data/ChunkRenderBounds;", ordinal = 0))
    public void cullleaves$shouldApplyTransparentBlockFaceCullingFix(List<RenderSection> sections, BlockRenderPass pass, ChunkCameraContext camera, CallbackInfoReturnable<Boolean> cir) {
        doFix = pass.getAlphaCutoff() != 0 && CullLeavesConfig.enabled && CullLeavesConfig.sodiumBlockFaceCullingFix && this.isBlockFaceCullingEnabled;
    }
    @Redirect(method = "buildDrawBatches", at = @At(value = "INVOKE", target = "Lme/jellysquid/mods/sodium/client/render/chunk/ChunkGraphicsState;getModelPart(Lme/jellysquid/mods/sodium/client/model/quad/properties/ModelQuadFacing;)Lme/jellysquid/mods/sodium/client/gl/util/ElementRange;", ordinal = 0))
    public ElementRange cullleaves$fixTransparentBlockFaceCulling(ChunkGraphicsState state, ModelQuadFacing facing) {
        if (doFix) {
            long indexOffset = state.getIndexSegment().getOffset();
            int baseVertex = state.getVertexSegment().getOffset() / this.vertexFormat.getStride();
            for (ModelQuadFacing direction : ModelQuadFacing.DIRECTIONS) {
                this.addDrawCall(state.getModelPart(direction), indexOffset, baseVertex);
            }
        }
        return state.getModelPart(ModelQuadFacing.UNASSIGNED);
    }
}
