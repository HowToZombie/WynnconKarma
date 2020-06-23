package us.howtozombie;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.plugin.java.JavaPlugin;
import us.howtozombie.karma.commands.karma.*;
import us.howtozombie.karma.commands.logs.LogCommand;
import us.howtozombie.karma.commands.logs.ReloadCommand;
import us.howtozombie.karma.commands.logs.StatsCommand;
import us.howtozombie.karma.commands.ranks.*;
import us.howtozombie.karma.commands.tags.*;
import us.howtozombie.karma.user.Database;
import us.howtozombie.karma.user.SubRank;
import us.howtozombie.karma.listeners.*;
import us.howtozombie.karma.user.User;
import us.howtozombie.misc.Command;
import us.howtozombie.misc.NightVisionCommand;
import us.howtozombie.misc.PingCommand;
import us.howtozombie.tickets.util.Request;
import us.howtozombie.tickets.commands.check.*;
import us.howtozombie.tickets.commands.player.*;
import us.howtozombie.tickets.commands.req.*;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class Main extends JavaPlugin {

    private static Database db;

    public static String host, database, username, password;
    public static int port;

    public static boolean databaseEnabled;

    public SimpleCommandMap map;

    private FileConfiguration databaseConfig = null;
    private File databaseFile = null;

    static FileConfiguration config = null;
    static File f = null;
    static File dataFolder = null;

    public static int requests;

    @Override
    public void onEnable() {

        if (!this.getDataFolder().exists())
            this.getDataFolder().mkdir();

        if (!getConfig().getBoolean("enabled")) {
            getServer().getPluginManager().disablePlugin(getPlugin(Main.class));
        } else {

            dataFolder = getDataFolder();

            saveDefaultConfig();

            getServer().getPluginManager().registerEvents(new Chat(), this);
            getServer().getPluginManager().registerEvents(new Login(), this);

            databaseEnabled = getConfig().getBoolean("database");

            if (databaseEnabled) {

                try {
                    databaseConfig = getDatabaseFile();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                saveDefaultDatabase();

                host = databaseConfig.getString("host");
                port = databaseConfig.getInt("port");
                database = databaseConfig.getString("database");
                username = databaseConfig.getString("username");
                password = databaseConfig.getString("password");


                db = new Database();

            }

            //check config for subranks
            loadRanks();

            registerCommands(
                    new AdminKarma(),
                    new AdminRank(),
                    new AdminTag(),
                    new ArtKarma(),
                    new ArtRank(),
                    new ArtTag(),
                    new BuildKarma(),
                    new BuildRank(),
                    new BuildTag(),
                    new CMDKarma(),
                    new CMDRank(),
                    new CMDTag(),
                    new DevKarma(),
                    new DevRank(),
                    new DevTag(),
                    new GMKarma(),
                    new GMRank(),
                    new GMTag(),
                    new LogCommand(),
                    new StatsCommand(),
                    new ReloadCommand(),
                    new Check(),
                    new CheckAll(),
                    new CheckArt(),
                    new CheckBuild(),
                    new CheckCmd(),
                    new CheckDev(),
                    new CheckGM(),
                    new CheckDone(),
                    new CheckManager(),
                    new Review(),
                    new Claim(),
                    new Close(),
                    new Done(),
                    new Site(),
                    new Unclaim(),
                    new Reopen(),
                    new ArtReq(),
                    new BuildReq(),
                    new CmdReq(),
                    new DevReq(),
                    new GMReq(),
                    new ManagerReq(),
                    new NightVisionCommand(),
                    new PingCommand(),
                    new EditRequest(),
                    new GroupReq());

            if (!databaseEnabled) {

                File dFile = new File(getDataFolder(), "data.yml");
                if (dFile.exists()) {

                    YamlConfiguration data = YamlConfiguration.loadConfiguration(dFile);

                    ConfigurationSection users = data.getConfigurationSection("users");

                    for (String s : users.getKeys(false)) {

                        UUID uuid = UUID.fromString(s);

                        User u = new User(uuid);

                    }

                }

            }

            loadRequests();

        }
    }

    @Override
    public void onDisable() {

        if (f == null) {
            f = new File(getDataFolder() + "/requests", "requests.yml");
        }

        config = YamlConfiguration.loadConfiguration(f);

        config.set("reqAmt", requests);

        try {
            config.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void loadRanks() {
        FileConfiguration config = getConfig();

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
    }

    public void registerCommands(Command... cmds) {
            if (map == null) {
                map = ((CraftServer) getServer()).getCommandMap();
            }
            map.registerAll("WynnconKarma", Arrays.asList(cmds));
    }

    public static Database getDatabase() {
        return db;
    }

    public void reloadDatabaseFile() throws UnsupportedEncodingException {
        if (databaseFile == null) {
            databaseFile = new File(getDataFolder(), "database.yml");
        }
        databaseConfig = YamlConfiguration.loadConfiguration(databaseFile);

        // Look for defaults in the jar
        Reader defConfigStream = new InputStreamReader(this.getResource("database.yml"), "UTF8");
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            databaseConfig.setDefaults(defConfig);
        }
    }

    public FileConfiguration getDatabaseFile() throws UnsupportedEncodingException {
        if (databaseConfig == null) {
            reloadDatabaseFile();
        }
        return databaseConfig;
    }

    public void saveDatabaseFile() {
        if (databaseConfig == null || databaseFile == null) {
            return;
        }
        try {
            getDatabaseFile().save(databaseFile);
        } catch (IOException ex) {
            getLogger().log(Level.SEVERE, "Could not save config to " + databaseFile, ex);
        }
    }

    public void saveDefaultDatabase() {
        if (databaseFile == null) {
            databaseFile = new File(getDataFolder(), "database.yml");
        }
        if (!databaseFile.exists()) {
            this.saveResource("database.yml", false);
        }
    }

    public void loadRequests() {

        if (f == null) {

            f = new File(getDataFolder() + "/requests", "requests.yml");

        }

        config = YamlConfiguration.loadConfiguration(f);

        requests = config.getInt("reqAmt");

        Map<String, Object> values = config.getConfigurationSection("requests").getValues(false);

        for (String s : values.keySet()) {

            String path = "requests." + s;
            int id = Integer.valueOf(s);
            String type = config.getString(path + ".type");
            String stage = config.getString(path + ".stage");
            String description = config.getString(path + ".description");
            int urgency = config.getInt(path + ".urgency");
            long time = config.getLong(path + ".date");
            Date d = new Date(time);
            User creator = null;
            if (config.getString(path + ".creator") != null) {
                creator = User.getUser(UUID.fromString(config.getString(path + ".creator")));
            }
            ArrayList<UUID> claim = new ArrayList<>();
            if (config.getStringList(path + ".claim") != null) {
                claim = config.getStringList(path + ".claim").stream().map(UUID::fromString).collect(Collectors.toCollection(ArrayList::new));
            }
            User reviewer = null;
            if (config.getString(path + ".reviewer") != null) {
                reviewer = User.getUser(UUID.fromString(config.getString(path + ".reviewer")));
            }
            double x = config.getDouble(path + ".locX");
            double y = config.getDouble(path + ".locY");
            double z = config.getDouble(path + ".locZ");
            if (getServer().getWorld(config.getString(path + ".locWorld")) == null) {
                getServer().createWorld(new WorldCreator(config.getString(path + ".locWorld")));
            }
            World w = getServer().getWorld(config.getString(path + ".locWorld"));
            float pitch = config.getLong(path + ".locPitch");
            float yaw = config.getLong(path + ".locYaw");
            Location loc = new Location(w, x, y, z, yaw, pitch);
            String comments = null;
            if (config.getString(path + ".comments") != null) {
                comments = config.getString(path + ".comments");
            }
            boolean group = config.getBoolean(path + ".group");
            boolean pinned = config.getBoolean(path + ".pinned");

            new Request(id, type, stage, description, urgency, d, creator, claim, reviewer, loc, comments, group, pinned);

        }

    }

    public static void updateRequest(Request r) {

        if (f == null) {

            f = new File(dataFolder + "/requests", "requests.yml");

        }

        config = YamlConfiguration.loadConfiguration(f);

        config.set("reqAmt", requests);

        int id = r.getId();
        String path = "requests." + id;

        config.set(path + ".type", r.getType().getName());
        config.set(path + ".stage", r.getStage().toString());
        config.set(path + ".description", r.getDescription());
        config.set(path + ".urgency", r.getUrgency());
        config.set(path + ".date", r.getDate().getTime());
        config.set(path + ".creator", r.getCreator().getUuid().toString());
        if (r.getClaim() != null) {
            ArrayList<String> claims = r.getClaim().stream().map(UUID::toString).collect(Collectors.toCollection(ArrayList::new));
            config.set(path + ".claim", claims);
        }
        if (r.getReviewer() != null) {
            config.set(path + ".reviewer", r.getReviewer().getUuid().toString());
        }
        config.set(path + ".locX", r.getLoc().getX());
        config.set(path + ".locY", r.getLoc().getY());
        config.set(path + ".locZ", r.getLoc().getZ());
        config.set(path + ".locWorld", r.getLoc().getWorld().getName());
        config.set(path + ".locPitch", r.getLoc().getPitch());
        config.set(path + ".locYaw", r.getLoc().getYaw());
        if (r.getComments() != null) {
            config.set(path + ".comments", r.getComments());
        }
        config.set(path + ".group", r.isGroup());
        config.set(path + ".pinned", r.isPinned());

        try {
            config.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
