package us.howtozombie.karma.commands.ranks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import us.howtozombie.karma.user.SubRank;
import us.howtozombie.misc.Command;
import us.howtozombie.karma.user.User;
import us.howtozombie.tickets.util.RomanNumeral;

public class ArtRank extends Command {

    public ArtRank() {
        super("artrank");
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {

        if (sender.hasPermission("karma.rank.art") || sender instanceof ConsoleCommandSender) {
            if (args.length < 2) {
                sendMessage(sender, "&c⚠ &4Invalid Syntax! &c/artrank [player] [rank ID]");
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

                u.setArtRank(rankID);

                if (rankID > 0) {

                    if (SubRank.subranks.get(rankID).hasSubcategories()) {

                        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                                "&7" + args[0] + " is now a " + r.getTag() + "Artist I"));
                        u.setArtNum(1);

                    } else {
                        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                                "&7" + args[0] + " is now a " + r.getTag() + "Artist"));
                        u.setArtNum(0);
                    }

                } else {
                    sendMessage(sender, "&6" + args[0] + " is no longer a Artist.");
                    u.setArtNum(0);
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

                u.setArtRank(rankID);
                u.setArtNum(rankNum);

                if (u.getArtNum() == 0) {
                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                            "&7" + args[0] + " is now a " + r.getTag() + "Artist"));
                } else {
                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                            "&7" + args[0] + " is now a " + r.getTag() + "Artist " + RomanNumeral.toRoman(u.getArtNum())));
                }

            }
        } else {
            sendMessage(sender, "&c⚠ &4Invalid Command!");
        }

        return true;

    }

}
