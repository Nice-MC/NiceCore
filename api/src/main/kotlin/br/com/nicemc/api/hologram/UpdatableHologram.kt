package br.com.nicemc.api.hologram

import br.com.nicemc.api.plugin.NicePlugin
import org.bukkit.Location

interface UpdatableHologram {

    fun updateHologram()

    companion object {
        fun create(
            source: Location,
            plugin: NicePlugin,
            lines: List<String>
        ): UpdatableHologram = NiceHologram(source, plugin, lines)
    }

}