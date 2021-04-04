package br.com.nicemc.api.plugin

import br.com.nicemc.api.command.AbstractCommand
import br.com.nicemc.api.command.context.CommandContext
import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.ServicePriority
import java.util.*

interface BasePlugin : Plugin {

    /**
     * @param listener to register
     * @return listener registered
     */
    fun <T : Listener> registerListener(listener: T): T

    /**
     *
     * @param listeners to register
     * @return list of listeners registered
     */
    fun <T : Listener> registerListeners(listeners: Array<T>): Array<T>

    /**
     * Gets a service provided by the ServiceManager
     *
     * @param clazz the service class
     * @param <T> the class type
     * @return the optional service
     */
    fun <T> getService(clazz: Class<T>): Optional<T>

    /**
     * Gets a service provided by the ServiceManager
     *
     * @param clazz the service class
     * @param <T> the class type
     * @return the service
     */
    fun <T> loadService(clazz: Class<T>): T

    /**
     * Provides a service to the ServiceManager, bound to this plugin
     *
     * @param clazz the service class
     * @param instance the instance
     * @param priority the priority to add the service at
     * @param <T> the service class type
     * @return the instance
     */
    fun <T> provideService(clazz: Class<T>, instance: T, priority: ServicePriority): T

    /**
     * Provides a service to the ServiceManager, bound to this plugin at {@link ServicePriority#Normal}.
     *
     * @param clazz the service class
     * @param instance the instance
     * @param <T> the service class type
     * @return the instance
     */
    fun <T> provideService(clazz: Class<T>, instance: T): T

    /**
     * Creates the AbstractCommand for creating the command.
     * Use .register(commandName) after
     *
     * @param function the the function performed by the command.
     * @return the AbstractCommand
     */
    fun registerCommand(function: ((CommandContext) -> Boolean)): AbstractCommand

}