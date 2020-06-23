package us.howtozombie.tickets.commands.player;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.howtozombie.karma.user.SubRank;
import us.howtozombie.karma.user.User;
import us.howtozombie.misc.Command;
import us.howtozombie.Main;
import us.howtozombie.tickets.util.Request;

import java.util.UUID;

public class Site extends Command {

    public Site() {
        super("site");
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

                    sendMessage(sender, "&c⚠ &4Invalid Syntax! &c/site [req]");
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

                ((Player) sender).teleport(r.getLoc());

            }

        }

        return true;
    }
}
