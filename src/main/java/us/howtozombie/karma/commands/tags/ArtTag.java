package us.howtozombie.karma.commands.tags;

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

public class ArtTag extends Command {

    public ArtTag() {
        super("arttag");
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {

        if (sender.hasPermission("karma.tag.art") || sender instanceof ConsoleCommandSender) {
            if (args.length < 1) {
                sendMessage(sender, "&c⚠ &4Invalid Syntax! &c/arttag [player] [tag]");
                return true;
            } else if (args.length == 1) {
                User u = User.getUser(args[0]);

                if (u == null) {
                    sendMessage(sender, "&c⚠ &4Invalid Player!");
                    return true;
                }
                u.removeArtTag();
            } else {
                User u = User.getUser(args[0]);

                if (u == null) {
                    sendMessage(sender, "&c⚠ &4Invalid Player!");
                    return true;
                }

                StringBuilder b = new StringBuilder();

                for (int i = 1; i < args.length; i++) {

                    b.append(args[i]);
                    if (i != args.length - 1) {
                        b.append(" ");
                    }

                }

                u.setArtTag(b.toString());
                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                        "&7" + args[0] + " is now a " + b.toString()));

            }
        } else {
            sendMessage(sender, "&c⚠ &4Invalid Command!");
        }

        return true;
    }

}
