package eu.midnightdust.cullleaves.mixin.sodium;

import net.caffeinemc.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderer;
import eu.midnightdust.cullleaves.CullLeavesClient;
import net.minecraft.world.level.block.LeavesBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//? if >= 1.21.4 {
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
//?} else if >= 1.21 {
/*import net.minecraft.core.BlockPos;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.level.block.state.BlockState;
*///?} else {
/*import net.caffeinemc.mods.sodium.client.render.chunk.compile.ChunkBuildBuffers;
import net.caffeinemc.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderContext;
*///?}

//? if fabric && >= 1.21 {
/*? if < 1.21.11 {*/ /*import net.caffeinemc.mods.sodium.client.render.frapi.render.AbstractBlockRenderContext *//*?} else {*/ import net.caffeinemc.mods.sodium.client.render.model.AbstractBlockRenderContext; /*?}*/;
//?} else {
/*import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Unique;
*///?}

@Mixin(BlockRenderer.class)
public abstract class MixinBlockRenderer /*? if fabric && >= 1.21 {*/ extends AbstractBlockRenderContext /*?}*/ {
    //? if neoforge || < 1.21 {
    /*@Unique
    private static final Minecraft cullleaves$client = Minecraft.getInstance();
    *///?}

    @Inject(at = @At("HEAD"), method = "renderModel", cancellable = true /*? if forge {*//*, remap = false *//*?}*/)
    //? if >= 1.21.4 {
    public void cullleaves$cancelRendering(BlockStateModel model, BlockState state, BlockPos pos, BlockPos origin, CallbackInfo ci) {
    //?} else if >= 1.21 {
    /*public void cullleaves$cancelRendering(BakedModel model, BlockState state, BlockPos pos, BlockPos origin, CallbackInfo ci) {
    *///?} else {
    /*public void cullleaves$cancelRendering(BlockRenderContext ctx, ChunkBuildBuffers buffers, CallbackInfo ci) {
        var state = ctx.state();
        var pos = ctx.pos();
    *///?}
        if (state.getBlock() instanceof LeavesBlock && CullLeavesClient.shouldHideBlock(/*? if fabric && >= 1.21 {*/ this.level /*?} else {*/ /*cullleaves$client.level *//*?}*/, pos))
            ci.cancel();
    }
}