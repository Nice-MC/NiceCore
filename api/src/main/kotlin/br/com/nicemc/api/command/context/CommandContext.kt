package br.com.nicemc.api.command.context

import br.com.nicemc.api.Services
import br.com.nicemc.api.service.PlayerService
import com.google.common.collect.ImmutableList
import org.bukkit.command.CommandSender

interface CommandContext {

    fun sender(): CommandSender

    fun msg(vararg messages: String) = Services.load(PlayerService::class.java).msg(sender(), *messages)

    fun args(): ImmutableList<String>

    fun rawArg(index: Int): String?

    fun label(): String

}