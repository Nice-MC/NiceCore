package br.com.nicemc.api.command

import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import java.util.function.Consumer

class CommandInterruptException(val action: Consumer<CommandSender>) : Exception() {

    companion object {
        fun makeAssertion(condition: Boolean, failMsg: String) {
            if (!condition) {
                throw CommandInterruptException(failMsg)
            }
        }
    }

    constructor(message: String) : this(action = Consumer {
        it.sendMessage(
            ChatColor.translateAlternateColorCodes(
                '&',
                message
            )
        )
    })

}