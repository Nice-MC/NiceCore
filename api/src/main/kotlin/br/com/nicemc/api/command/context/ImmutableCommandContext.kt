package br.com.nicemc.api.command.context

import com.google.common.collect.ImmutableList
import org.bukkit.command.CommandSender

class ImmutableCommandContext(
    private val sender: CommandSender,
    private val label: String,
    private val args: ImmutableList<String>
) : CommandContext {

    constructor(sender: CommandSender, label: String, args: Array<out String>) : this(
        sender, label, ImmutableList.copyOf(args)
    )

    override fun sender(): CommandSender = sender

    override fun args(): ImmutableList<String> = args

    override fun rawArg(index: Int): String? {
        if (index < 0 || index >= args.size) {
            return null
        }
        return args[index]
    }

    override fun label(): String = label
}