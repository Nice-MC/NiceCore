package br.com.nicemc.api.scoreboard

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Objective
import org.bukkit.scoreboard.Scoreboard
import java.util.function.Consumer

class NiceScoreboard(
    val player: Player,
    displayName: String,
) {

    companion object {
        private const val MAX_LINES = 16
        private const val MAX_PREFIX_LENGTH = 16
        private const val MAX_NAME_LENGTH = 16
        private const val MAX_SUFFIX_LENGTH = 16
    }

    private val lines: MutableList<Line> = mutableListOf()
    private val scoreboard: Scoreboard = Bukkit.getScoreboardManager().newScoreboard
    private val objective: Objective = this.scoreboard.registerNewObjective(displayName, "dummy")
    private var lineConsumer: Consumer<List<Line>>? = null

    private var previousLines: MutableSet<String> = mutableSetOf()

    init {
        objective.displaySlot = DisplaySlot.SIDEBAR
        player.scoreboard = scoreboard
    }

    fun addLine(line: Line) {
        this.lines.add(line)
    }

    fun addLine(prefix: String, middle: String, suffix: String) {
        this.lines.add(Line(prefix, middle, suffix))
    }

    fun with(consumer: Consumer<NiceScoreboard>): NiceScoreboard {
        consumer.accept(this)
        return this
    }

    fun setLineConsumer(consumer: Consumer<List<Line>>): NiceScoreboard {
        lineConsumer = consumer
        return this
    }

    fun setDisplayName(displayName: String) {
        this.objective.displayName = displayName
    }

    fun update() {
        val size = MAX_LINES.coerceAtMost(lines.size)
        var count = 0

        lineConsumer?.accept(lines)

        val adding = mutableSetOf<String>()
        lines.forEach {
            var middle = it.middle
            if (middle.length > MAX_NAME_LENGTH) {
                middle = middle.substring(0, MAX_NAME_LENGTH);
            }
            if (scoreboard.getTeam(middle) == null) scoreboard.registerNewTeam(middle)
            val team = scoreboard.getTeam(middle)
            if (it.prefix.isNotBlank()) {
                team.prefix = if (it.prefix.length > MAX_PREFIX_LENGTH) {
                    it.prefix.substring(0, MAX_PREFIX_LENGTH)
                } else {
                    it.prefix
                }
            }
            if (it.suffix.isNotBlank()) {
                team.suffix = if (it.suffix.length > MAX_PREFIX_LENGTH) {
                    it.suffix.substring(0, MAX_SUFFIX_LENGTH)
                } else {
                    it.suffix
                }
            }
            adding.add(middle)
            if (!team.hasEntry(middle)) {
                team.addEntry(middle)
            }
            objective.getScore(middle).score = size - count++
        }

        previousLines.removeIf(adding::contains)

        val iterator = previousLines.iterator()
        while (iterator.hasNext()) {
            val last = iterator.next()
            val team = scoreboard.getTeam(last)
            team?.removeEntry(last)
            scoreboard.resetScores(last)
            iterator.remove()
        }

        previousLines = adding
    }

}

