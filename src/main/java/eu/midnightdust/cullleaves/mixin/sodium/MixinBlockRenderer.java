package eu.midnightdust.cullleaves.mixin.sodium;

import eu.midnightdust.cullleaves.CullLeavesClient;
import net.caffeinemc.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderer;
import net.caffeinemc.mods.sodium.client.render.frapi.render.AbstractBlockRenderContext;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//? if >= 1.21.4 {
import net.minecraft.client.renderer.block.model.BlockStateModel;
//?} else {
/*import net.minecraft.client.resources.model.BakedModel;
*///?}

//? if fabric {
//?} else {
/*import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Unique;
*///?}


@Mixin(BlockRenderer.class)
public abstract class MixinBlockRenderer /*? if fabric {*/ extends AbstractBlockRenderContext /*?}*/ {
    //? if neoforge {
    /*@Unique
    private static final Minecraft cullleaves$client = Minecraft.getInstance();
    *///?}

    @Inject(at = @At("HEAD"), method = "renderModel", cancellable = true)
    //? if >= 1.21.4 {
    public void cullleaves$cancelRendering(BlockStateModel model, BlockState state, BlockPos pos, BlockPos origin, CallbackInfo ci) {
    //?} else {
    /*public void cullleaves$cancelRendering(BakedModel model, BlockState state, BlockPos pos, BlockPos origin, CallbackInfo ci) {
    *///?}
        if (state.getBlock() instanceof LeavesBlock && CullLeavesClient.shouldHideBlock(/*? if fabric {*/ this.level /*?} else {*/ /*cullleaves$client.level *//*?}*/, pos))
            ci.cancel();
    }
}