package cloud.drakon.tempestbot.commands

import cloud.drakon.tempestbot.translate
import com.github.shynixn.mccoroutine.bukkit.SuspendingCommandExecutor
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class TranslateCommand : SuspendingCommandExecutor {
    override suspend fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>,
    ): Boolean {
        if (command.name == "translate") {
            val text = StringBuilder(args[0])
            for (arg in 1 until args.size - 2) text.append(" ").append(args[arg])

            Bukkit.getPlayer(sender.name)?.chat(
                translate(
                    Text = text.toString(),
                    TargetLanguageCode = args[args.size - 2],
                    SourceLanguageCode = args[args.size - 1]
                )
            )
            return true
        }
        return false
    }
}
