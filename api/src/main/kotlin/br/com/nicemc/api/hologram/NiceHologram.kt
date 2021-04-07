package br.com.nicemc.api.hologram

import br.com.nicemc.api.plugin.NicePlugin
import com.gmail.filoghost.holographicdisplays.`object`.PluginHologram
import com.gmail.filoghost.holographicdisplays.`object`.PluginHologramManager
import com.gmail.filoghost.holographicdisplays.`object`.line.CraftHologramLine
import com.gmail.filoghost.holographicdisplays.`object`.line.CraftTextLine
import com.gmail.filoghost.holographicdisplays.disk.HologramDatabase
import org.bukkit.Location

class NiceHologram(
    location: Location,
    plugin: NicePlugin,
    lines: List<String>
) : PluginHologram(
    location, plugin
), UpdatableHologram {

    init {
        for (line in lines) {
            if (line.toLowerCase().startsWith("icon:")) {
                val newLine: CraftHologramLine = HologramDatabase.readLineFromString(line, this)
                linesUnsafe.add(newLine)
            } else {
                appendTextLine(line)
            }
        }

        // Register to HolographicDisplays
        PluginHologramManager.addHologram(this)
    }

    override fun updateHologram() {
        for (hologramLine in linesUnsafe) {
            if (hologramLine !is CraftTextLine) {
                continue
            }
            val textLine: CraftTextLine = hologramLine
            // Force the hologram to update
            textLine.text = if (textLine.text.startsWith("-")) textLine.text.substring(1) else "-" + textLine.text
        }
    }

}