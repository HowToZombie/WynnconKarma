package us.howtozombie.karma.commands.karma;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import us.howtozombie.misc.Command;
import us.howtozombie.Main;
import us.howtozombie.karma.user.Log;
import us.howtozombie.karma.user.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class BuildKarma extends Command {

    public BuildKarma() {
        super("buildkarma");
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {

        if (sender.hasPermission("karma.give.build")) {
            if (args.length < 2) {
                sendMessage(sender, "&c⚠ &4Invalid Syntax! &c/buildkarma [player] [number] [reason]");
                return true;
            } else {

                User u = User.getUser(args[0]);

                if (u == null) {
                    sendMessage(sender, "&c⚠ &4Invalid Player!");
                    return true;
                }

                int karma;

                try {
                    karma = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    sendMessage(sender, "&c⚠ &4Invalid Karma Amount!");
                    return true;
                }

                if (karma == 0) {
                    sendMessage(sender, "&c⚠ &4Invalid Karma Amount!");
                    return true;
                }

                if (args.length > 2) {
                    StringBuilder b = new StringBuilder();
                    for (int i = 2; i < args.length; i++) {
                        b.append(args[i]);
                        if (i != args.length - 1) {
                            b.append(" ");
                        }
                    }

                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                            "&7" + args[0] + " has recieved " + args[1] + " Builder Karma for " + b.toString()));
                    Log.addToLog(args[0], ((Player) sender).getUniqueId(), karma, true, b.toString(), "Builder");
                } else {

                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                            "&7" + args[0] + " has recieved " + args[1] + " Builder Karma"));
                    Log.addToLog(args[0], ((Player) sender).getUniqueId(), karma, false, "", "Builder");

                }

                u.setBuildKarma(u.getBuildKarma() + karma);

            }
        } else if (sender instanceof ConsoleCommandSender) {

            if (args.length < 2) {
                sendMessage(sender, "&c⚠ &4Invalid Syntax! &c/buildkarma [player] [number] [reason]");
                return true;
            } else {

                User u = User.getUser(args[0]);

                if (u == null) {
                    sendMessage(sender, "&c⚠ &4Invalid Player!");
                    return true;
                }

                int karma;

                try {
                    karma = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    sendMessage(sender, "&c⚠ &4Invalid Karma Amount!");
                    return true;
                }

                if (karma == 0) {
                    sendMessage(sender, "&c⚠ &4Invalid Karma Amount!");
                    return true;
                }

                if (args.length > 2) {
                    StringBuilder b = new StringBuilder();
                    for (int i = 2; i < args.length; i++) {
                        b.append(args[i]);
                        if (i != args.length - 1) {
                            b.append(" ");
                        }
                    }

                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                            "&7" + args[0] + " has recieved " + args[1] + " Builder Karma for " + b.toString()));
                    Log.addToLog(args[0], karma, true, b.toString(), "Builder");
                } else {

                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                            "&7" + args[0] + " has recieved " + args[1] + " Builder Karma"));
                    Log.addToLog(args[0], karma, false, "", "Builder");

                }

                u.setBuildKarma(u.getBuildKarma() + karma);

            }

        } else {
            sendMessage(sender, "&c⚠ &4Invalid Command!");
        }

        return true;

    }
    
}
