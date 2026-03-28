import org.gradle.kotlin.dsl.replace

plugins {
    id("dev.kikugie.stonecutter")
    id("me.modmuss50.mod-publish-plugin") version "0.8.4" apply false
}
stonecutter active "26.1-fabric" /* [SC] DO NOT EDIT */

// See https://stonecutter.kikugie.dev/wiki/config/params
stonecutter parameters {
    swaps["mod_version"] = "\"" + property("mod.version") + "\";"
    swaps["minecraft"] = "\"" + node.metadata.version + "\";"
    constants["release"] = property("mod.id") != "template"
    dependencies["fapi"] = node.project.property("deps.fabric_version") as String

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
