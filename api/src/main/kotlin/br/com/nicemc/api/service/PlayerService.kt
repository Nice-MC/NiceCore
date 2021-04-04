package br.com.nicemc.api.service

import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*
import java.util.function.Consumer
import java.util.stream.Stream

interface PlayerService {

    /**
     * Gets a player by username
     *
     * @param userName the player username
     * @return an optional player
     */
    fun get(userName: String): Optional<Player>

    /**
     * Gets a player by unique id
     *
     * @param uuid the player unique id
     * @return an optional player
     */
    fun get(uuid: UUID): Optional<Player>

    /**
     * Gets all players on the server
     *
     * @return all players on the server
     */
    fun all(): Collection<Player>

    /**
     * Apply an action to all players on the server
     *
     * @consumer consumer the action to apply
     */
    fun forEach(consumer: Consumer<Player>)

    /**
     * Gets a stream clickable all online players
     *
     * @return a stream clickable players
     */
    fun streamAll(): Stream<Player>

    /**
     * Messages a target a set messages
     *
     * @param target the sender
     * @param messages the message to send
     */
    fun msg(target: CommandSender, vararg messages: String)

    /**
     * Gets a Offline Player by unique id
     *
     * @param uuid the offline player unique id
     * @return an optional offline player
     */
    fun getOffline(uuid: UUID): Optional<OfflinePlayer>

    /**
     * Gets a Offline Player by unique id
     *
     * @param name the offline player name
     * @return an optional offline player
     */
    fun getOffline(name: String): Optional<OfflinePlayer>

}