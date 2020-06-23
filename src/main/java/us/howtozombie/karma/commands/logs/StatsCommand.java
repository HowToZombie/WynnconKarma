package us.howtozombie.karma.commands.logs;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import us.howtozombie.karma.user.SubRank;
import us.howtozombie.misc.Command;
import us.howtozombie.karma.user.User;
import us.howtozombie.tickets.util.RomanNumeral;

public class StatsCommand extends Command {

    public StatsCommand() {
        super("stats");
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {

        if (sender.hasPermission("karma.stats") || sender instanceof ConsoleCommandSender) {

            if (args.length < 1) {

                sendMessage(sender, "&c⚠ &4Invalid Syntax! &c/stats [player]");

            } else {

                User u = User.getUser(args[0]);

                if (u == null) {
                    sendMessage(sender, "&c⚠ &4Invalid Player!");
                    return true;
                }

                sendMessage(sender, "&7[======= Stats for " + args[0] + " =======]");
                sendMessage(sender, "");
                if (u.getAdminRank() > 0) {
                    if (u.hasAdminTag()) {
                        sendMessage(sender, "&6 - " + u.getAdminTag() + " &7[" + u.getAdminKarma() + " Karma]");
                    } else {
                        sendMessage(sender, "&6 - &4Admin &7[" + u.getAdminKarma() + " Karma]");
                    }
                }
                if (u.getDevRank() > 0) {
                    if (u.hasDevTag()) {
                        sendMessage(sender, "&6 - " + u.getDevTag() + " &7[" + u.getDevKarma() + " Karma]");
                    } else {
                        if (u.getDevNum() == 0) {
                            sendMessage(sender, "&6 - " + SubRank.subranks.get(u.getDevRank()).getTag() + "Developer &7[" + u.getDevKarma() + " Karma]");
                        } else {
                            sendMessage(sender, "&6 - " + SubRank.subranks.get(u.getDevRank()).getTag() + "Developer " + RomanNumeral.toRoman(u.getDevNum())
                                    +" &7[" + u.getDevKarma() + " Karma]");
                        }
                    }
                }
                if (u.getBuildRank() > 0) {
                    if (u.hasBuildTag()) {
                        sendMessage(sender, "&6 - " + u.getBuildTag() + " &7[" + u.getBuildKarma() + " Karma]");
                    } else {
                        if (u.getBuildNum() == 0) {
                            sendMessage(sender, "&6 - " + SubRank.subranks.get(u.getBuildRank()).getTag() + "Builder &7[" + u.getBuildKarma() + " Karma]");
                        } else {
                            sendMessage(sender, "&6 - " + SubRank.subranks.get(u.getBuildRank()).getTag() + "Builder " + RomanNumeral.toRoman(u.getBuildNum())
                                    +" &7[" + u.getBuildKarma() + " Karma]");
                        }
                    }
                }
                if (u.getCmdRank() > 0) {
                    if (u.hasCmdTag()) {
                        sendMessage(sender, "&6 - " + u.getCmdTag() + " &7[" + u.getCmdKarma() + " Karma]");
                    } else {
                        if (u.getCmdNum() == 0) {
                            sendMessage(sender, "&6 - " + SubRank.subranks.get(u.getCmdRank()).getTag() + "CMD &7[" + u.getCmdKarma() + " Karma]");
                        } else {
                            sendMessage(sender, "&6 - " + SubRank.subranks.get(u.getCmdRank()).getTag() + "CMD " + RomanNumeral.toRoman(u.getCmdNum())
                                    +" &7[" + u.getCmdKarma() + " Karma]");
                        }
                    }
                }
                if (u.getArtRank() > 0) {
                    if (u.hasArtTag()) {
                        sendMessage(sender, "&6 - " + u.getArtTag() + " &7[" + u.getArtKarma() + " Karma]");
                    } else {
                        if (u.getArtNum() == 0) {
                            sendMessage(sender, "&6 - " + SubRank.subranks.get(u.getArtRank()).getTag() + "Artist &7[" + u.getArtKarma() + " Karma]");
                        } else {
                            sendMessage(sender, "&6 - " + SubRank.subranks.get(u.getArtRank()).getTag() + "Artist " + RomanNumeral.toRoman(u.getArtNum())
                                    +" &7[" + u.getArtKarma() + " Karma]");
                        }
                    }
                }
                if (u.getGMRank() > 0) {
                    if (u.hasGMTag()) {
                        sendMessage(sender, "&6 - " + u.getGMTag() + " &7[" + u.getGMKarma() + " Karma]");
                    } else {
                        if (u.getGMNum() == 0) {
                            sendMessage(sender, "&6 - " + SubRank.subranks.get(u.getGMRank()).getTag() + "GM &7[" + u.getGMKarma() + " Karma]");
                        } else {
                            sendMessage(sender, "&6 - " + SubRank.subranks.get(u.getGMRank()).getTag() + "GM " + RomanNumeral.toRoman(u.getGMNum())
                                    +" &7[" + u.getGMKarma() + " Karma]");
                        }
                    }
                }
                sendMessage(sender, "");
                int karma = 0;
                if (u.getAdminRank() > 0) {
                    karma += u.getAdminKarma();
                }
                if (u.getArtRank() > 0) {
                    karma += u.getArtKarma();
                }
                if (u.getBuildRank() > 0) {
                    karma += u.getBuildKarma();
                }
                if (u.getCmdRank() > 0) {
                    karma += u.getCmdKarma();
                }
                if (u.getDevRank() > 0) {
                    karma += u.getDevKarma();
                }
                if (u.getGMRank() > 0) {
                    karma += u.getGMKarma();
                }
                sendMessage(sender, "&6Total Karma: &7" + karma);

            }

        } else {

            sendMessage(sender, "&c⚠ &4Invalid Command!");

        }

        return true;
    }
}
