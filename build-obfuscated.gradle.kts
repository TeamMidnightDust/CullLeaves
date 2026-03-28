plugins {
    id("dev.architectury.loom") version "1.13-SNAPSHOT"
    id("me.modmuss50.mod-publish-plugin")
    `maven-publish`
}

val minecraft = stonecutter.current.version
val loader = loom.platform.get().name.lowercase()

version = "${mod.version}+$minecraft"
group = mod.group
base {
    archivesName.set("${mod.id}-$loader")
}

repositories {
    maven("https://maven.neoforged.net/releases/")
    maven("https://api.modrinth.com/maven")

    // modmenu
    maven("https://maven.terraformersmc.com/")
    maven("https://maven.nucleoid.xyz/")

    // MidnightLib
    maven("https://maven.midnightdust.eu/releases/")

    // Sodium
    maven("https://maven.caffeinemc.net/releases")
}
dependencies {
    minecraft("com.mojang:minecraft:$minecraft")

    // MidnightLib
    val midnightlib = "eu.midnightdust:midnightlib:${mod.dep("midnightlib_version")}+${minecraft}-${loader}"
    modImplementation(midnightlib)
    include(midnightlib)

    if (loader == "fabric") {
        modImplementation("net.fabricmc:fabric-loader:${mod.dep("fabric_loader")}")
        modImplementation("net.fabricmc.fabric-api:fabric-api:${mod.dep("fabric_version")}")

        modCompileOnly("maven.modrinth:sodium:${mod.dep("sodium_version")}-fabric")
    }
    if (loader == "forge") {
        "forge"("net.minecraftforge:forge:${minecraft}-${mod.dep("forge_loader")}")

        modCompileOnly("maven.modrinth:xenon-forge:0.3.31")
    }
    if (loader == "neoforge") {
        "neoForge"("net.neoforged:neoforge:${mod.dep("neoforge_loader")}")

        if (minecraft == "1.21.11")
            modCompileOnly("net.caffeinemc:sodium-neoforge-mod:0.8.0+mc1.21.11")
        else
            modCompileOnly("maven.modrinth:sodium:${mod.dep("sodium_version")}-neoforge")
    }
    mappings (loom.officialMojangMappings())
}

loom {
    //accessWidenerPath = rootProject.file("src/main/resources/template.accesswidener")

    if (loader == "forge") {
        forge.mixinConfigs("cullleaves.mixins.json", "cullleaves-neoforge.mixins.json")
    }
}

publishMods {
    val modrinthToken = System.getenv("MODRINTH_TOKEN")
    val curseforgeToken = System.getenv("CURSEFORGE_TOKEN")
    val githubToken = System.getenv("GITHUB_TOKEN").orEmpty()

    file = project.tasks.remapJar.get().archiveFile
    dryRun = modrinthToken == null || curseforgeToken == null

    displayName = "${mod.name} ${mod.version} - ${loader.replaceFirstChar { it.uppercase() }} ${property("mod.mc_title")}"
    version = "${mod.version}+${property("mod.mc_title")}-${loader}"
    changelog = rootProject.file("CHANGELOG.md").readText()
    type = STABLE

    modLoaders.add(loader)
    if (loader == "fabric") {
        modLoaders.add("quilt")
    }

    val targets = property("mod.mc_targets").toString().split(' ')
    modrinth {
        projectId = property("publish.modrinth").toString()
        accessToken = modrinthToken
        targets.forEach(minecraftVersions::add)
        requires("midnightlib")
        if (loader == "fabric") {
            requires("fabric-api")
        }
    }

    curseforge {
        projectId = property("publish.curseforge").toString()
        accessToken = curseforgeToken.toString()
        targets.forEach(minecraftVersions::add)
        requires("midnightlib")
        if (loader == "fabric") {
            requires("fabric-api")
        }
    }

//    github {
//        accessToken = githubToken
//        repository = "TeamMidnightDust/CullLeaves"
//        commitish = "multiversion" // This is the branch the release tag will be created from
//
//        tagName = "v" + properties["mod.version"]
//
//        // Allow the release to be initially created without any files.
//        allowEmptyFiles = true
//    }
}
publishing {
    repositories {
        maven {
            name = "MidnightDust"
            url = uri("https://maven.midnightdust.eu/releases")
            credentials(PasswordCredentials::class)
        }
    }
    publications {
        create<MavenPublication>("mavenJava") {
            pom {
                groupId = "eu.midnightdust"
                artifactId = project.mod.id
                version = "${project.version}-${loader}"

                from(components["java"])
            }
        }
    }
}


java {
    withSourcesJar()
    val java = if (stonecutter.eval(minecraft, ">=1.20.5")) JavaVersion.VERSION_21 else JavaVersion.VERSION_17
    targetCompatibility = java
    sourceCompatibility = java
}

tasks.remapJar {
    injectAccessWidener = true
    inputs.file(tasks.jar.get().archiveFile)
    archiveClassifier = null
    dependsOn(tasks.jar)
}

tasks.jar {
    archiveClassifier = "dev"
}

val buildAndCollect = tasks.register<Copy>("buildAndCollect") {
    group = "build"
    from(tasks.remapJar.get().archiveFile, tasks.remapSourcesJar.get().archiveFile)
    into(rootProject.layout.buildDirectory.file("libs/${mod.version}/$loader"))
    dependsOn("build")
}

if (stonecutter.current.isActive) {
    rootProject.tasks.register("buildActive") {
        group = "project"
        dependsOn(buildAndCollect)
    }

    rootProject.tasks.register("runActive") {
        group = "project"
        dependsOn(tasks.named("runClient"))
    }
}

tasks.processResources {
    properties(
        listOf("fabric.mod.json"),
        "id" to mod.id,
        "name" to mod.name,
        "version" to mod.version,
        "minecraft" to mod.prop("mc_dep_fabric")
    )
    properties(
        listOf("META-INF/mods.toml", "pack.mcmeta"),
        "id" to mod.id,
        "name" to mod.name,
        "version" to mod.version,
        "minecraft" to mod.prop("mc_dep_forgelike")
    )
    properties(
        listOf("META-INF/neoforge.mods.toml", "pack.mcmeta"),
        "id" to mod.id,
        "name" to mod.name,
        "version" to mod.version,
        "minecraft" to mod.prop("mc_dep_forgelike")
    )
}

tasks.build {
    group = "versioned"
    description = "Must run through 'chiseledBuild'"
}


stonecutter {
    constants {
        arrayOf("fabric", "neoforge", "forge").forEach { it -> put(it, loader == it) }
    }
}
