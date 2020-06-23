package us.howtozombie.karma.commands.ranks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import us.howtozombie.misc.Command;
import us.howtozombie.Main;
import us.howtozombie.karma.user.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class AdminRank extends Command {

    public AdminRank() {
        super("toggleadmin");
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {

        if (sender.hasPermission("karma.rank.admin") || sender instanceof ConsoleCommandSender) {
            if (args.length < 1) {
                sendMessage(sender, "&c⚠ &4Invalid Syntax! &c/toggleadmin [player]");
                return true;
            } else {

                User u = User.getUser(args[0]);

                if (u == null) {
                    sendMessage(sender, "&c⚠ &4Invalid Player!");
                    return true;
                }

                if (u.getAdminRank() > 0) {

                    u.setAdminRank(0);
                    sendMessage(sender, "&6" + args[0] + " is no longer an Admin.");

                } else {

                    u.setAdminRank(2);
                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                            "&7" + args[0] + " is now an &4Admin"));

                }

            }
        } else {
            sendMessage(sender, "&c⚠ &4Invalid Command!");
        }

        return true;

    }

}
