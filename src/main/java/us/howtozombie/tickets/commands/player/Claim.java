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

public class Claim extends Command {

    public Claim() {
        super("claim");
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

                    sendMessage(sender, "&c⚠ &4Invalid Syntax! &c/claim [req]");
                    return true;

                }

                int req = 0;

                try {

                    req = Integer.valueOf(args[0]);

                } catch (NumberFormatException e) {

                    sendMessage(sender, "&c⚠ &4Invalid Request!");
                    return true;

                }

                if (req == 0 || req > Main.requests) {

                    sendMessage(sender, "&c⚠ &4Invalid Request!");
                    return true;

                }

                Request r = Request.requests.get(req);

                if (r.getStage().equals(Request.RequestStage.UNCLAIMED) || (r.isGroup() && !r.getStage().equals(Request.RequestStage.REVIEW) && !r.getStage().equals(Request.RequestStage.DONE))) {

                    if ((r.getType().hasRank(u) || u.getAdminRank() > 0) && !r.getClaim().contains(u.getUuid())) {

                        if (r.isGroup()) {
                            r.addClaim(u.getUuid());
                            r.setStage(Request.RequestStage.CLAIMED);
                            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                                    "&f" + u.getName() + " &7joined &2request " + r.getId()));
                        } else {
                            r.setClaim(u.getUuid());
                            r.setStage(Request.RequestStage.CLAIMED);
                            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                                    "&f" + u.getName() + " &7claimed &2request " + r.getId()));
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
