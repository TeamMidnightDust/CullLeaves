package eu.midnightdust.cullleaves.mixin.sodium;

//? if < 1.21.11 {
/*import eu.midnightdust.cullleaves.config.CullLeavesConfig;
import net.caffeinemc.mods.sodium.client.gui.SodiumGameOptionPages;
import net.caffeinemc.mods.sodium.client.gui.options.OptionFlag;
import net.caffeinemc.mods.sodium.client.gui.options.OptionGroup;
import net.caffeinemc.mods.sodium.client.gui.options.OptionImpact;
import net.caffeinemc.mods.sodium.client.gui.options.OptionImpl;
import net.caffeinemc.mods.sodium.client.gui.options.control.TickBoxControl;
import net.caffeinemc.mods.sodium.client.gui.options.storage.SodiumOptionsStorage;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;

@Mixin(value = SodiumGameOptionPages.class, remap = false)
public class MixinSodiumGameOptionPages {

    @Shadow @Final private static SodiumOptionsStorage sodiumOpts;

    @ModifyVariable(method = "performance", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableList;copyOf(Ljava/util/Collection;)Lcom/google/common/collect/ImmutableList;"))
    private static List<OptionGroup> cullleaves$addCullLeavesOption(List<OptionGroup> groups) {
        groups.add(OptionGroup.createBuilder()
                .add(OptionImpl.createBuilder(boolean.class, sodiumOpts)
                        .setName(Component.translatable("cullleaves.midnightconfig.enabled"))
                        .setTooltip(Component.translatable("cullleaves.midnightconfig.enabled.tooltip"))
                        .setControl(TickBoxControl::new)
                        .setBinding((opts, value) -> {
                            CullLeavesConfig.enabled = value;
                            CullLeavesConfig.write("cullleaves");
                        }, opts -> CullLeavesConfig.enabled)
                        .setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD)
                        .setImpact(OptionImpact.MEDIUM)
                        .build()
                ).add(OptionImpl.createBuilder(boolean.class, sodiumOpts)
                        .setName(Component.translatable("cullleaves.midnightconfig.cullRoots"))
                        .setTooltip(Component.translatable("cullleaves.midnightconfig.cullRoots.tooltip"))
                        .setControl(TickBoxControl::new)
                        .setBinding((opts, value) -> {
                            CullLeavesConfig.cullRoots = value;
                            CullLeavesConfig.write("cullleaves");
                        }, opts -> CullLeavesConfig.cullRoots)
                        .setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD)
                        .setImpact(OptionImpact.MEDIUM)
                        .build()
                )
                .build()
        );

        return groups;
    }
}
*///?} else {
import eu.midnightdust.core.MidnightLib;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = MidnightLib.class)
public class MixinSodiumGameOptionPages {
}
//?}