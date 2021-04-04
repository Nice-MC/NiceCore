package br.com.nicemc.api.builder.protocol

import br.com.nicemc.api.Services
import br.com.nicemc.api.protocol.packets.PacketPlayServerTitle
import br.com.nicemc.api.service.PlayerService
import com.comphenix.protocol.wrappers.EnumWrappers
import com.comphenix.protocol.wrappers.WrappedChatComponent
import org.bukkit.entity.Player
import java.util.function.Consumer

class TitleBuilder(
    var titleType: TitleType,
    var title: String
) {

    private val playServerTitle: PacketPlayServerTitle = PacketPlayServerTitle()

    var fadeIn = 0
    var fadeOut = 0
    var stay = 0

    fun with(builderConsumer: Consumer<TitleBuilder>): TitleBuilder {
        builderConsumer.accept(this)
        return this
    }

    /**
     * Send title to player
     *
     * @param player player to diplay the title
     * @return [TitleBuilder]
     */
    fun apply(player: Player): TitleBuilder {
        playServerTitle.setAction(EnumWrappers.TitleAction.valueOf(titleType.name))
        playServerTitle.setFadeIn(fadeIn)
        playServerTitle.setFadeOut(fadeOut)
        playServerTitle.setStay(stay)
        playServerTitle.setTitle(WrappedChatComponent.fromText(title))
        playServerTitle.sendPacket(player)
        return this
    }

    /**
     * Send title to all online players
     *
     * @return [TitleBuilder]
     */
    fun apply(): TitleBuilder {
        Services.load(PlayerService::class.java).forEach(this::apply)
        return this
    }

    enum class TitleType {
        TITLE, SUBTITLE, ACTIONBAR
    }
}