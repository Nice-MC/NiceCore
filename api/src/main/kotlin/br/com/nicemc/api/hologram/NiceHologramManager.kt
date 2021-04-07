package br.com.nicemc.api.hologram

import br.com.nicemc.api.Services
import br.com.nicemc.api.plugin.NicePlugin
import br.com.nicemc.api.service.placeholder.PlaceholderService
import com.comphenix.net.sf.cglib.proxy.Factory
import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.ListenerPriority
import com.comphenix.protocol.events.PacketAdapter
import com.comphenix.protocol.events.PacketEvent
import com.comphenix.protocol.wrappers.WrappedChatComponent
import com.comphenix.protocol.wrappers.WrappedWatchableObject
import com.gmail.filoghost.holographicdisplays.HolographicDisplays
import com.gmail.filoghost.holographicdisplays.`object`.line.CraftHologramLine
import com.gmail.filoghost.holographicdisplays.`object`.line.CraftTextLine
import com.gmail.filoghost.holographicdisplays.bridge.protocollib.current.packet.WrapperPlayServerEntityMetadata
import com.gmail.filoghost.holographicdisplays.bridge.protocollib.current.packet.WrapperPlayServerSpawnEntityLiving
import org.bukkit.ChatColor
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import java.util.*

class NiceHologramManager(
    plugin: NicePlugin
) {

    companion object {
        const val CUSTOM_NAME_WATCHER_INDEX = 2
        var niceHologramManager: NiceHologramManager? = null

        fun create(plugin: NicePlugin) {
            if (niceHologramManager == null) {
                niceHologramManager = NiceHologramManager(plugin)
            }
        }
    }

    init {
        val params = PacketAdapter
            .params()
            .plugin(plugin)
            .types(
                PacketType.Play.Server.SPAWN_ENTITY_LIVING,
                PacketType.Play.Server.ENTITY_METADATA
            )
            .serverSide()
            .listenerPriority(ListenerPriority.NORMAL)


        ProtocolLibrary.getProtocolManager().addPacketListener(object : PacketAdapter(params) {
            override fun onPacketSending(event: PacketEvent) {
                val packet = event.packet
                val player = event.player
                if (player is Factory) {
                    return  // Ignore temporary players (reference: https://github.com/dmulloy2/ProtocolLib/issues/349)
                }

                // Spawn entity packet
                if (packet.type === PacketType.Play.Server.SPAWN_ENTITY_LIVING) {
                    var spawnEntityPacket = WrapperPlayServerSpawnEntityLiving(packet)
                    val hologramLine: CraftTextLine =
                        getPlaceholderBuilder(spawnEntityPacket.getEntity(event)) ?: return
                    spawnEntityPacket = WrapperPlayServerSpawnEntityLiving(packet.deepClone())
                    val customNameWatchableObject =
                        spawnEntityPacket.metadata.getWatchableObject(CUSTOM_NAME_WATCHER_INDEX)
                    replaceRelativePlaceholders(customNameWatchableObject, player, hologramLine)
                    event.packet = spawnEntityPacket.handle
                } else if (packet.type === PacketType.Play.Server.ENTITY_METADATA) {
                    var entityMetadataPacket = WrapperPlayServerEntityMetadata(packet)
                    val hologramLine: CraftTextLine =
                        getPlaceholderBuilder(entityMetadataPacket.getEntity(event)) ?: return
                    entityMetadataPacket = WrapperPlayServerEntityMetadata(packet.deepClone())
                    val dataWatcherValues = entityMetadataPacket.entityMetadata
                    for (i in dataWatcherValues.indices) {
                        val watchableObject = dataWatcherValues[i]
                        if (watchableObject.index == CUSTOM_NAME_WATCHER_INDEX) {
                            if (replaceRelativePlaceholders(watchableObject, player, hologramLine)) {
                                event.packet = entityMetadataPacket.handle
                            }

                            // No reason to check further.
                            return
                        }
                    }
                }
            }
        })
    }

    private fun getPlaceholderBuilder(entity: Entity): CraftTextLine? {
        val hologramLine: CraftHologramLine? = getHologramLine(entity)
        return if (hologramLine !is CraftTextLine || hologramLine.getParent() !is NiceHologram) {
            null
        } else hologramLine
    }

    private fun replaceRelativePlaceholders(
        customNameWatchableObject: WrappedWatchableObject?,
        player: Player,
        textLine: CraftTextLine
    ): Boolean {
        if (customNameWatchableObject == null) {
            return true
        }
        val customNameWatchableObjectValue = customNameWatchableObject.value
        if (customNameWatchableObjectValue is Optional<*>) {
            if (!customNameWatchableObjectValue.isPresent) {
                return false
            }
            var customName = if (textLine.text.startsWith("-")) textLine.text.substring(1) else textLine.text
            val placeholder = Services.load(PlaceholderService::class.java)

            customName = placeholder.build(customName, player)
            customName = ChatColor.translateAlternateColorCodes('&', customName)

            customNameWatchableObject.value = Optional.of(
                WrappedChatComponent.fromJson(
                    "{\"extra\":[{\"text\":\"$customName\"}],\"text\":\"\"}"
                ).handle
            )
            return true
        }
        return false
    }

    private fun getHologramLine(bukkitEntity: Entity?): CraftHologramLine? {
        if (bukkitEntity != null && isHologramType(bukkitEntity.type)) {
            val entity = HolographicDisplays.getNMSManager().getNMSEntityBase(bukkitEntity)
            if (entity != null) {
                return entity.hologramLine as CraftHologramLine
            }
        }
        return null
    }

    private fun isHologramType(type: EntityType): Boolean {
        return type == EntityType.ARMOR_STAND || type == EntityType.DROPPED_ITEM || type == EntityType.SLIME
    }

}