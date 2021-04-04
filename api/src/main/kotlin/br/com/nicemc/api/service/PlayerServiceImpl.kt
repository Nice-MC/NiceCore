package br.com.nicemc.api.service

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*
import java.util.function.Consumer
import java.util.stream.Stream

class PlayerServiceImpl : PlayerService {

    override fun get(userName: String): Optional<Player> = Optional.ofNullable(Bukkit.getPlayer(userName))

    override fun get(uuid: UUID): Optional<Player> = Optional.ofNullable(Bukkit.getPlayer(uuid))

    override fun all(): Collection<Player> = Bukkit.getOnlinePlayers()

    override fun forEach(consumer: Consumer<Player>) {
        all().forEach(consumer)
    }

    override fun streamAll(): Stream<Player> = all().stream()

    override fun msg(target: CommandSender, vararg messages: String) {
        messages.forEach { s -> target.sendMessage(ChatColor.translateAlternateColorCodes('&', s)) }
    }

    override fun getOffline(uuid: UUID): Optional<OfflinePlayer> = Optional.ofNullable(Bukkit.getOfflinePlayer(uuid))

    override fun getOffline(name: String): Optional<OfflinePlayer> = Optional.ofNullable(Bukkit.getOfflinePlayer(name))
}