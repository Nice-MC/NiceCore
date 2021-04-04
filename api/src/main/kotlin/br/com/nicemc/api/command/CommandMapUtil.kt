package br.com.nicemc.api.command

import com.google.common.base.Preconditions
import org.bukkit.Bukkit
import org.bukkit.command.*
import org.bukkit.command.Command
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.SimplePluginManager
import java.lang.reflect.Constructor
import java.lang.reflect.Field

class CommandMapUtil {

    private var commandConstructor: Constructor<PluginCommand>
    private var commandMapField: Field
    private var knownCommandsField: Field

    init {
        val commandConstructor: Constructor<PluginCommand>
        try {
            commandConstructor =
                PluginCommand::class.java.getDeclaredConstructor(String::class.java, Plugin::class.java)
            commandConstructor.setAccessible(true)
        } catch (e: NoSuchMethodException) {
            throw RuntimeException(e)
        }
        this.commandConstructor = commandConstructor

        val commandMapField: Field
        try {
            commandMapField = SimplePluginManager::class.java.getDeclaredField("commandMap")
            commandMapField.isAccessible = true
        } catch (e: NoSuchFieldException) {
            throw RuntimeException(e)
        }
        this.commandMapField = commandMapField

        val knownCommandsField: Field
        try {
            knownCommandsField = SimpleCommandMap::class.java.getDeclaredField("knownCommands")
            knownCommandsField.isAccessible = true
        } catch (e: NoSuchFieldException) {
            throw RuntimeException(e)
        }
        this.knownCommandsField = knownCommandsField
    }

    private fun getCommandMap(): CommandMap {
        return try {
            commandMapField.get(Bukkit.getServer().pluginManager) as CommandMap
        } catch (e: Exception) {
            throw RuntimeException("Could not get CommandMap", e)
        }
    }

    private fun getKnownCommandMap(): MutableMap<String, Command> {
        return try {
            knownCommandsField.get(getCommandMap()) as MutableMap<String, Command>
        } catch (e: java.lang.Exception) {
            throw RuntimeException("Could not get known commands map", e)
        }
    }

    fun <T : CommandExecutor> registerCommand(
        plugin: Plugin,
        command: T,
        vararg aliases: String
    ): T {
        Preconditions.checkArgument(aliases.isNotEmpty(), "No aliases")
        for (alias in aliases) {
            try {
                val cmd: PluginCommand = commandConstructor.newInstance(alias, plugin)
                getCommandMap().register(plugin.description.name, cmd)
                getKnownCommandMap()[plugin.description.name.toLowerCase() + ":" + alias.toLowerCase()] = cmd
                getKnownCommandMap()[alias.toLowerCase()] = cmd
                cmd.label = alias.toLowerCase()
                cmd.executor = command
                if (command is TabCompleter) {
                    cmd.tabCompleter = command
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return command
    }

    fun <T : CommandExecutor> unregisterCommand(command: T): T {
        val map: CommandMap = getCommandMap()
        try {
            val knownCommands = knownCommandsField.get(map) as MutableMap<String, Command>
            val iterator = knownCommands.values.iterator()
            while (iterator.hasNext()) {
                val cmd = iterator.next()
                if (cmd is PluginCommand) {
                    val executor = cmd.executor
                    if (command === executor) {
                        cmd.unregister(map)
                        iterator.remove()
                    }
                }
            }
        } catch (e: Exception) {
            throw RuntimeException("Could not unregister command", e)
        }
        return command
    }
}