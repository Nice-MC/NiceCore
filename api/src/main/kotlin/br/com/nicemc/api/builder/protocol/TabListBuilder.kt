package br.com.nicemc.api.builder.protocol

import br.com.nicemc.api.Services
import br.com.nicemc.api.plugin.NicePlugin
import br.com.nicemc.api.protocol.packets.PacketPlayerListHeaderFooter
import br.com.nicemc.api.service.PlayerService
import com.comphenix.protocol.wrappers.WrappedChatComponent
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import java.util.function.Consumer

class TabListBuilder {

    private val packetPlayerListHeaderFooter: PacketPlayerListHeaderFooter = PacketPlayerListHeaderFooter()
    var header: String = ""
    var footer: String = ""

    fun with(consumer: Consumer<TabListBuilder?>): TabListBuilder? {
        consumer.accept(this)
        return this
    }

    /**
     * Send tab list to a player, verifiy if exists header or footer
     *
     * @param player player to set the tab list
     * @return {@link TabListBuilder}
     */
    fun apply(player: Player): TabListBuilder {
        if (header.isNotEmpty()) {
            packetPlayerListHeaderFooter.setHeader(WrappedChatComponent.fromText(header))
        }
        if (footer.isNotEmpty()) {
            packetPlayerListHeaderFooter.setFooter(WrappedChatComponent.fromText(footer))
        }
        packetPlayerListHeaderFooter.sendPacket(player)
        return this
    }

    /**
     * Send tab list to all online players
     *
     * @return {@link TabListBuilder}
     */
    fun apply(): TabListBuilder {
        Services.load(PlayerService::class.java).forEach { apply() }
        return this
    }

    /**
     * Update tablist
     *
     * @param player player to set the tab list
     * @param basePlugin your main class instance {@link network.zentry.commons.plugin.ZentryPlugin}
     * @param delay delay
     * @param period period
     */
    fun update(player: Player, plugin: NicePlugin, delay: Long, period: Long) {
        val tabListUpdater = TabListUpdater(this, player)
        tabListUpdater.runTaskTimer(plugin, delay, period)
    }

    private class TabListUpdater(private val builder: TabListBuilder, private val player: Player) : BukkitRunnable() {
        override fun run() {
            builder.apply(player)
        }
    }
}