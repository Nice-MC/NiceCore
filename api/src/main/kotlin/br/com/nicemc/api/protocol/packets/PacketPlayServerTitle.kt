package br.com.nicemc.api.protocol.packets

import br.com.nicemc.api.protocol.AbstractPacket
import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.wrappers.EnumWrappers
import com.comphenix.protocol.wrappers.WrappedChatComponent

class PacketPlayServerTitle : AbstractPacket(
    PacketContainer(PacketType.Play.Server.TITLE), PacketType.Play.Server.TITLE
) {

    init {
        handle.modifier.writeDefaults()
    }

    /**
     * Retrieve Action.
     *
     * @return The current Action
     */
    fun getAction(): EnumWrappers.TitleAction = handle.titleActions.read(0)

    /**
     * Set Action.
     *
     * @param value - new value.
     */
    fun setAction(value: EnumWrappers.TitleAction) = handle.titleActions.write(0, value)

    /**
     * Retrieve 0 (TITLE).
     *
     *
     * Notes: chat
     *
     * @return The current 0 (TITLE)
     */
    fun getTitle(): WrappedChatComponent = handle.chatComponents.read(0)

    /**
     * Set 0 (TITLE).
     *
     * @param value - new value.
     */
    fun setTitle(value: WrappedChatComponent) = handle.chatComponents.write(0, value)

    /**
     * Retrieve 2 (TIMES).
     *
     *
     * Notes: int
     *
     * @return The current 2 (TIMES)
     */
    fun getFadeIn(): Int = handle.integers.read(0)

    /**
     * Set 2 (TIMES).
     *
     * @param value - new value.
     */
    fun setFadeIn(value: Int) = handle.integers.write(0, value)

    /**
     * Retrieve Stay.
     *
     * @return The current Stay
     */
    fun getStay(): Int = handle.integers.read(1)

    /**
     * Set Stay.
     *
     * @param value - new value.
     */
    fun setStay(value: Int) = handle.integers.write(1, value)

    /**
     * Retrieve Fade Out.
     *
     * @return The current Fade Out
     */
    fun getFadeOut(): Int = handle.integers.read(2)

    /**
     * Set Fade Out.
     *
     * @param value - new value.
     */
    fun setFadeOut(value: Int) = handle.integers.write(2, value)

}