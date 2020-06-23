package us.howtozombie.tickets.commands.check;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import us.howtozombie.karma.user.SubRank;
import us.howtozombie.karma.user.User;
import us.howtozombie.misc.Command;
import us.howtozombie.tickets.util.Request;
import us.howtozombie.tickets.util.CenteredMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class CheckAll extends Command {

    public CheckAll() {
        super("checkall");
    }

    @Override
    public boolean execute(CommandSender sender, String ss, String[] args) {

        if (sender instanceof Player) {

            User u = User.getUser((Player) sender);

            if (SubRank.subranks.get(u.getArtRank()).getPermissions() > 0 ||
                    SubRank.subranks.get(u.getBuildRank()).getPermissions() > 0 ||
                    SubRank.subranks.get(u.getCmdRank()).getPermissions() > 0 ||
                    SubRank.subranks.get(u.getDevRank()).getPermissions() > 0 ||
                    SubRank.subranks.get(u.getGMRank()).getPermissions() > 0 ||
                    u.getAdminRank() > 0 || sender instanceof ConsoleCommandSender ||
                    u.getUuid().equals(UUID.fromString("463925a6-bf21-4a47-bf64-7b42f399168b"))) {

                int page = 1;

                if (args.length > 0) {

                    try {

                        page = Integer.valueOf(args[0]);

                    } catch (NumberFormatException e) {

                        sendMessage(sender, "&c⚠ &4Invalid Page Number!");

                        return true;
                    }

                }

                ArrayList<String> reqs = new ArrayList<>();
                int reqNum = 0;

                ArrayList<Integer> highRequests = new ArrayList<>();
                highRequests.addAll(Request.highRequests.keySet());
                Collections.sort(highRequests);
                Collections.reverse(highRequests);

                for (Integer i : highRequests) {

                    Request r = Request.highRequests.get(i);

                    if (r.getStage().equals(Request.RequestStage.UNCLAIMED)) {

                        reqNum++;

                        StringBuilder b = new StringBuilder();

                        if (r.isGroup()) {

                            b.append("&7").append(r.getId()).append(" ").append("&3[&b&lGROUP&3] ").append(r.getType().color).append(r.getType().getName()).append(" &c[✫✫✫] &7");

                        } else {

                            b.append("&7").append(r.getId()).append(" ").append("&2[&a&lOPEN&2] ").append(r.getType().color).append(r.getType().getName()).append(" &c[✫✫✫] &7");

                        }

                        Date d = new Date();

                        String time;

                        long difInHrs = TimeUnit.MILLISECONDS.toHours(d.getTime() - r.getDate().getTime());

                        if (difInHrs >= 24) {

                            long difInDays = TimeUnit.MILLISECONDS.toDays(d.getTime() - r.getDate().getTime());

                            time = String.valueOf((int) Math.floor(difInDays)) + "d";

                        } else {

                            time = String.valueOf((int) Math.floor(difInHrs)) + "h";

                        }

                        b.append(time).append(" ago &c").append(r.getCreator().getName()).append(" - &7");

                        if (r.getDescription().length() < 15) {
                            b.append(r.getDescription());
                        } else {
                            b.append(r.getDescription().substring(0, 14)).append("...");
                        }

                        reqs.add(b.toString());

                    } else if (r.getStage().equals(Request.RequestStage.CLAIMED)) {

                        reqNum++;

                        StringBuilder b = new StringBuilder();

                        if (r.isGroup()) {

                            b.append("&7").append(r.getId()).append(" ").append("&3[&b&lGROUP&3] ").append(r.getType().color).append(r.getType().getName()).append(" &c[✫✫✫] &7");

                        } else {

                            b.append("&7").append(r.getId()).append(" ").append("&6[&e").append(User.getUser(r.getClaim().get(0)).getName()).append("&6] ").append(r.getType().color).append(r.getType().getName()).append(" &c[✫✫✫] &7");

                        }

                        Date d = new Date();

                        String time;

                        long difInHrs = TimeUnit.MILLISECONDS.toHours(d.getTime() - r.getDate().getTime());

                        if (difInHrs >= 24) {

                            long difInDays = TimeUnit.MILLISECONDS.toDays(d.getTime() - r.getDate().getTime());

                            time = String.valueOf((int) Math.floor(difInDays)) + "d";

                        } else {

                            time = String.valueOf((int) Math.floor(difInHrs)) + "h";

                        }

                        b.append(time).append(" ago &c").append(r.getCreator().getName()).append(" - &7");

                        if (r.getDescription().length() < 15) {
                            b.append(r.getDescription());
                        } else {
                            b.append(r.getDescription().substring(0, 14)).append("...");
                        }

                        reqs.add(b.toString());

                    } else if (r.getStage().equals(Request.RequestStage.REVIEW)) {

                        reqNum ++;

                        StringBuilder b = new StringBuilder();

                        b.append("&7").append(r.getId()).append(" ").append("&4[&c&lREVIEW&4] ").append(r.getType().color).append(r.getType().getName()).append(" &c[✫✫✫] &7");

                        Date d = new Date();

                        String time;

                        long difInHrs = TimeUnit.MILLISECONDS.toHours(d.getTime() - r.getDate().getTime());

                        if (difInHrs >= 24) {

                            long difInDays = TimeUnit.MILLISECONDS.toDays(d.getTime() - r.getDate().getTime());

                            time = String.valueOf((int) Math.floor(difInDays)) + "d";

                        } else {

                            time = String.valueOf((int) Math.floor(difInHrs)) + "h";

                        }

                        b.append(time).append(" ago &c").append(r.getCreator().getName()).append(" - &7");

                            if (r.getDescription().length() < 15) {
                                b.append(r.getDescription());
                            } else {
                                b.append(r.getDescription().substring(0, 14)).append("...");
                            }

                        reqs.add(b.toString());

                    }

                }

                ArrayList<Integer> medRequests = new ArrayList<>();
                medRequests.addAll(Request.medRequests.keySet());
                Collections.sort(medRequests);
                Collections.reverse(medRequests);

                for (Integer i : medRequests) {

                    Request r = Request.medRequests.get(i);

                    if (r.getStage().equals(Request.RequestStage.UNCLAIMED)) {

                        reqNum++;

                        StringBuilder b = new StringBuilder();

                        if (r.isGroup()) {

                            b.append("&7").append(r.getId()).append(" ").append("&3[&b&lGROUP&3] ").append(r.getType().color).append(r.getType().getName()).append(" &e[✫✫&7✫&e] &7");

                        } else {

                            b.append("&7").append(r.getId()).append(" ").append("&2[&a&lOPEN&2] ").append(r.getType().color).append(r.getType().getName()).append(" &e[✫✫&7✫&e] &7");

                        }

                        Date d = new Date();

                        String time;

                        long difInHrs = TimeUnit.MILLISECONDS.toHours(d.getTime() - r.getDate().getTime());

                        if (difInHrs >= 24) {

                            long difInDays = TimeUnit.MILLISECONDS.toDays(d.getTime() - r.getDate().getTime());

                            time = String.valueOf((int) Math.floor(difInDays)) + "d";

                        } else {

                            time = String.valueOf((int) Math.floor(difInHrs)) + "h";

                        }

                        b.append(time).append(" ago &c").append(r.getCreator().getName()).append(" - &7");

                        if (r.getDescription().length() < 15) {
                            b.append(r.getDescription());
                        } else {
                            b.append(r.getDescription().substring(0, 14)).append("...");
                        }

                        reqs.add(b.toString());

                    } else if (r.getStage().equals(Request.RequestStage.CLAIMED)) {

                        reqNum++;

                        StringBuilder b = new StringBuilder();

                        if (r.isGroup()) {

                            b.append("&7").append(r.getId()).append(" ").append("&3[&b&lGROUP&3] ").append(r.getType().color).append(r.getType().getName()).append(" &e[✫✫&7✫&e] &7");

                        } else {

                            b.append("&7").append(r.getId()).append(" ").append("&6[&e").append(User.getUser(r.getClaim().get(0)).getName()).append("&6] ").append(r.getType().color).append(r.getType().getName()).append(" &e[✫✫&7✫&e] &7");

                        }

                        Date d = new Date();

                        String time;

                        long difInHrs = TimeUnit.MILLISECONDS.toHours(d.getTime() - r.getDate().getTime());

                        if (difInHrs >= 24) {

                            long difInDays = TimeUnit.MILLISECONDS.toDays(d.getTime() - r.getDate().getTime());

                            time = String.valueOf((int) Math.floor(difInDays)) + "d";

                        } else {

                            time = String.valueOf((int) Math.floor(difInHrs)) + "h";

                        }

                        b.append(time).append(" ago &c").append(r.getCreator().getName()).append(" - &7");

                        if (r.getDescription().length() < 15) {
                            b.append(r.getDescription());
                        } else {
                            b.append(r.getDescription().substring(0, 14)).append("...");
                        }

                        reqs.add(b.toString());

                    } else if (r.getStage().equals(Request.RequestStage.REVIEW)) {

                        reqNum ++;

                        StringBuilder b = new StringBuilder();

                        b.append("&7").append(r.getId()).append(" ").append("&4[&c&lREVIEW&4] ").append(r.getType().color).append(r.getType().getName()).append(" &e[✫✫&7✫&e] &7");

                        Date d = new Date();

                        String time;

                        long difInHrs = TimeUnit.MILLISECONDS.toHours(d.getTime() - r.getDate().getTime());

                        if (difInHrs >= 24) {

                            long difInDays = TimeUnit.MILLISECONDS.toDays(d.getTime() - r.getDate().getTime());

                            time = String.valueOf((int) Math.floor(difInDays)) + "d";

                        } else {

                            time = String.valueOf((int) Math.floor(difInHrs)) + "h";

                        }

                        b.append(time).append(" ago &c").append(r.getCreator().getName()).append(" - &7");

                            if (r.getDescription().length() < 15) {
                                b.append(r.getDescription());
                            } else {
                                b.append(r.getDescription().substring(0, 14)).append("...");
                            }

                        reqs.add(b.toString());

                    }

                }

                ArrayList<Integer> lowRequests = new ArrayList<>();
                lowRequests.addAll(Request.lowRequests.keySet());
                Collections.sort(lowRequests);
                Collections.reverse(lowRequests);

                for (Integer i : lowRequests) {

                    Request r = Request.lowRequests.get(i);

                    if (r.getStage().equals(Request.RequestStage.UNCLAIMED)) {

                        reqNum++;

                        StringBuilder b = new StringBuilder();

                        if (r.isGroup()) {

                            b.append("&7").append(r.getId()).append(" ").append("&3[&b&lGROUP&3] ").append(r.getType().color).append(r.getType().getName()).append(" &a[✫&7✫✫&a] &7");

                        } else {

                            b.append("&7").append(r.getId()).append(" ").append("&2[&a&lOPEN&2] ").append(r.getType().color).append(r.getType().getName()).append(" &a[✫&7✫✫&a] &7");

                        }

                        Date d = new Date();

                        String time;

                        long difInHrs = TimeUnit.MILLISECONDS.toHours(d.getTime() - r.getDate().getTime());

                        if (difInHrs >= 24) {

                            long difInDays = TimeUnit.MILLISECONDS.toDays(d.getTime() - r.getDate().getTime());

                            time = String.valueOf((int) Math.floor(difInDays)) + "d";

                        } else {

                            time = String.valueOf((int) Math.floor(difInHrs)) + "h";

                        }

                        b.append(time).append(" ago &c").append(r.getCreator().getName()).append(" - &7");

                        if (r.getDescription().length() < 15) {
                            b.append(r.getDescription());
                        } else {
                            b.append(r.getDescription().substring(0, 14)).append("...");
                        }

                        reqs.add(b.toString());

                    } else if (r.getStage().equals(Request.RequestStage.CLAIMED)) {

                        reqNum++;

                        StringBuilder b = new StringBuilder();

                        if (r.isGroup()) {

                            b.append("&7").append(r.getId()).append(" ").append("&3[&b&lGROUP&3] ").append(r.getType().color).append(r.getType().getName()).append(" &a[✫&7✫✫&a] &7");

                        } else {

                            b.append("&7").append(r.getId()).append(" ").append("&6[&e").append(User.getUser(r.getClaim().get(0)).getName()).append("&6] ").append(r.getType().color).append(r.getType().getName()).append(" &a[✫&7✫✫&a] &7");

                        }

                        Date d = new Date();

                        String time;

                        long difInHrs = TimeUnit.MILLISECONDS.toHours(d.getTime() - r.getDate().getTime());

                        if (difInHrs >= 24) {

                            long difInDays = TimeUnit.MILLISECONDS.toDays(d.getTime() - r.getDate().getTime());

                            time = String.valueOf((int) Math.floor(difInDays)) + "d";

                        } else {

                            time = String.valueOf((int) Math.floor(difInHrs)) + "h";

                        }

                        b.append(time).append(" ago &c").append(r.getCreator().getName()).append(" - &7");

                        if (r.getDescription().length() < 15) {
                            b.append(r.getDescription());
                        } else {
                            b.append(r.getDescription().substring(0, 14)).append("...");
                        }

                        reqs.add(b.toString());

                    } else if (r.getStage().equals(Request.RequestStage.REVIEW)) {

                        reqNum ++;

                        StringBuilder b = new StringBuilder();

                        b.append("&7").append(r.getId()).append(" ").append("&4[&c&lREVIEW&4] ").append(r.getType().color).append(r.getType().getName()).append(" &a[✫&7✫✫&a] &7");

                        Date d = new Date();

                        String time;

                        long difInHrs = TimeUnit.MILLISECONDS.toHours(d.getTime() - r.getDate().getTime());

                        if (difInHrs >= 24) {

                            long difInDays = TimeUnit.MILLISECONDS.toDays(d.getTime() - r.getDate().getTime());

                            time = String.valueOf((int) Math.floor(difInDays)) + "d";

                        } else {

                            time = String.valueOf((int) Math.floor(difInHrs)) + "h";

                        }

                        b.append(time).append(" ago &c").append(r.getCreator().getName()).append(" - &7");

                            if (r.getDescription().length() < 15) {
                                b.append(r.getDescription());
                            } else {
                                b.append(r.getDescription().substring(0, 14)).append("...");
                            }

                        reqs.add(b.toString());

                    }

                }

                int max = (int) (int) Math.ceil(reqNum / 15.0);

                CenteredMessage.sendCenteredMessage(sender, "&a&l" + reqNum + " &2Open Requests &7[" + page + "/" + max + "]");

                if (page < max) {

                    for (int i = (page - 1) * 15; i < page * 15; i++) {

                        sendMessage(sender, reqs.get(i));

                    }

                } else {

                    for (int i = (page - 1) * 15; i < reqs.size(); i++) {

                        sendMessage(sender, reqs.get(i));

                    }

                }

            }

        } else {

            sendMessage(sender, "&c⚠ &4No Permission!");

        }


        return true;
    }

}
