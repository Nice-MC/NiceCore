package br.com.nicemc.api.command

import br.com.nicemc.api.command.context.CommandContext
import br.com.nicemc.api.command.context.ImmutableCommandContext
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.plugin.Plugin

class AbstractCommand(
    val function: (CommandContext) -> Boolean,
    val plugin: Plugin
) : CommandExecutor {

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean = try {
        function(ImmutableCommandContext(sender, label, args))
    } catch (e: CommandInterruptException) {
        e.action.accept(sender)
        false
    }

    fun register(vararg aliases: String) {
        CommandMapUtil().registerCommand(plugin, this, *aliases)
    }
}