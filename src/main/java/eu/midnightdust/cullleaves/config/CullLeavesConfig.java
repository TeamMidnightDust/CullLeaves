package eu.midnightdust.cullleaves.config;

import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;
import me.sargunvohra.mcmods.autoconfig1u.shadowed.blue.endless.jankson.Comment;
import net.minecraft.client.render.entity.PlayerModelPart;

@Config(name = "cullleaves")
public class CullLeavesConfig implements ConfigData {

    @ConfigEntry.Gui.PrefixText //  Enable/Disable the mod. Requires Chunk Reload (F3 + A).
    public boolean enabled = true;
}
