package br.com.nicemc.api.inventory

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class InventoryItem(
    val itemStack: ItemStack,
    val function: (player: Player, clickType: ClickType, itemStack: ItemStack, slot: Int) -> Boolean
) {

    constructor(itemStack: ItemStack) : this(itemStack, ({ _, _, _, _ -> true }))

}