package br.com.nicemc.api.builder.item

import com.comphenix.protocol.wrappers.WrappedGameProfile
import com.comphenix.protocol.wrappers.WrappedSignedProperty
import org.bukkit.Material
import org.bukkit.OfflinePlayer
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import java.lang.reflect.Field
import java.util.*
import java.util.function.Consumer


class SkullBuilder(
    private val itemStack: ItemStack
) {

    constructor() : this(ItemStack(Material.SKULL_ITEM, 1, 3))

    init {
        if (itemStack.type != Material.SKULL_ITEM) {
            itemStack.type = Material.SKULL_ITEM
            itemStack.durability = 3
        }
    }

    private fun changeItem(consumer: Consumer<ItemStack>): SkullBuilder {
        consumer.accept(this.itemStack)
        return this
    }

    private fun changeMeta(consumer: Consumer<SkullMeta>): SkullBuilder {
        val itemMeta = itemStack.itemMeta as SkullMeta
        consumer.accept(itemMeta)
        this.itemStack.itemMeta = itemMeta
        return this
    }

    fun owner(offlinePlayer: OfflinePlayer) = changeMeta { it.owner = offlinePlayer.name }

    fun textures(textures: String) = changeMeta {
        val profile = WrappedGameProfile(UUID.randomUUID(), null)
        val propertyMap = profile.properties
        if (propertyMap != null) {
            val encodedData: ByteArray = Base64.getEncoder().encode(
                String.format("{textures:{SKIN:{url:\"%s\"}}}", textures).toByteArray()
            )
            propertyMap.put("textures", WrappedSignedProperty("textures", String(encodedData), null))

            try {
                val field: Field = it.javaClass.getDeclaredField("profile")
                field.isAccessible = true
                field[it] = profile
            } catch (e: NoSuchFieldException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }
        }
    }

    fun name(displayName: String): SkullBuilder = changeMeta { it.displayName = displayName }

    fun amount(amount: Int): SkullBuilder = changeItem() { it.amount = amount }

    fun lore(lore: List<String>): SkullBuilder {
        return changeMeta { it.lore = lore }
    }

    fun lore(vararg lore: String): SkullBuilder {
        return changeMeta { it.lore = listOf(*lore) }
    }

    fun lore(consumer: Consumer<List<String>>): SkullBuilder {
        val lore: List<String> = ArrayList()
        consumer.accept(lore)
        return changeMeta { it.lore = lore }
    }

    fun flag(vararg flags: ItemFlag): SkullBuilder {
        return changeMeta { it.addItemFlags(*flags) }
    }

    fun removeFlag(vararg flags: ItemFlag): SkullBuilder {
        return changeMeta { it.removeItemFlags(*flags) }
    }

    fun with(consumer: Consumer<SkullBuilder>): SkullBuilder {
        consumer.accept(this)
        return this
    }

    fun build(): ItemStack {
        return itemStack
    }
}