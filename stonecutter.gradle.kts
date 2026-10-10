import org.gradle.kotlin.dsl.replace

plugins {
    id("dev.kikugie.stonecutter")
    id("me.modmuss50.mod-publish-plugin") version "2.2.0" apply false
}
stonecutter active "26.3-fabric" /* [SC] DO NOT EDIT */

// See https://stonecutter.kikugie.dev/wiki/config/params
stonecutter parameters {
    val (version, loader) = current.project.split('-', limit = 2)

    // Makes version- and loader-specific properties apply from `stoncutter.properties.toml`
    properties {
        tags(version, loader)
    }

    // Adds constants to Stonecutter comments (i.e. for `//? if fabric {...`)
    constants {
        match(loader, "fabric", "neoforge", "forge")
    }

    swaps["mod_version"] = "\"${properties.get<String>("mod.version")}\";"
    swaps["minecraft"] = "\"${node.metadata.version}\";"
    constants["release"] = properties.get<String>("mod.id") != "template"
    dependencies["fapi"] = properties.getOrNull<String>("deps.fabric_api") ?: "0"


    replacements {
        string {
            direction = eval(current.version, ">=1.21.11")
            replace("ResourceLocation", "Identifier")
        }
        string {
            direction = eval(current.version, ">=1.21")
            replace("new ResourceLocation", "ResourceLocation.fromNamespaceAndPath")
        }
        string {
            direction = eval(current.version, ">=1.21")
            replace("me.jellysquid.mods.sodium", "net.caffeinemc.mods.sodium")
        }
        string {
            direction = eval(current.version, ">=26.1")
            replace("net.minecraft.client.renderer.block.model.BlockStateModel", "net.minecraft.client.renderer.block.dispatch.BlockStateModel")
        }
        string {
            direction = eval(current.version, ">=26.1")
            replace("net.minecraft.world.level.BlockAndTintGetter", "net.minecraft.client.renderer.block.BlockAndTintGetter")
        }
    }
}
