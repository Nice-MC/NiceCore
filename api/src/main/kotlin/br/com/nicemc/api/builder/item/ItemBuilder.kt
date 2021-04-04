package br.com.nicemc.api.builder.item

import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import java.util.function.Consumer


class ItemBuilder(
    private val itemStack: ItemStack
) {

    constructor(material: Material) : this(ItemStack(material))

    private fun changeItem(consumer: Consumer<ItemStack>): ItemBuilder {
        consumer.accept(this.itemStack)
        return this
    }

    private fun changeMeta(consumer: Consumer<ItemMeta>): ItemBuilder {
        val itemMeta = itemStack.itemMeta
        consumer.accept(itemMeta)
        this.itemStack.itemMeta = itemMeta
        return this
    }

    fun name(displayName: String): ItemBuilder = changeMeta { it.displayName = displayName }

    fun amount(amount: Int): ItemBuilder = changeItem() { it.amount = amount }

    fun durability(durability: Int): ItemBuilder = changeItem {
        it.durability = durability.toShort()
    }

    fun enchantment(enchantment: Enchantment, level: Int): ItemBuilder =
        changeItem { it.addEnchantment(enchantment, level) }

    fun lore(lore: List<String>): ItemBuilder {
        return changeMeta { it.lore = lore }
    }

    fun lore(vararg lore: String): ItemBuilder {
        return changeMeta { it.lore = listOf(*lore) }
    }

    fun lore(consumer: Consumer<List<String>>): ItemBuilder {
        val lore: List<String> = ArrayList()
        consumer.accept(lore)
        return changeMeta { it.lore = lore }
    }

    fun flag(vararg flags: ItemFlag): ItemBuilder {
        return changeMeta { it.addItemFlags(*flags) }
    }

    fun removeFlag(vararg flags: ItemFlag): ItemBuilder {
        return changeMeta { it.removeItemFlags(*flags) }
    }

    fun hideEnchantments(b: Boolean): ItemBuilder {
        return changeMeta { it.addItemFlags(ItemFlag.HIDE_ENCHANTS) }
    }

    fun unbreakable(b: Boolean): ItemBuilder {
        return changeMeta { it.spigot().isUnbreakable = b }
    }

    fun with(consumer: Consumer<ItemBuilder>): ItemBuilder {
        consumer.accept(this)
        return this
    }

    fun build(): ItemStack {
        return itemStack
    }
}