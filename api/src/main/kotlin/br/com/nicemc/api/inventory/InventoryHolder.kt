package br.com.nicemc.api.inventory

import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder

class InventoryHolder(var niceInventory: NiceInventory) : InventoryHolder {

    override fun getInventory(): Inventory? {
        return niceInventory.inventory
    }

    fun destroy() {

    }
}