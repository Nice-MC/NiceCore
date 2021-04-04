package br.com.nicemc.api.command

import br.com.nicemc.api.command.context.CommandContext
import org.bukkit.plugin.Plugin

object Commands {

    fun create(function: (CommandContext) -> Boolean, plugin: Plugin): AbstractCommand =
        AbstractCommand(function, plugin)

}