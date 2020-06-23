package us.howtozombie.karma.commands.logs;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import us.howtozombie.karma.user.User;
import us.howtozombie.misc.Command;
import us.howtozombie.Main;
import us.howtozombie.karma.user.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class LogCommand extends Command {

    public LogCommand() {
        super("log");
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        
        if (sender.hasPermission("karma.log") || sender instanceof ConsoleCommandSender) {

            if (args.length < 1) {
                sendMessage(sender, "&c⚠ &4Invalid Syntax! &c/log [player]");
            } else {

                User u = User.getUser(args[0]);

                if (u == null) {
                    sendMessage(sender, "&c⚠ &4Invalid Player!");
                    return true;
                }

                HashMap<Integer, String> logs = Log.getLogs(args[0]);

                sendMessage(sender, "&7[======= Logs for " + args[0] + " =======]");

                if (logs != null) {

                    ArrayList<Integer> logSort = new ArrayList<>();
                    logSort.addAll(logs.keySet());
                    Collections.sort(logSort);

                    for (Integer i : logSort) {
                        sendMessage(sender, logs.get(i));
                    }
                }

            }

        } else {
            sendMessage(sender, "&c⚠ &4Invalid Command!");
        }




        return true;
    }
}
