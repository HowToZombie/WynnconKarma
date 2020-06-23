package us.howtozombie.karma.commands.ranks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import us.howtozombie.karma.user.SubRank;
import us.howtozombie.misc.Command;
import us.howtozombie.karma.user.User;
import us.howtozombie.tickets.util.RomanNumeral;

public class GMRank extends Command {

    public GMRank() {
        super("gmrank");
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {

        if (sender.hasPermission("karma.rank.gm") || sender instanceof ConsoleCommandSender) {
            if (args.length < 2) {
                sendMessage(sender, "&c⚠ &4Invalid Syntax! &c/gmrank [player] [rank ID] [rank level]");
                return true;
            } else if (args.length == 2) {

                User u = User.getUser(args[0]);

                if (u == null) {
                    sendMessage(sender, "&c⚠ &4Invalid Player!");
                    return true;
                }

                int rankID;

                try {
                    rankID = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    sendMessage(sender, "&c⚠ &4Invalid SubRank Number!");
                    return true;
                }

                if (rankID > SubRank.rankNum - 1) {
                    sendMessage(sender, "&c⚠ &4Invalid SubRank Number!");
                    return true;
                }

                SubRank r = SubRank.subranks.get(rankID);

                u.setGMRank(rankID);

                if (rankID > 0) {

                    if (SubRank.subranks.get(rankID).hasSubcategories()) {

                        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                                "&7" + args[0] + " is now a " + r.getTag() + "GM I"));
                        u.setDevNum(1);

                    } else {
                        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                                "&7" + args[0] + " is now a " + r.getTag() + "GM"));
                        u.setGMNum(0);
                    }

                } else {
                    sendMessage(sender, "&6" + args[0] + " is no longer a GM.");
                    u.setGMNum(0);
                }

            } else {

                User u = User.getUser(args[0]);

                if (u == null) {
                    sendMessage(sender, "&c⚠ &4Invalid Player!");
                    return true;
                }

                int rankID;
                int rankNum;

                try {
                    rankID = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    sendMessage(sender, "&c⚠ &4Invalid SubRank Number!");
                    return true;
                }

                if (rankID > SubRank.rankNum - 1) {
                    sendMessage(sender, "&c⚠ &4Invalid SubRank Number!");
                    return true;
                }

                SubRank r = SubRank.subranks.get(rankID);

                try {
                    rankNum = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    sendMessage(sender, "&c⚠ &4Invalid SubRank Level!");
                    return true;
                }

                if (rankNum > r.getMaxNum() || !r.hasSubcategories()) {
                    sendMessage(sender, "&c⚠ &4Invalid SubRank Level!");
                    return true;
                }

                u.setGMRank(rankID);
                u.setGMNum(rankNum);

                if (u.getGMNum() == 0) {
                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                            "&7" + args[0] + " is now a " + r.getTag() + "GM"));
                } else {
                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                            "&7" + args[0] + " is now a " + r.getTag() + "GM " + RomanNumeral.toRoman(u.getGMNum())));
                }

            }
        } else {
            sendMessage(sender, "&c⚠ &4Invalid Command!");
        }

        return true;

    }

}
