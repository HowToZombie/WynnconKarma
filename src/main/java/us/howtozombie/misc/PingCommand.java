package us.howtozombie.misc;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PingCommand extends Command {

    public PingCommand() {
        super("ping");
    }

    @Override
    public boolean execute(CommandSender sender, String ss, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (args.length == 0) {

                p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&c⚠ &4Invalid Syntax! &c/ping [player]"));
                return true;
            }

            for (Player p1 : Bukkit.getOnlinePlayers()) {

                if (p1.getName().equalsIgnoreCase(args[0])) {

                    Bukkit.broadcastMessage(p.getName() + ChatColor.GRAY + " has pinged " + ChatColor.WHITE + p1.getName());
                    p1.playSound(p1.getLocation(), Sound.BLOCK_NOTE_PLING, 3.0F, 1.0F);
                    p1.sendTitle(ChatColor.translateAlternateColorCodes('&',
                            "&4[&c!&4]"), ChatColor.translateAlternateColorCodes('&',
                            "&c" + p.getName() + " has pinged you"));
                    return true;

                }

            }

            p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&c⚠ &4Invalid Player!"));

        }

        return true;
    }
}
