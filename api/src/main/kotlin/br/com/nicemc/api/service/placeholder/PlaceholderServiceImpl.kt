package br.com.nicemc.api.service.placeholder

import org.bukkit.entity.Player

class PlaceholderServiceImpl : PlaceholderService {

    private val placeholders: MutableMap<String, (Player) -> String> = mutableMapOf()

    override fun add(tag: String, function: (Player) -> String) {
        placeholders[tag] = function
    }

    override fun build(message: String, player: Player): String {
        placeholders.forEach { (tag, function) ->
            run {
                message.replace("{$tag}", function.invoke(player))
            }
        }

        return message
    }
}