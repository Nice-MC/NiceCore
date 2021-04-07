package br.com.nicemc.api.service.placeholder

import org.bukkit.entity.Player

interface PlaceholderService {

    fun add(tag: String, function: (Player) -> String)

    fun build(message: String, player: Player): String

}