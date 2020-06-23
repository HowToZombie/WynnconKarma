package us.howtozombie.tickets.commands.player;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.howtozombie.karma.user.User;
import us.howtozombie.misc.Command;
import us.howtozombie.Main;
import us.howtozombie.tickets.util.Request;

import java.util.UUID;

public class Reopen extends Command {

    public Reopen() {
        super("reopen");
    }

    @Override
    public boolean execute(CommandSender sender, String ss, String[] args) {

        if (sender instanceof Player) {

            User u = User.getUser((Player) sender);

            if (u.getAdminRank() > 0 || u.getUuid().equals(UUID.fromString("463925a6-bf21-4a47-bf64-7b42f399168b"))) {

                if (args.length < 1) {

                    sendMessage(sender, "&c⚠ &4Invalid Syntax! &c/done [req]");
                    return true;

                }

                int req = 0;

                try {

                    req = Integer.valueOf(args[0]);

                } catch (NumberFormatException e) {

                    sendMessage(sender, "&c⚠ &4Invalid Request!");
                    return true;

                }

                if (req <= 0 || req > Main.requests) {

                    sendMessage(sender, "&c⚠ &4Invalid Request!");
                    return true;

                }

                Request r = Request.requests.get(req);

                if (r.getStage().equals(Request.RequestStage.DONE) && u.getAdminRank() > 0) {

                    if (r.getClaim().size() != 0) {
                        r.setStage(Request.RequestStage.CLAIMED);
                    } else {
                        r.setStage(Request.RequestStage.UNCLAIMED);
                    }

                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                            "&f" + u.getName() + " &7reopened &2request " + r.getId()));

                } else if (r.getStage().equals(Request.RequestStage.REVIEW)) {

                    if (r.getClaim().contains(u.getUuid()) || u.getAdminRank() > 0) {

                        Request.reviewRequests.remove(r.getId());

                        if (r.getClaim().size() != 0) {
                            r.setStage(Request.RequestStage.CLAIMED);
                        } else {
                            r.setStage(Request.RequestStage.UNCLAIMED);
                        }

                        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                                "&f" + u.getName() + " &7reopened &2request " + r.getId()));

                    } else {

                        sendMessage(sender, "&c⚠ &4No permission!");
                        return true;
                    }

                } else {

                    sendMessage(sender, "&c⚠ &4That request is not closed!");
                    return true;

                }

            }

        }

        return true;
    }

}
