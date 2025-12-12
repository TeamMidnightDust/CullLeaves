package eu.midnightdust.cullleaves.config;

//? if >= 1.21.11 {
import net.caffeinemc.mods.sodium.api.config.ConfigEntryPoint;
import net.caffeinemc.mods.sodium.api.config.option.OptionFlag;
import net.caffeinemc.mods.sodium.api.config.option.OptionImpact;
import net.caffeinemc.mods.sodium.api.config.structure.ConfigBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import static eu.midnightdust.cullleaves.CullLeavesClient.MOD_ID;

public class SodiumConfigEntryPoint implements ConfigEntryPoint {
    @Override
    public void registerConfigLate(ConfigBuilder builder) {
        builder.registerOwnModOptions().setName("Cull Leaves").setColorTheme(builder.createColorTheme().setBaseThemeRGB(0xFF55AA55))
                .addPage(builder.createOptionPage().setName(Component.translatable("sodium.options.pages.performance")).addOptionGroup(builder.createOptionGroup()
                .addOption(
                        builder.createBooleanOption(Identifier.fromNamespaceAndPath(MOD_ID, "enabled"))
                                .setDefaultValue(true)
                                .setName(Component.translatable("cullleaves.midnightconfig.enabled"))
                                .setTooltip(Component.translatable("cullleaves.midnightconfig.enabled.tooltip"))
                                .setBinding(value -> CullLeavesConfig.enabled = value,
                                        () -> CullLeavesConfig.enabled)
                                .setStorageHandler(() -> CullLeavesConfig.write(MOD_ID))
                                .setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD)
                                .setImpact(OptionImpact.MEDIUM)
                )
                .addOption(
                        builder.createBooleanOption(Identifier.fromNamespaceAndPath(MOD_ID, "cullRoots"))
                                .setDefaultValue(true)
                                .setName(Component.translatable("cullleaves.midnightconfig.cullRoots"))
                                .setTooltip(Component.translatable("cullleaves.midnightconfig.cullRoots.tooltip"))
                                .setBinding(value -> CullLeavesConfig.cullRoots = value,
                                        () -> CullLeavesConfig.cullRoots)
                                .setStorageHandler(() -> CullLeavesConfig.write(MOD_ID))
                                .setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD)
                                .setImpact(OptionImpact.MEDIUM)
                )
        ));
    }
}
//?}