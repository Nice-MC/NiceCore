package br.com.nicemc.api.inventory

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.*

class InventoryListener : Listener {

    @EventHandler
    fun onInventoryClickEvent(event: InventoryClickEvent) {
        if (event.inventory == null) {
            return
        }
        with(event.inventory) {
            val holder = holder
            if (type != InventoryType.CHEST || holder == null || (holder !is InventoryHolder)) {
                return
            }
            event.isCancelled = true
            if (event.clickedInventory != this || (event.whoClicked !is Player) || event.slot < 0) {
                return
            }
            val niceInventory = holder.niceInventory
            if (niceInventory.hasItem(event.slot)) {
                val item = niceInventory.getItem(event.slot)
                item!!.function(
                    event.whoClicked as Player, if (event.action == InventoryAction.PICKUP_HALF) {
                        ClickType.RIGHT
                    } else {
                        ClickType.LEFT
                    }, event.currentItem, event.slot
                )
            }
        }
    }

    @EventHandler
    fun onInventoryDragEvent(event: InventoryDragEvent) {
        val inv = event.inventory
        if (inv == null
            || inv.type != InventoryType.CHEST
            || inv.holder == null
            || inv.holder !is InventoryHolder
            || event.whoClicked !is Player
        ) {
            return
        }
        event.isCancelled = true
    }

    @EventHandler
    fun onInventoryCloseEvent(event: InventoryCloseEvent) {
        if (event.inventory == null) {
            return
        }
        val inventory = event.inventory
        val holder = inventory.holder
        if (inventory.type != InventoryType.CHEST || holder == null || (holder !is InventoryHolder) || event.player !is Player) {
            return
        }
        holder.destroy()
    }

}