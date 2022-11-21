package eu.midnightdust.cullleaves.mixin;

import eu.midnightdust.cullleaves.config.CullLeavesConfig;
import me.jellysquid.mods.sodium.client.gl.device.RenderDevice;
import me.jellysquid.mods.sodium.client.gl.util.ElementRange;
import me.jellysquid.mods.sodium.client.model.quad.properties.ModelQuadFacing;
import me.jellysquid.mods.sodium.client.model.vertex.type.ChunkVertexType;
import me.jellysquid.mods.sodium.client.render.chunk.*;
import me.jellysquid.mods.sodium.client.render.chunk.data.ChunkRenderBounds;
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

@Mixin(value = RegionChunkRenderer.class, remap = false, priority = 1100)
public abstract class MixinRegionChunkRenderer extends ShaderChunkRenderer {
    @Shadow @Final private boolean isBlockFaceCullingEnabled;
    @Shadow protected abstract void addDrawCall(ElementRange part, long baseIndexPointer, int baseVertexIndex);
    @Unique private boolean doFix = false;
    @Unique private ChunkCameraContext camera;
    @Unique private ChunkRenderBounds bounds;

    public MixinRegionChunkRenderer(RenderDevice device, ChunkVertexType vertexType) {
        super(device, vertexType);
    }

    @Inject(method = "buildDrawBatches", at = @At(value = "INVOKE", target = "Lme/jellysquid/mods/sodium/client/render/chunk/RenderSection;getBounds()Lme/jellysquid/mods/sodium/client/render/chunk/data/ChunkRenderBounds;", ordinal = 0))
    public void cullleaves$shouldApplyTransparentBlockFaceCullingFix(List<RenderSection> sections, BlockRenderPass pass, ChunkCameraContext camera, CallbackInfoReturnable<Boolean> cir) {
        doFix = pass.getAlphaCutoff() != 0 && CullLeavesConfig.enabled && CullLeavesConfig.sodiumBlockFaceCullingFix && this.isBlockFaceCullingEnabled;
        this.camera = camera;
    }
    @Redirect(method = "buildDrawBatches", at = @At(value = "INVOKE", target = "Lme/jellysquid/mods/sodium/client/render/chunk/RenderSection;getBounds()Lme/jellysquid/mods/sodium/client/render/chunk/data/ChunkRenderBounds;", ordinal = 0))
    public ChunkRenderBounds cullleaves$getChunkRenderBounds(RenderSection instance) {
        bounds = instance.getBounds();
        return bounds;
    }
    @Redirect(method = "buildDrawBatches", at = @At(value = "INVOKE", target = "Lme/jellysquid/mods/sodium/client/render/chunk/ChunkGraphicsState;getModelPart(Lme/jellysquid/mods/sodium/client/model/quad/properties/ModelQuadFacing;)Lme/jellysquid/mods/sodium/client/gl/util/ElementRange;", ordinal = 0))
    public ElementRange cullleaves$fixTransparentBlockFaceCulling(ChunkGraphicsState state, ModelQuadFacing facing) {
        if (doFix) {
            long indexOffset = state.getIndexSegment().getOffset();
            int baseVertex = state.getVertexSegment().getOffset() / this.vertexFormat.getStride();
            if (this.isBlockFaceCullingEnabled) {
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
        return state.getModelPart(ModelQuadFacing.UNASSIGNED);
    }
}
