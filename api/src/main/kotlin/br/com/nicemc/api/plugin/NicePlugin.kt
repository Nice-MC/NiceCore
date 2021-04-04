package br.com.nicemc.api.plugin

import br.com.nicemc.api.Services
import br.com.nicemc.api.command.AbstractCommand
import br.com.nicemc.api.command.Commands
import br.com.nicemc.api.command.context.CommandContext
import org.bukkit.event.Listener
import org.bukkit.plugin.ServicePriority
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

abstract class NicePlugin : JavaPlugin(), BasePlugin {

    override fun onLoad() {
        load()
    }

    override fun onEnable() {
        enable()
    }

    override fun onDisable() {
        disable()
    }

    open fun load() {
    }

    open fun enable() {
    }

    open fun disable() {
    }

    override fun <T : Listener> registerListener(listener: T): T {
        server.pluginManager.registerEvents(listener, this)
        return listener
    }

    override fun <T : Listener> registerListeners(listeners: Array<T>): Array<T> {
        listeners.forEach { listener -> registerListener(listener) }
        return listeners
    }

    override fun <T> getService(clazz: Class<T>): Optional<T> =
        Services.get(clazz)


    override fun <T> loadService(clazz: Class<T>): T =
        Services.load(clazz)

    override fun <T> provideService(clazz: Class<T>, instance: T, priority: ServicePriority): T {
        Services.provide(clazz, instance, this, priority)
        return instance
    }

    override fun <T> provideService(clazz: Class<T>, instance: T): T {
        Services.provide(clazz, this, instance)
        return instance
    }

    override fun registerCommand(function: (CommandContext) -> Boolean): AbstractCommand =
        Commands.create(function, this)

}