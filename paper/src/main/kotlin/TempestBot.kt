package cloud.drakon.tempestbot

import cloud.drakon.tempestbot.commands.TranslateCommand
import com.github.shynixn.mccoroutine.bukkit.SuspendingJavaPlugin
import com.github.shynixn.mccoroutine.bukkit.setSuspendingExecutor

class TempestBot : SuspendingJavaPlugin() {
    override suspend fun onEnableAsync() {
        this.getCommand("translate") !!.setSuspendingExecutor(TranslateCommand())
    }
}
