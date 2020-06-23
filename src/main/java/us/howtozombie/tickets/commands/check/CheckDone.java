package us.howtozombie.tickets.commands.check;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import us.howtozombie.karma.user.User;
import us.howtozombie.misc.Command;
import us.howtozombie.tickets.util.Request;
import us.howtozombie.tickets.util.CenteredMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class CheckDone extends Command {

    public CheckDone() {
        super("checkdone");
    }

    @Override
    public boolean execute(CommandSender sender, String ss, String[] args) {

        if (sender instanceof Player) {

            User u = User.getUser((Player) sender);

            if (u.getAdminRank() > 0 || sender instanceof ConsoleCommandSender ||
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

                ArrayList<Integer> requests = new ArrayList<>();
                requests.addAll(Request.requests.keySet());
                Collections.sort(requests);
                Collections.reverse(requests);

                for (Integer i : requests) {

                    Request r = Request.requests.get(i);

                    if (r.getStage().equals(Request.RequestStage.DONE)) {

                        reqNum ++;

                        StringBuilder b = new StringBuilder();

                        b.append("&7").append(r.getId()).append(" ").append("&4[&c&lCLOSED&4] ").append(r.getType().color).append(r.getType().getName());

                        if (r.getUrgency() == 1) {
                            b.append(" &a[✫&7✫✫&a] &7");
                        } else if (r.getUrgency() == 2) {
                            b.append(" &e[&e✫✫&7✫&e] &7");
                        } else if (r.getUrgency() == 3) {
                            b.append(" &c[&c✫✫✫&c] &7");
                        }

                        Date d = new Date();

                        String time;

                        long difInHrs = TimeUnit.MILLISECONDS.toHours(d.getTime() - r.getDate().getTime());

                        if (difInHrs >= 24) {

                            long difInDays = TimeUnit.MILLISECONDS.toDays(d.getTime() - r.getDate().getTime());

                            time = String.valueOf((int) (int) Math.floor(difInDays)) + "d";

                        } else {

                            time = String.valueOf((int) (int) Math.floor(difInHrs)) + "h";

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

                int max = (int) Math.ceil(reqNum / 15.0);

                CenteredMessage.sendCenteredMessage(sender, "&c&l" + reqNum + " &4Closed Requests &7[" + page + "/" + max + "]");

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
        }

        return true;
    }

}
