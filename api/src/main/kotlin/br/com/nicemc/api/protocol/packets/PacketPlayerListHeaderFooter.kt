package br.com.nicemc.api.protocol.packets

import br.com.nicemc.api.protocol.AbstractPacket
import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.wrappers.WrappedChatComponent

class PacketPlayerListHeaderFooter : AbstractPacket(
    PacketContainer(PacketType.Play.Server.PLAYER_LIST_HEADER_FOOTER), PacketType.Play.Server.PLAYER_LIST_HEADER_FOOTER
) {

    init {
        handle.modifier.writeDefaults()
    }

    /**
     * Retrieve Header.
     *
     * @return The current Header
     */
    fun getHeader(): WrappedChatComponent = handle.chatComponents.read(0)

    /**
     * Set Header.
     *
     * @param value - new value.
     */
    fun setHeader(value: WrappedChatComponent) = handle.chatComponents.write(0, value)

    /**
     * Retrieve Footer.
     *
     * @return The current Footer
     */
    fun getFooter(): WrappedChatComponent = handle.chatComponents.read(1)

    /**
     * Set Footer.
     *
     * @param value - new value.
     */
    fun setFooter(value: WrappedChatComponent) = handle.chatComponents.write(1, value)

}