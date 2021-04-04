package br.com.nicemc.api

import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.ServicePriority
import java.util.*

object Services {

    fun <T> load(clazz: Class<T>): T {
        Objects.requireNonNull(clazz, "clazz")
        return get(clazz).orElseThrow {
            IllegalStateException(
                "No registration present for service '" + clazz.name + "'"
            )
        }
    }

    fun <T> get(clazz: Class<T>): Optional<T> {
        Objects.requireNonNull(clazz, "clazz")
        val registration = Bukkit.getServicesManager().getRegistration(clazz) ?: return Optional.empty()
        return Optional.ofNullable(registration.provider)
    }

    fun <T> provide(clazz: Class<T>, plugin: Plugin, instance: T): T {
        return provide(clazz, instance, plugin, ServicePriority.Normal)
    }

    fun <T> provide(
        clazz: Class<T>,
        instance: T,
        plugin: Plugin,
        priority: ServicePriority
    ): T {
        Objects.requireNonNull(clazz, "clazz")
        Objects.requireNonNull(instance, "instance")
        Objects.requireNonNull(plugin, "plugin")
        Objects.requireNonNull(priority, "priority")
        Bukkit.getServicesManager().register(clazz, instance, plugin, priority)
        return instance
    }

}