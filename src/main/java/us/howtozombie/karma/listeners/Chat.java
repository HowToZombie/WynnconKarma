package us.howtozombie.karma.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import us.howtozombie.karma.user.SubRank;
import us.howtozombie.karma.user.User;
import us.howtozombie.tickets.util.RomanNumeral;

import java.util.ArrayList;
import java.util.Collections;

public class Chat implements Listener {

    @EventHandler
    public void chat(AsyncPlayerChatEvent e) {

        Player p = e.getPlayer();
        User u = User.users.get(p.getUniqueId());

        int rankNum = 0;

        StringBuilder chatTag = new StringBuilder();

        int karma = 0;

        int artRank = 0;
        int buildRank = 0;
        int cmdRank = 0;
        int devRank = 0;
        int gmRank = 0;

        ArrayList<String> rankOrder = orderRanks(u);

        if (u.getAdminRank() > 0) {
            rankNum += 1;
            karma += u.getAdminKarma();
        }
        if (u.getArtRank() > 0) {
            rankNum += 1;
            artRank = u.getArtRank();
            karma += u.getArtKarma();
        }
        if (u.getBuildRank() > 0) {
            rankNum += 1;
            buildRank = u.getBuildRank();
            karma += u.getBuildKarma();
        }
        if (u.getCmdRank() > 0) {
            rankNum += 1;
            cmdRank = u.getCmdRank();
            karma += u.getCmdKarma();
        }
        if (u.getDevRank() > 0) {
            rankNum += 1;
            devRank = u.getDevRank();
            karma += u.getDevKarma();
        }
        if (u.getGMRank() > 0) {
            rankNum += 1;
            gmRank = u.getGMRank();
            karma += u.getGMKarma();
        }

        chatTag.append("&7[");
        if (rankNum == 0) {

            chatTag.append("Guest");

        } else if (rankNum > 1) {
            boolean started = false;

            if (u.getAdminRank() > 0) {
                if (u.hasAdminTag()) {
                    chatTag.append(u.getAdminTag());
                } else {
                    chatTag.append("&4Admin");
                }
                started = true;
            }

            for (String s : rankOrder) {

                if (s.equalsIgnoreCase("DEVELOPER")) {

                    if (u.getDevRank() > 1) {
                        if (started) {
                            chatTag.append("&7/");
                        }
                        if (u.hasDevTag()) {
                            chatTag.append(u.getDevTag());
                        } else {
                            chatTag.append(SubRank.subranks.get(devRank).getShortTag()).append("Dev");
                        }
                        started = true;
                    } else if (u.getDevRank() == 1) {
                        if (started) {
                            chatTag.append("&7/");
                        }
                        if (u.hasDevTag()) {
                            chatTag.append(u.getDevTag());
                        } else {
                            chatTag.append(SubRank.subranks.get(devRank).getShortTag()).append("Dev");
                        }
                        started = true;
                    }

                }

                if (s.equalsIgnoreCase("BUILDER")) {

                    if (u.getBuildRank() > 1) {
                        if (started) {
                            chatTag.append("&7/");
                        }
                        if (u.hasBuildTag()) {
                            chatTag.append(u.getBuildTag());
                        } else {
                            chatTag.append(SubRank.subranks.get(buildRank).getShortTag()).append("Builder");
                        }
                        started = true;
                    } else if (u.getBuildRank() == 1) {
                        if (started) {
                            chatTag.append("&7/");
                        }
                        if (u.hasBuildTag()) {
                            chatTag.append(u.getBuildTag());
                        } else {
                            chatTag.append(SubRank.subranks.get(buildRank).getShortTag()).append("Builder");
                        }
                        started = true;
                    }

                }

                if (s.equalsIgnoreCase("GM")) {

                    if (u.getGMRank() > 1) {
                        if (started) {
                            chatTag.append("&7/");
                        }
                        if (u.hasGMTag()) {
                            chatTag.append(u.getGMTag());
                        } else {
                            chatTag.append(SubRank.subranks.get(gmRank).getShortTag()).append("GM");
                        }
                    } else if (u.getGMRank() == 1) {
                        if (started) {
                            chatTag.append("&7/");
                        }
                        if (u.hasArtTag()) {
                            chatTag.append(u.getGMTag());
                        } else {
                            chatTag.append(SubRank.subranks.get(gmRank).getShortTag()).append("GM");
                        }
                    }

                }

                if (s.equalsIgnoreCase("CMD")) {

                    if (u.getCmdRank() > 1) {
                        if (started) {
                            chatTag.append("&7/");
                        }
                        if (u.hasCmdTag()) {
                            chatTag.append(u.getCmdTag());
                        } else {
                            chatTag.append(SubRank.subranks.get(cmdRank).getShortTag()).append("CMD");
                        }
                        started = true;
                    } else if (u.getCmdRank() == 1) {
                        if (started) {
                            chatTag.append("&7/");
                        }
                        if (u.hasCmdTag()) {
                            chatTag.append(u.getCmdTag());
                        } else {
                            chatTag.append(SubRank.subranks.get(cmdRank).getShortTag()).append("CMD");
                        }
                        started = true;
                    }

                }

                if (s.equalsIgnoreCase("ARTIST")) {

                    if (u.getArtRank() > 1) {
                        if (started) {
                            chatTag.append("&7/");
                        }
                        if (u.hasArtTag()) {
                            chatTag.append(u.getArtTag());
                        } else {
                            chatTag.append(SubRank.subranks.get(artRank).getShortTag()).append("Artist");
                        }
                    } else if (u.getArtRank() == 1) {
                        if (started) {
                            chatTag.append("&7/");
                        }
                        if (u.hasArtTag()) {
                            chatTag.append(u.getArtTag());
                        } else {
                            chatTag.append(SubRank.subranks.get(artRank).getShortTag()).append("Artist");
                        }
                    }

                }

            }

        } else {
            if (u.getAdminRank() > 1) {
                if (u.hasAdminTag()) {
                    chatTag.append(u.getAdminTag());
                } else {
                    chatTag.append("&4Admin");
                }
            }
            if (u.getDevRank() > 0) {
                if (u.hasDevTag()) {
                    chatTag.append(u.getDevTag());
                } else {
                    chatTag.append(SubRank.subranks.get(devRank).getTag()).append("Dev");
                    if (SubRank.subranks.get(devRank).hasSubcategories()) {
                        if (u.getDevNum() != 0) {
                            chatTag.append(" " + RomanNumeral.toRoman(u.getDevNum()));
                        }
                    }
                }
            }
            if (u.getBuildRank() > 0) {
                if (u.hasBuildTag()) {
                    chatTag.append(u.getBuildTag());
                } else {
                    chatTag.append(SubRank.subranks.get(buildRank).getTag()).append("Builder");
                    if (SubRank.subranks.get(buildRank).hasSubcategories()) {
                        if (u.getBuildNum() != 0) {
                            chatTag.append(" " + RomanNumeral.toRoman(u.getBuildNum()));
                        }
                    }
                }
            }
            if (u.getArtRank() > 0) {
                if (u.hasArtTag()) {
                    chatTag.append(u.getArtTag());
                } else {
                    chatTag.append(SubRank.subranks.get(artRank).getTag()).append("Artist");
                    if (SubRank.subranks.get(artRank).hasSubcategories()) {
                        if (u.getArtNum() != 0) {
                            chatTag.append(" " + RomanNumeral.toRoman(u.getArtNum()));
                        }
                    }
                }
            }
            if (u.getCmdRank() > 0) {
                if (u.hasCmdTag()) {
                    chatTag.append(u.getCmdTag());
                } else {
                    chatTag.append(SubRank.subranks.get(cmdRank).getTag()).append("CMD");
                    if (SubRank.subranks.get(cmdRank).hasSubcategories()) {
                        if (u.getCmdNum() != 0) {
                            chatTag.append(" " + RomanNumeral.toRoman(u.getCmdNum()));
                        }
                    }
                }
            }
            if (u.getGMRank() > 0) {
                if (u.hasGMTag()) {
                    chatTag.append(u.getGMTag());
                } else {
                    chatTag.append(SubRank.subranks.get(gmRank).getTag()).append("GM");
                    if (SubRank.subranks.get(gmRank).hasSubcategories()) {
                        if (u.getGMNum() != 0) {
                            chatTag.append(" " + RomanNumeral.toRoman(u.getGMNum()));
                        }
                    }
                }
            }
        }

        chatTag.append("&7]");
        String rank = chatTag.toString();
        String message = e.getMessage().replaceAll("%", "%%");

        String karmaTag = "&7[" + karma + "]";

        e.setFormat(ChatColor.translateAlternateColorCodes('&',
                karmaTag + " " + rank + " &7" + p.getName() + ": &f" + message));

    }

    public ArrayList<String> orderRanks(User u) {

        ArrayList<Integer> karmaOrder = new ArrayList<>();

        karmaOrder.add(u.getArtKarma());
        karmaOrder.add(u.getBuildKarma());
        karmaOrder.add(u.getCmdKarma());
        karmaOrder.add(u.getDevKarma());
        karmaOrder.add(u.getGMKarma());

        Collections.sort(karmaOrder);

        ArrayList<String> order = new ArrayList<>();

        for (int i = karmaOrder.size() - 1; i >= 0; i--) {

            if (u.getDevKarma() == karmaOrder.get(i) && !order.contains("DEVELOPER")) {
                order.add("DEVELOPER");
            } else if (u.getBuildKarma() == karmaOrder.get(i) && !order.contains("BUILDER")) {
                order.add("BUILDER");
            } else if (u.getGMKarma() == karmaOrder.get(i) && !order.contains("GM")) {
                order.add("GM");
            } else if (u.getCmdKarma() == karmaOrder.get(i) && !order.contains("CMD")) {
                order.add("CMD");
            } else if (u.getArtKarma() == karmaOrder.get(i) && !order.contains("ARTIST")) {
                order.add("ARTIST");
            }

        }

        return order;

    }

}
