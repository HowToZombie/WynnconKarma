package us.howtozombie.tickets.commands.player;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.howtozombie.karma.user.SubRank;
import us.howtozombie.karma.user.User;
import us.howtozombie.misc.Command;
import us.howtozombie.Main;
import us.howtozombie.tickets.util.Request;

import java.util.UUID;

public class Done extends Command {

    public Done() {
        super("done");
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
                    u.getAdminRank() > 0 ||
                    u.getUuid().equals(UUID.fromString("463925a6-bf21-4a47-bf64-7b42f399168b"))) {

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

                if (r.getStage().equals(Request.RequestStage.CLAIMED)) {

                    if (r.getClaim().contains(u.getUuid()) || r.getCreator().getUuid().equals(u.getUuid()) || u.getAdminRank() > 0) {

                        r.setStage(Request.RequestStage.REVIEW);
                        Request.reviewRequests.put(r.getId(), r);
                        sendMessage(sender, "&7Nice work! A manager will look at your request soon.");
                        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                                "&f" + u.getName() + " &7finished &2request " + r.getId()));

                        if (args.length > 1) {

                            StringBuilder b = new StringBuilder();

                            for (int i = 1; i < args.length; i++) {

                                b.append(args[i]);

                                if (i != args.length - 1) {

                                    b.append(" ");

                                }

                            }

                            r.setComments(b.toString());

                        }

                    } else {

                        sendMessage(sender, "&c⚠ &4No Permission!");
                        return true;

                    }

                } else {
                    sendMessage(sender, "&c⚠ &4That request is not claimable!");
                    return true;

                }

            }

        }

        return true;
    }

}
