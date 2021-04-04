package br.com.nicemc.api.inventory

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.utility.MinecraftReflection
import com.comphenix.protocol.wrappers.WrappedChatComponent
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import java.lang.reflect.Field
import java.lang.reflect.Method

class NiceInventory(
    private val rows: Int,
    private val title: String,
    var inventory: Inventory? = null
) {

    /**
     * Created to offer better support for java only
     */
    constructor(rows: Int, title: String) : this(rows, title, null)

    private val slotItem: MutableMap<Int, InventoryItem> = mutableMapOf()

    fun open(player: Player) {
        if (player.openInventory == null
            || player.openInventory.topInventory.type != InventoryType.CHEST
            || player.openInventory.topInventory.size != rows * 9 || player.openInventory.topInventory.holder == null
            || player.openInventory.topInventory.holder !is InventoryHolder
        ) {
            createAndOpenInventory(player)
        } else {
            // Update the current inventory of player
            for (i in 0 until rows * 9) {
                if (slotItem.containsKey(i)) {
                    player.openInventory.topInventory.setItem(i, slotItem[i]?.itemStack)
                } else {
                    player.openInventory.topInventory.setItem(i, null)
                }
            }
            player.updateInventory()
        }
        (player.openInventory.topInventory.holder as InventoryHolder).niceInventory = this
    }

    fun updateTitle(player: Player) {
        try {
            val packet = PacketContainer(PacketType.Play.Server.OPEN_WINDOW)
            packet.chatComponents.write(0, WrappedChatComponent.fromText(title))
            val getHandle: Method = MinecraftReflection.getCraftPlayerClass().getMethod("getHandle")
            val entityPlayer: Any = getHandle.invoke(player)
            val activeContainerField: Field = entityPlayer.javaClass.getField("activeContainer")
            val activeContainer: Any = activeContainerField.get(entityPlayer)
            val windowIdField: Field = activeContainer.javaClass.getField("windowId")
            val id: Int = windowIdField.getInt(activeContainer)
            packet.strings.write(0, "minecraft:chest")
            packet.integers.write(0, id)
            packet.integers.write(1, rows * 9)
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet)
            var i = 0
            for (item in player.inventory.contents) {
                player.inventory.setItem(i, item)
                i += 1
            }
            player.updateInventory()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun createAndOpenInventory(player: Player) {
        val playerInventory = Bukkit.createInventory(InventoryHolder(this), rows * 9, title)
        for ((key, value) in slotItem) {
            playerInventory.setItem(key, value.itemStack)
        }
        this.inventory = playerInventory
        player.openInventory(playerInventory)
    }

    fun addItem(item: InventoryItem) {
        setItem(firstEmpty(), item)
    }

    fun addItem(item: ItemStack) {
        setItem(firstEmpty(), item)
    }

    fun setItem(item: ItemStack, slot: Int) {
        setItem(slot, InventoryItem(item))
    }

    fun setItem(slot: Int, item: ItemStack) {
        setItem(slot, InventoryItem(item))
    }

    fun setItem(
        slot: Int,
        item: ItemStack,
        function: (player: Player, clickType: ClickType, itemStack: ItemStack, slot: Int) -> Boolean
    ) {
        setItem(slot, InventoryItem(item, function))
    }

    fun setItem(item: InventoryItem, slot: Int) {
        setItem(slot, item)
    }

    fun setItem(slot: Int, item: InventoryItem) {
        slotItem[slot] = item
        inventory?.setItem(slot, item.itemStack)
    }

    fun firstEmpty(): Int {
        return if (inventory != null) {
            inventory!!.firstEmpty()
        } else {
            for (i in 0 until rows * 9) {
                if (!slotItem.containsKey(i)) {
                    return i
                }
            }
            -1
        }
    }

    fun hasItem(slot: Int): Boolean {
        return slotItem.containsKey(slot)
    }

    fun getItem(slot: Int): InventoryItem? {
        return slotItem[slot]
    }

    fun clear() {
        slotItem.clear()
    }

}