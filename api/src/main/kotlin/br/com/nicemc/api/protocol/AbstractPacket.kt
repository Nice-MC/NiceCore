package br.com.nicemc.api.protocol

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.PacketContainer
import org.bukkit.entity.Player
import java.lang.reflect.InvocationTargetException

/**
 * Constructs a new wrapper for the given packet.
 *
 * @param handle - handle to the raw packet models.
 * @param type   - the packet type.
 */
abstract class AbstractPacket(
    val handle: PacketContainer,
    type: PacketType
) {

    init {
        require(handle.type == type)
    }

    /**
     * Send the current packet to the given receiver.
     *
     * @param receiver - the receiver.
     * @throws RuntimeException If the packet cannot be sent.
     */
    fun sendPacket(receiver: Player) = try {
        ProtocolLibrary.getProtocolManager().sendServerPacket(receiver, handle)
    } catch (e: InvocationTargetException) {
        throw RuntimeException("Cannot send packet.", e)
    }

    /**
     * Send the current packet to all online players.
     */
    fun broadcastPacket() = ProtocolLibrary.getProtocolManager().broadcastServerPacket(handle)

    /**
     * Simulate receiving the current packet from the given sender.
     *
     * @param sender - the sender.
     * @throws RuntimeException if the packet cannot be received.
     */
    fun receivePacket(sender: Player) = try {
        ProtocolLibrary.getProtocolManager().recieveClientPacket(sender, handle)
    } catch (e: Exception) {
        throw RuntimeException("Cannot receive packet.", e)
    }
}