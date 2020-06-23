package us.howtozombie.karma.commands.logs;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import us.howtozombie.karma.user.SubRank;
import us.howtozombie.misc.Command;
import us.howtozombie.Main;

import java.util.ArrayList;
import java.util.Map;

public class ReloadCommand extends Command {

    public ReloadCommand() {
        super("karmareload");
    }

    @Override
    public boolean execute(CommandSender sender, String st, String[] args) {

        if (sender.hasPermission("karma.reload") || sender instanceof ConsoleCommandSender) {

            sendMessage(sender, "&cReloading config...");

            Main.getPlugin(Main.class).reloadConfig();
            FileConfiguration config = Main.getPlugin(Main.class).getConfig();

            Main.databaseEnabled = config.getBoolean("database");

            int ranks = 0;
            Map<String, Object> values = config.getConfigurationSection("subranks").getValues(false);
            for (String s : values.keySet()) {
                ranks++;
                String path = "subranks." + s;
                String tag = config.getString(path + ".tag");
                String shortTag = config.getString(path + ".shortTag");
                boolean canRankup = config.getBoolean(path + ".canRankup");
                int rankup;
                int rankupTo;
                if (canRankup) {
                    rankup = config.getInt(path + ".rankup");
                    rankupTo = config.getInt(path + ".rankupTo");
                } else {
                    rankup = 0;
                    rankupTo = 0;
                }
                boolean canRankdown = config.getBoolean(path + ".canRankdown");
                int rankdown;
                int rankdownTo;
                if (canRankdown) {
                    rankdown = config.getInt(path + ".rankdown");
                    rankdownTo = config.getInt(path + "rankdownTo");
                } else {
                    rankdown = 0;
                    rankdownTo = 0;
                }
                int permissions = config.getInt(path + ".permissions");
                boolean hasNumbers = config.getBoolean(path + ".subcategoriesEnabled");
                ArrayList<Integer> numbers = new ArrayList<>();
                if (hasNumbers) {
                    values = config.getConfigurationSection(path + ".subcategories").getValues(false);
                    for (String s1 : values.keySet()) {
                        numbers.add(config.getInt(path + ".subcategories." + s1));
                    }
                }
                new SubRank(Integer.parseInt(s), tag, shortTag, canRankup, rankup, rankupTo, canRankdown, rankdown, rankdownTo, permissions, hasNumbers, numbers);
            }
            SubRank.rankNum = ranks;

            sendMessage(sender, "&cReloaded config.");

        } else {

            sendMessage(sender, "&c⚠ &4Invalid Command!");

        }

        return true;
    }
}
