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

public class CmdReq extends Command {

    public CmdReq() {
        super("cmdreq");
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

                if (args.length < 2) {

                    sendMessage(sender, "&c⚠ &4Invalid Syntax! &c/cmdreq [urgency] [request]");
                    return true;

                }

                int urgency;

                try {

                    urgency = Integer.valueOf(args[0]);

                    if (urgency > 3 || urgency < 1) {

                        sendMessage(sender, "&c⚠ &4Invalid Urgency!");
                        return true;

                    }

                } catch (NumberFormatException e) {

                    sendMessage(sender, "&c⚠ &4Invalid Urgency!");
                    return true;

                }

                StringBuilder b = new StringBuilder();

                for (int i = 1; i < args.length; i++) {

                    b.append(args[i]);

                    if (i != args.length - 1) {
                        b.append(" ");
                    }

                }

                Main.requests ++;

                Request.createNewRequest(Main.requests, "CMD", b.toString(), urgency, u, ((Player) sender).getLocation());

                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                        "&f" + u.getName() + " &7created &2request " + Main.requests));

            }

        }

        return true;
    }
    
}
