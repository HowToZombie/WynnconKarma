package us.howtozombie.tickets.commands.req;

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

public class GroupReq extends Command {

    public GroupReq() {
        super("groupreq");
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

                if (args.length < 3) {

                    sendMessage(sender, "&c⚠ &4Invalid Syntax! &c/groupreq [type] [urgency] [request]");
                    return true;

                }

                String type = args[0];

                if (!type.equalsIgnoreCase("manager") && !type.equalsIgnoreCase("admin") &&
                        !type.equalsIgnoreCase("art") && !type.equalsIgnoreCase("artist") &&
                        !type.equalsIgnoreCase("build") && !type.equalsIgnoreCase("builder") &&
                        !type.equalsIgnoreCase("cmd") &&
                        !type.equalsIgnoreCase("dev") && !type.equalsIgnoreCase("developer") &&
                        !type.equalsIgnoreCase("gm")) {

                    sendMessage(sender, "&c⚠ &4Invalid Request Type!");
                    return true;

                }

                int urgency;

                try {

                    urgency = Integer.valueOf(args[1]);

                    if (urgency > 3 || urgency < 1) {

                        sendMessage(sender, "&c⚠ &4Invalid Urgency!");
                        return true;

                    }

                } catch (NumberFormatException e) {

                    sendMessage(sender, "&c⚠ &4Invalid Urgency!");
                    return true;

                }

                StringBuilder b = new StringBuilder();

                for (int i = 2; i < args.length; i++) {

                    b.append(args[i]);

                    if (i != args.length - 1) {
                        b.append(" ");
                    }

                }

                Main.requests ++;

                if (type.equalsIgnoreCase("admin")) {
                    Request.createNewRequest(Main.requests, "MANAGER", b.toString(), urgency, u, ((Player) sender).getLocation(), true);
                } else if (type.equalsIgnoreCase("art")) {
                    Request.createNewRequest(Main.requests, "ARTIST", b.toString(), urgency, u, ((Player) sender).getLocation(), true);
                } else if (type.equalsIgnoreCase("build")) {
                    Request.createNewRequest(Main.requests, "BUILDER", b.toString(), urgency, u, ((Player) sender).getLocation(), true);
                } else if (type.equalsIgnoreCase("dev")) {
                    Request.createNewRequest(Main.requests, "DEVELOPER", b.toString(), urgency, u, ((Player) sender).getLocation(), true);
                } else {
                    Request.createNewRequest(Main.requests, type.toUpperCase(), b.toString(), urgency, u, ((Player) sender).getLocation(), true);
                }

                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                        "&f" + u.getName() + " &7created &3group request " + Main.requests));

            }

        }

        return true;
    }

}
