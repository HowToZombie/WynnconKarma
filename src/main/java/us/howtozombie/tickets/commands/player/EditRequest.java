package us.howtozombie.tickets.commands.player;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.howtozombie.Main;
import us.howtozombie.karma.user.SubRank;
import us.howtozombie.karma.user.User;
import us.howtozombie.misc.Command;
import us.howtozombie.tickets.util.Request;

import java.util.UUID;

public class EditRequest extends Command {

    public EditRequest() {
        super("editreq");
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

                    sendMessage(sender, "&c⚠ &4Invalid Syntax! &c/editreq [req] [loc | urg | desc | group]");
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

                if (r.getCreator().equals(u) || u.getAdminRank() > 0) {

                    if (args[1].equalsIgnoreCase("loc")) {

                        Location l = ((Player) sender).getLocation();

                        r.setLoc(l);

                        sendMessage(sender, "&2Edited location of request " + r.getId() + ".");

                    } else if (args[1].equalsIgnoreCase("urg")) {

                        if (args.length > 2) {

                            int urgency;

                            try {

                                urgency = Integer.valueOf(args[2]);

                                if (urgency < 1 || urgency > 3) {

                                    sendMessage(sender, "&c⚠ &4Invalid urgency!");
                                    return true;

                                }

                                r.setUrgency(urgency);

                                sendMessage(sender, "&2Edited urgency of request " + r.getId() + ".");

                            } catch (NumberFormatException e) {

                                sendMessage(sender, "&c⚠ &4Invalid urgency!");
                                return true;

                            }

                        } else {

                            sendMessage(sender, "&c⚠ &4Invalid Syntax! &c/editreq urg [urgency]");
                            return true;

                        }

                    } else if (args[1].equalsIgnoreCase("desc")) {

                        if (args.length > 2) {

                            StringBuilder b = new StringBuilder();

                            for (int i = 2; i < args.length; i++) {

                                b.append(args[i]);

                                if (i != args.length - 1) {
                                    b.append(" ");
                                }

                            }

                            r.setDescription(b.toString());

                            sendMessage(sender, "&2Edited description of request " + r.getId() + ".");

                        } else {

                            sendMessage(sender, "&c⚠ &4Invalid Syntax! &c/editreq desc [description]");
                            return true;

                        }

                    } else if (args[1].equalsIgnoreCase("group")) {

                        if (args.length > 2) {

                            if (args[2].equalsIgnoreCase("true")) {

                                r.setGroup(true);

                                sendMessage(sender, "&2Request " + r.getId() + " is now a group request.");

                            } else if (args[2].equalsIgnoreCase("false")) {

                                r.setGroup(false);

                                sendMessage(sender, "&2Request " + r.getId() + " is no longer a group request.");

                            } else {

                                sendMessage(sender, "&c⚠ &4Invalid Syntax! &c/editreq group [true/false]");

                            }

                        } else {

                            sendMessage(sender, "&c⚠ &4Invalid Syntax! &c/editreq group [true/false]");
                            return true;

                        }

                    }

                } else {

                    sendMessage(sender, "&c⚠ &4No Permission!");
                    return true;

                }

            }

        }

        return true;


    }

}
