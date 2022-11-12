package cloud.drakon.tempestbot

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.java.JavaPlugin

class TempestBot : JavaPlugin(), Listener {
    override fun onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this)
    }

    @EventHandler fun onPlayerJoin(event: PlayerJoinEvent) {
        event.player.sendMessage(
            Component.text(
                "Hello, " + event.player.name + "!"
            )
        )
    }
}
