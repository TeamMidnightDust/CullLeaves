package eu.midnightdust.cullleaves.mixin;

import eu.midnightdust.cullleaves.config.CullLeavesConfig;
import me.jellysquid.mods.sodium.client.gl.device.RenderDevice;
import me.jellysquid.mods.sodium.client.gl.util.ElementRange;
import me.jellysquid.mods.sodium.client.model.quad.properties.ModelQuadFacing;
import me.jellysquid.mods.sodium.client.render.chunk.*;
import me.jellysquid.mods.sodium.client.render.chunk.data.ChunkRenderBounds;
import me.jellysquid.mods.sodium.client.render.chunk.passes.BlockRenderPass;
import me.jellysquid.mods.sodium.client.render.vertex.type.ChunkVertexType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;
import java.util.List;

@Mixin(value = RegionChunkRenderer.class, remap = false, priority = 1100)
public abstract class MixinRegionChunkRenderer extends ShaderChunkRenderer {
    @Shadow @Final private boolean isBlockFaceCullingEnabled;
    @Shadow protected abstract void addDrawCall(ElementRange part, long baseIndexPointer, int baseVertexIndex);

    public MixinRegionChunkRenderer(RenderDevice device, ChunkVertexType vertexType) {
        super(device, vertexType);
    }

    @Inject(method = "buildDrawBatches", locals = LocalCapture.CAPTURE_FAILSOFT, at = @At(value = "INVOKE", target = "Lme/jellysquid/mods/sodium/client/render/chunk/ChunkGraphicsState;getModelPart(Lme/jellysquid/mods/sodium/client/model/quad/properties/ModelQuadFacing;)Lme/jellysquid/mods/sodium/client/gl/util/ElementRange;", ordinal = 0))
    public void cullleaves$fixTransparentBlockFaceCulling(List<RenderSection> sections, BlockRenderPass pass, ChunkCameraContext camera, CallbackInfoReturnable<Boolean> cir, Iterator var4, RenderSection render, ChunkGraphicsState state, ChunkRenderBounds bounds, long indexOffset, int baseVertex) {
        if (pass.getAlphaCutoff() != 0 && CullLeavesConfig.enabled && CullLeavesConfig.sodiumBlockFaceCullingFix && this.isBlockFaceCullingEnabled) {
            if (camera.posY <= bounds.y1) {
                this.addDrawCall(state.getModelPart(ModelQuadFacing.UP), indexOffset, baseVertex);
            }
            if (camera.posY >= bounds.y2) {
                this.addDrawCall(state.getModelPart(ModelQuadFacing.DOWN), indexOffset, baseVertex);
            }
            if (camera.posX <= bounds.x1) {
                this.addDrawCall(state.getModelPart(ModelQuadFacing.EAST), indexOffset, baseVertex);
            }
            if (camera.posX >= bounds.x2) {
                this.addDrawCall(state.getModelPart(ModelQuadFacing.WEST), indexOffset, baseVertex);
            }
            if (camera.posZ <= bounds.z1) {
                this.addDrawCall(state.getModelPart(ModelQuadFacing.SOUTH), indexOffset, baseVertex);
            }
            if (camera.posZ >= bounds.z2) {
                this.addDrawCall(state.getModelPart(ModelQuadFacing.NORTH), indexOffset, baseVertex);
            }
        }
    }
}
