package us.howtozombie.karma.user;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import us.howtozombie.Main;
import us.howtozombie.tickets.util.RomanNumeral;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.UUID;

public class User {

    private String buildTag;
    private String cmdTag;
    private String devTag;
    private String artTag;
    private String adminTag;
    private String gmTag;

    private int adminRank;
    private int cmdRank;
    private int devRank;
    private int artRank;
    private int buildRank;
    private int gmRank;

    private int adminNum;
    private int cmdNum;
    private int devNum;
    private int artNum;
    private int buildNum;
    private int gmNum;

    private int buildKarma;
    private int cmdKarma;
    private int devKarma;
    private int artKarma;
    private int adminKarma;
    private int gmKarma;

    private boolean hasAdminTag;
    private boolean hasCmdTag;
    private boolean hasDevTag;
    private boolean hasArtTag;
    private boolean hasBuildTag;
    private boolean hasGMTag;
    
    private String name;
    private UUID uuid;
    private Player p;
    
    public static HashMap<UUID, User> users = new HashMap<>();
    public static HashMap<String, User> usersName = new HashMap<>();

    public User(Player p) {
        
        this.p = p;
        name = p.getName();
        uuid = p.getUniqueId();

        if (Main.databaseEnabled) {
            loadDB();
        } else {
            loadFile();
        }

        users.put(uuid, this);
        usersName.put(name, this);

    }

    public User(String st) {
        
        boolean b = false;
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getName().equalsIgnoreCase(st)) {
                this.p = p;
                this.uuid = this.p.getUniqueId();
                b = true;
            }
        }
        if (!b) {
            this.uuid = Bukkit.getOfflinePlayer(st).getUniqueId();
            this.p = Bukkit.getOfflinePlayer(uuid).getPlayer();
        }
        name = st;

        if (Main.databaseEnabled) {
            loadDB();
        } else {
            loadFile();
        }

        users.put(uuid, this);
        usersName.put(name, this);

    }

    public User(UUID u) {

        boolean b = false;
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getUniqueId().toString().equalsIgnoreCase(u.toString())) {
                this.p = p;
                b = true;
            }
        }
        if (!b) {
            this.p = Bukkit.getOfflinePlayer(u).getPlayer();
        }
        uuid = u;

        if (Main.databaseEnabled) {
            loadDB();
        } else {
            loadFile();
        }

        users.put(uuid, this);
        usersName.put(name, this);

    }
    
    private void loadDB() {

            Connection connection = null;
            PreparedStatement statement = null;
            ResultSet result = null;

            try {

                connection = Main.getDatabase().getConnection();
                statement = connection.prepareStatement("SELECT * FROM users WHERE UUID = '" + uuid.toString() + "'");
                result = statement.executeQuery();

                boolean exists = false;

                while (result.next()) {
                    exists = true;
                    adminRank = result.getInt("ADMIN_RANK");
                    adminKarma = result.getInt("ADMIN_KARMA");
                    adminNum = result.getInt("ADMIN_NUM");
                    if (result.getBoolean("HAS_ADMIN_TAG")) {
                        hasAdminTag = true;
                        adminTag = result.getString("ADMIN_TAG");
                    } else {
                        hasAdminTag = false;
                    }

                    devRank = result.getInt("DEV_RANK");
                    devNum = result.getInt("DEV_NUM");
                    devKarma = result.getInt("DEV_KARMA");
                    if (result.getBoolean("HAS_DEV_TAG")) {
                        hasDevTag = true;
                        devTag = result.getString("DEV_TAG");
                    } else {
                        hasDevTag = false;
                    }

                    cmdRank = result.getInt("CMD_RANK");
                    cmdNum = result.getInt("CMD_NUM");
                    cmdKarma = result.getInt("CMD_KARMA");
                    if (result.getBoolean("HAS_CMD_TAG")) {
                        hasCmdTag = true;
                        cmdTag = result.getString("CMD_TAG");
                    } else {
                        hasCmdTag = false;
                    }

                    buildRank = result.getInt("BUILD_RANK");
                    buildNum = result.getInt("BUILD_NUM");
                    buildKarma = result.getInt("BUILD_KARMA");
                    if (result.getBoolean("HAS_BUILD_TAG")) {
                        hasBuildTag = true;
                        buildTag = result.getString("BUILD_TAG");
                    } else {
                        hasBuildTag = false;
                    }

                    artRank = result.getInt("ART_RANK");
                    artNum = result.getInt("ART_NUM");
                    artKarma = result.getInt("ART_KARMA");
                    if (result.getBoolean("HAS_ART_TAG")) {
                        hasArtTag = true;
                        artTag = result.getString("ART_TAG");
                    } else {
                        hasArtTag = false;
                    }

                    gmRank = result.getInt("GM_RANK");
                    gmNum = result.getInt("GM_NUM");
                    gmKarma = result.getInt("GM_KARMA");
                    if (result.getBoolean("HAS_GM_TAG")) {
                        hasGMTag = true;
                        gmTag = result.getString("GM_TAG");
                    } else {
                        hasGMTag = false;
                    }

                    if (name == null) {
                        name = result.getString("NAME");
                    }

                    statement = connection.prepareStatement("UPDATE users SET NAME = '" + name + "' WHERE UUID = '" + uuid.toString() + "';");
                    statement.executeUpdate();

                }

                if (!exists) {
                    statement = connection.prepareStatement("INSERT INTO users (UUID, NAME) VALUES ('" + uuid.toString() + "', '" + name + "');");
                    statement.executeUpdate();
                }

                users.put(uuid, this);

                connection.close();

            } catch (SQLException e) {
                e.printStackTrace();
            } /*catch (NullPointerException e) {
            try {
                connection = Main.getDatabase().getConnection();
                statement = connection.prepareStatement("INSERT INTO users (UUID, NAME) VALUES ('" + uuid.toString() + "', '" + name + "');");
                statement.executeUpdate();
            } catch (SQLException e1) {
                e.printStackTrace();
            }
        }*/
    }

    private void loadFile() {

        File f = new File(Main.getPlugin(Main.class).getDataFolder() + "/data.yml");

        YamlConfiguration config = YamlConfiguration.loadConfiguration(f);

        String path = "users." + uuid.toString();

        if (config.contains(path)) {
            adminRank = config.getInt(path + ".admin.rank");
            adminNum = config.getInt(path + ".admin.rankNum");
            adminKarma = config.getInt(path + ".admin.karma");
            if (config.getBoolean(path + ".admin.hasTag")) {
                hasAdminTag = true;
                adminTag = config.getString(path + ".admin.tag");
            } else {
                hasAdminTag = false;
            }

            devRank = config.getInt(path + ".developer.rank");
            devNum = config.getInt(path + ".developer.rankNum");
            devKarma = config.getInt(path + ".developer.karma");
            if (config.getBoolean(path + ".developer.hasTag")) {
                hasDevTag = true;
                devTag = config.getString(path + ".developer.tag");
            } else {
                hasDevTag = false;
            }

            cmdRank = config.getInt(path + ".cmd.rank");
            cmdNum = config.getInt(path + ".cmd.rankNum");
            cmdKarma = config.getInt(path + ".cmd.karma");
            if (config.getBoolean(path + ".cmd.hasTag")) {
                hasCmdTag = true;
                cmdTag = config.getString(path + ".cmd.tag");
            } else {
                hasCmdTag = false;
            }

            buildRank = config.getInt(path + ".builder.rank");
            buildNum = config.getInt(path + ".builder.rankNum");
            buildKarma = config.getInt(path + ".builder.karma");
            if (config.getBoolean(path + ".builder.hasTag")) {
                hasBuildTag = true;
                buildTag = config.getString(path + ".builder.tag");
            } else {
                hasBuildTag = false;
            }

            artRank = config.getInt(path + ".artist.rank");
            artNum = config.getInt(path + ".artist.rankNum");
            artKarma = config.getInt(path + ".artist.karma");
            if (config.getBoolean(path + ".artist.hasTag")) {
                hasArtTag = true;
                artTag = config.getString(path + ".artist.tag");
            } else {
                hasArtTag = false;
            }

            gmRank = config.getInt(path + ".gm.rank");
            gmNum = config.getInt(path + ".gm.rankNum");
            gmKarma = config.getInt(path + ".gm.karma");
            if (config.getBoolean(path + ".gm.hasTag")) {
                hasGMTag = true;
                gmTag = config.getString(path + ".gm.tag");
            } else {
                hasGMTag = false;
            }

            if (name == null) {
                name = config.getString(path + ".name");
            } else {
                config.set(path + ".name", name);
                try {
                    config.save(f);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } else {

            config.set(path + ".admin.rank", 0);
            config.set(path + ".admin.rankNum", 0);
            config.set(path + ".admin.karma", 0);
            config.set(path + ".admin.hasTag", false);
            config.set(path + ".admin.tag", "");

            config.set(path + ".developer.rank", 0);
            config.set(path + ".developer.rankNum", 0);
            config.set(path + ".developer.karma", 0);
            config.set(path + ".developer.hasTag", false);
            config.set(path + ".developer.tag", "");

            config.set(path + ".cmd.rank", 0);
            config.set(path + ".cmd.rankNum", 0);
            config.set(path + ".cmd.karma", 0);
            config.set(path + ".cmd.hasTag", false);
            config.set(path + ".cmd.tag", "");

            config.set(path + ".builder.rank", 0);
            config.set(path + ".builder.rankNum", 0);
            config.set(path + ".builder.karma", 0);
            config.set(path + ".builder.hasTag", false);
            config.set(path + ".builder.tag", "");

            config.set(path + ".artist.rank", 0);
            config.set(path + ".artist.rankNum", 0);
            config.set(path + ".artist.karma", 0);
            config.set(path + ".artist.hasTag", false);
            config.set(path + ".artist.tag", "");

            config.set(path + ".gm.rank", 0);
            config.set(path + ".gm.rankNum", 0);
            config.set(path + ".gm.karma", 0);
            config.set(path + ".gm.hasTag", false);
            config.set(path + ".gm.tag", "");

            adminRank = 0;
            adminNum = 0;
            adminKarma = 0;
            hasAdminTag = false;
            adminTag = "";

            artRank = 0;
            artNum = 0;
            artKarma = 0;
            hasArtTag = false;
            artTag = "";

            buildRank = 0;
            buildNum = 0;
            buildKarma = 0;
            hasBuildTag = false;
            buildTag = "";

            cmdRank = 0;
            cmdNum = 0;
            cmdKarma = 0;
            hasCmdTag = false;
            cmdTag = "";

            devRank = 0;
            devNum = 0;
            devKarma = 0;
            hasDevTag = false;
            devTag = "";

            gmRank = 0;
            gmNum = 0;
            gmKarma = 0;
            hasGMTag = false;
            gmTag = "";

            if (name == null) {
                config.set(path + ".name", "");
            } else {
                config.set(path + ".name", name);
            }

            try {
                config.save(f);
            } catch(IOException e) {
                e.printStackTrace();
            }
            
        }

    }

    public static User getUser(String name) {

        return usersName.getOrDefault(name, null);

    }

    public static User getUser(UUID uuid) {

        return users.getOrDefault(uuid, null);

    }

    public static User getUser(Player p) {

        return users.getOrDefault(p.getUniqueId(), null);

    }

    public int getAdminRank() {
        return adminRank;
    }

    public int getArtRank() {
        return artRank;
    }

    public int getBuildRank() {
        return buildRank;
    }

    public int getCmdRank() {
        return cmdRank;
    }

    public int getDevRank() {
        return devRank;
    }
    
    public int getGMRank() {
        return gmRank;
    }

    public String getAdminTag() {
        return adminTag;
    }

    public String getBuildTag() {
        return buildTag;
    }

    public String getGMTag() {
        return gmTag;
    }

    public int getAdminNum() {
        return adminNum;
    }

    public int getArtNum() {
        return artNum;
    }

    public int getGMNum() {
        return gmNum;
    }

    public int getBuildKarma() {
        return buildKarma;
    }

    public int getGMKarma() {
        return gmKarma;
    }

    public int getBuildNum() {
        return buildNum;
    }

    public int getArtKarma() {
        return artKarma;
    }

    public int getCmdKarma() {
        return cmdKarma;
    }

    public int getCmdNum() {
        return cmdNum;
    }

    public int getDevNum() {
        return devNum;
    }

    public int getDevKarma() {
        return devKarma;
    }

    public String getArtTag() {
        return artTag;
    }

    public String getCmdTag() {
        return cmdTag;
    }

    public String getDevTag() {
        return devTag;
    }

    public void setAdminRank(int adminRank) {
        this.adminRank = adminRank;

        if (Main.databaseEnabled) {
            Connection connection = null;
            PreparedStatement statement = null;

            try {
                connection = Main.getDatabase().getConnection();
                statement = connection.prepareStatement("UPDATE users SET ADMIN_RANK = " + adminRank + " WHERE UUID = '" + uuid.toString() + "';");
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            File f = new File(Main.getPlugin(Main.class).getDataFolder() + "/data.yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(f);

            config.set("users." + uuid.toString() + ".admin.rank", adminRank);

            try {
                config.save(f);
            } catch(IOException e) {
                e.printStackTrace();
            }
        }

        users.put(uuid, this);
        usersName.put(name, this);
    }

    public void setAdminNum(int adminNum) {
        this.adminNum = adminNum;

        if (Main.databaseEnabled) {
            Connection connection = null;
            PreparedStatement statement = null;

            try {
                connection = Main.getDatabase().getConnection();
                statement = connection.prepareStatement("UPDATE users SET ADMIN_NUM = " + adminNum + " WHERE UUID = '" + uuid.toString() + "';");
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            File f = new File(Main.getPlugin(Main.class).getDataFolder() + "/data.yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(f);

            config.set("users." + uuid.toString() + ".admin.rankNum", adminNum);

            try {
                config.save(f);
            } catch(IOException e) {
                e.printStackTrace();
            }
        }

        users.put(uuid, this);
        usersName.put(name, this);
    }

    public void setAdminTag(String adminTag) {
        this.adminTag = adminTag;
        this.hasAdminTag = true;

        if (Main.databaseEnabled) {
            Connection connection = null;
            PreparedStatement statement = null;

            try {
                connection = Main.getDatabase().getConnection();
                statement = connection.prepareStatement("UPDATE users SET HAS_ADMIN_TAG = true, ADMIN_TAG = '" + adminTag + "' WHERE UUID = '" + uuid.toString() + "';");
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            File f = new File(Main.getPlugin(Main.class).getDataFolder() + "/data.yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(f);

            config.set("users." + uuid.toString() + ".admin.tag", adminTag);
            config.set("users." + uuid.toString() + ".admin.hasTag", true);

            try {
                config.save(f);
            } catch(IOException e) {
                e.printStackTrace();
            }
        }

        users.put(uuid, this);
        usersName.put(name, this);
    }

    public void removeAdminTag() {
        this.adminTag = null;
        this.hasAdminTag = false;

        if (Main.databaseEnabled) {
            Connection connection = null;
            PreparedStatement statement = null;

            try {
                connection = Main.getDatabase().getConnection();
                statement = connection.prepareStatement("UPDATE users SET HAS_ADMIN_TAG = false, ADMIN_TAG = null WHERE UUID = '" + uuid.toString() + "';");
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            File f = new File(Main.getPlugin(Main.class).getDataFolder() + "/data.yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(f);

            config.set("users." + uuid.toString() + ".admin.tag", "");
            config.set("users." + uuid.toString() + ".admin.hasTag", false);

            try {
                config.save(f);
            } catch(IOException e) {
                e.printStackTrace();
            }
        }

        users.put(uuid, this);
        usersName.put(name, this);
    }

    public void setAdminKarma(int adminKarma) {
        this.adminKarma = adminKarma;

        if (Main.databaseEnabled) {
            Connection connection = null;
            PreparedStatement statement = null;

            try {
                connection = Main.getDatabase().getConnection();
                statement = connection.prepareStatement("UPDATE users SET ADMIN_KARMA = " + adminKarma + " WHERE UUID = '" + uuid.toString() + "';");
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            File f = new File(Main.getPlugin(Main.class).getDataFolder() + "/data.yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(f);

            config.set("users." + uuid.toString() + ".admin.karma", adminKarma);

            try {
                config.save(f);
            } catch(IOException e) {
                e.printStackTrace();
            }
        }

        users.put(uuid, this);
        usersName.put(name, this);
    }

    public void setArtRank(int artRank) {
        this.artRank = artRank;

        if (Main.databaseEnabled) {
            Connection connection = null;
            PreparedStatement statement = null;

            try {
                connection = Main.getDatabase().getConnection();
                statement = connection.prepareStatement("UPDATE users SET ART_RANK = " + artRank + " WHERE UUID = '" + uuid.toString() + "';");
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            File f = new File(Main.getPlugin(Main.class).getDataFolder() + "/data.yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(f);

            config.set("users." + uuid.toString() + ".artist.rank", artRank);

            try {
                config.save(f);
            } catch(IOException e) {
                e.printStackTrace();
            }
        }

        users.put(uuid, this);
        usersName.put(name, this);
    }

    public void setArtTag(String artTag) {
        this.artTag = artTag;
        this.hasArtTag = true;

        if (Main.databaseEnabled) {
            Connection connection = null;
            PreparedStatement statement = null;

            try {
                connection = Main.getDatabase().getConnection();
                statement = connection.prepareStatement("UPDATE users SET HAS_ART_TAG = true, ART_TAG = '" + artTag + "' WHERE UUID = '" + uuid.toString() + "';");
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            File f = new File(Main.getPlugin(Main.class).getDataFolder() + "/data.yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(f);

            config.set("users." + uuid.toString() + ".artist.tag", artTag);
            config.set("users." + uuid.toString() + ".artist.hasTag", true);

            try {
                config.save(f);
            } catch(IOException e) {
                e.printStackTrace();
            }
        }

        users.put(uuid, this);
        usersName.put(name, this);
    }

    public void removeArtTag() {
        this.artTag = null;
        this.hasArtTag = false;

        if (Main.databaseEnabled) {
            Connection connection = null;
            PreparedStatement statement = null;

            try {
                connection = Main.getDatabase().getConnection();
                statement = connection.prepareStatement("UPDATE users SET HAS_ART_TAG = false, ART_TAG = null WHERE UUID = '" + uuid.toString() + "';");
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            File f = new File(Main.getPlugin(Main.class).getDataFolder() + "/data.yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(f);

            config.set("users." + uuid.toString() + ".artist.tag", "");
            config.set("users." + uuid.toString() + ".artist.hasTag", false);

            try {
                config.save(f);
            } catch(IOException e) {
                e.printStackTrace();
            }
        }

        users.put(uuid, this);
        usersName.put(name, this);
    }

    public void setArtKarma(int artKarma) {
        this.artKarma = artKarma;
        checkArt();

        if (Main.databaseEnabled) {
            Connection connection = null;
            PreparedStatement statement = null;

            try {
                connection = Main.getDatabase().getConnection();
                statement = connection.prepareStatement("UPDATE users SET ART_KARMA = " + artKarma + " WHERE UUID = '" + uuid.toString() + "';");
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            File f = new File(Main.getPlugin(Main.class).getDataFolder() + "/data.yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(f);

            config.set("users." + uuid.toString() + ".artist.karma", artKarma);

            try {
                config.save(f);
            } catch(IOException e) {
                e.printStackTrace();
            }
        }

        users.put(uuid, this);
        usersName.put(name, this);
    }

    public void setArtNum(int artNum) {
        this.artNum = artNum;

        if (Main.databaseEnabled) {
            Connection connection = null;
            PreparedStatement statement = null;

            try {
                connection = Main.getDatabase().getConnection();
                statement = connection.prepareStatement("UPDATE users SET ART_NUM = " + artNum + " WHERE UUID = '" + uuid.toString() + "';");
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            File f = new File(Main.getPlugin(Main.class).getDataFolder() + "/data.yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(f);

            config.set("users." + uuid.toString() + ".artist.rankNum", artNum);

            try {
                config.save(f);
            } catch(IOException e) {
                e.printStackTrace();
            }
        }

        users.put(uuid, this);
        usersName.put(name, this);
    }

    public void setBuildRank(int buildRank) {
        this.buildRank = buildRank;

        if (Main.databaseEnabled) {
            Connection connection = null;
            PreparedStatement statement = null;

            try {
                connection = Main.getDatabase().getConnection();
                statement = connection.prepareStatement("UPDATE users SET BUILD_RANK = " + buildRank + " WHERE UUID = '" + uuid.toString() + "';");
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            File f = new File(Main.getPlugin(Main.class).getDataFolder() + "/data.yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(f);

            config.set("users." + uuid.toString() + ".builder.rank", buildRank);

            try {
                config.save(f);
            } catch(IOException e) {
                e.printStackTrace();
            }
        }

        users.put(uuid, this);
        usersName.put(name, this);
    }

    public void setBuildTag(String buildTag) {
        this.buildTag = buildTag;
        this.hasBuildTag = true;

        if (Main.databaseEnabled) {
            Connection connection = null;
            PreparedStatement statement = null;

            try {
                connection = Main.getDatabase().getConnection();
                statement = connection.prepareStatement("UPDATE users SET HAS_BUILD_TAG = true, BUILD_TAG = '" + buildTag + "' WHERE UUID = '" + uuid.toString() + "';");
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            File f = new File(Main.getPlugin(Main.class).getDataFolder() + "/data.yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(f);

            config.set("users." + uuid.toString() + ".builder.tag", buildTag);
            config.set("users." + uuid.toString() + ".builder.hasTag", true);

            try {
                config.save(f);
            } catch(IOException e) {
                e.printStackTrace();
            }
        }

        users.put(uuid, this);
        usersName.put(name, this);
    }

    public void removeBuildTag() {
        this.buildTag = null;
        this.hasBuildTag = false;

        if (Main.databaseEnabled) {
            Connection connection = null;
            PreparedStatement statement = null;

            try {
                connection = Main.getDatabase().getConnection();
                statement = connection.prepareStatement("UPDATE users SET HAS_BUILD_TAG = false, BUILD_TAG = null WHERE UUID = '" + uuid.toString() + "';");
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            File f = new File(Main.getPlugin(Main.class).getDataFolder() + "/data.yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(f);

            config.set("users." + uuid.toString() + ".builder.tag", "");
            config.set("users." + uuid.toString() + ".builder.hasTag", false);

            try {
                config.save(f);
            } catch(IOException e) {
                e.printStackTrace();
            }
        }

        users.put(uuid, this);
        usersName.put(name, this);
    }

    public void setBuildKarma(int buildKarma) {
        this.buildKarma = buildKarma;
        checkBuild();

        if (Main.databaseEnabled) {
            Connection connection = null;
            PreparedStatement statement = null;

            try {
                connection = Main.getDatabase().getConnection();
                statement = connection.prepareStatement("UPDATE users SET BUILD_KARMA = " + buildKarma + " WHERE UUID = '" + uuid.toString() + "';");
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            File f = new File(Main.getPlugin(Main.class).getDataFolder() + "/data.yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(f);

            config.set("users." + uuid.toString() + ".builder.karma", buildKarma);

            try {
                config.save(f);
            } catch(IOException e) {
                e.printStackTrace();
            }
        }

        users.put(uuid, this);
        usersName.put(name, this);
    }

    public void setBuildNum(int buildNum) {
        this.buildNum = buildNum;

        if (Main.databaseEnabled) {
            Connection connection = null;
            PreparedStatement statement = null;

            try {
                connection = Main.getDatabase().getConnection();
                statement = connection.prepareStatement("UPDATE users SET BUILD_NUM = " + buildNum + " WHERE UUID = '" + uuid.toString() + "';");
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            File f = new File(Main.getPlugin(Main.class).getDataFolder() + "/data.yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(f);

            config.set("users." + uuid.toString() + ".builder.rankNum", buildNum);

            try {
                config.save(f);
            } catch(IOException e) {
                e.printStackTrace();
            }
        }

        users.put(uuid, this);
        usersName.put(name, this);
    }

    public void setCmdRank(int cmdRank) {
        this.cmdRank = cmdRank;

        if (Main.databaseEnabled) {
            Connection connection = null;
            PreparedStatement statement = null;

            try {
                connection = Main.getDatabase().getConnection();
                statement = connection.prepareStatement("UPDATE users SET CMD_RANK = " + cmdRank + " WHERE UUID = '" + uuid.toString() + "';");
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            File f = new File(Main.getPlugin(Main.class).getDataFolder() + "/data.yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(f);

            config.set("users." + uuid.toString() + ".cmd.rank", cmdRank);

            try {
                config.save(f);
            } catch(IOException e) {
                e.printStackTrace();
            }
        }

        users.put(uuid, this);
        usersName.put(name, this);
    }

    public void setCmdTag(String cmdTag) {
        this.cmdTag = cmdTag;
        this.hasCmdTag = true;

        if (Main.databaseEnabled) {
            Connection connection = null;
            PreparedStatement statement = null;

            try {
                connection = Main.getDatabase().getConnection();
                statement = connection.prepareStatement("UPDATE users SET HAS_CMD_TAG = true, CMD_TAG = '" + cmdTag + "' WHERE UUID = '" + uuid.toString() + "';");
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            File f = new File(Main.getPlugin(Main.class).getDataFolder() + "/data.yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(f);

            config.set("users." + uuid.toString() + ".cmd.tag", cmdTag);
            config.set("users." + uuid.toString() + ".cmd.hasTag", true);

            try {
                config.save(f);
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
        users.put(uuid, this);
        usersName.put(name, this);

    }

    public void removeCmdTag() {
        this.cmdTag = null;
        this.hasCmdTag = false;

        if (Main.databaseEnabled) {
            Connection connection = null;
            PreparedStatement statement = null;

            try {
                connection = Main.getDatabase().getConnection();
                statement = connection.prepareStatement("UPDATE users SET HAS_CMD_TAG = false, CMD_TAG = null WHERE UUID = '" + uuid.toString() + "';");
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            File f = new File(Main.getPlugin(Main.class).getDataFolder() + "/data.yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(f);

            config.set("users." + uuid.toString() + ".cmd.tag", "");
            config.set("users." + uuid.toString() + ".cmd.hasTag", false);

            try {
                config.save(f);
            } catch(IOException e) {
                e.printStackTrace();
            }
        }

        users.put(uuid, this);
        usersName.put(name, this);
    }

    public void setCmdKarma(int cmdKarma) {
        this.cmdKarma = cmdKarma;
        checkCmd();

        if (Main.databaseEnabled) {
            Connection connection = null;
            PreparedStatement statement = null;

            try {
                connection = Main.getDatabase().getConnection();
                statement = connection.prepareStatement("UPDATE users SET CMD_KARMA = " + cmdKarma + " WHERE UUID = '" + uuid.toString() + "';");
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            File f = new File(Main.getPlugin(Main.class).getDataFolder() + "/data.yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(f);

            config.set("users." + uuid.toString() + ".cmd.karma", cmdKarma);

            try {
                config.save(f);
            } catch(IOException e) {
                e.printStackTrace();
            }
        }

        users.put(uuid, this);
        usersName.put(name, this);
    }

    public void setCmdNum(int cmdNum) {
        this.cmdNum = cmdNum;

        if (Main.databaseEnabled) {
            Connection connection = null;
            PreparedStatement statement = null;

            try {
                connection = Main.getDatabase().getConnection();
                statement = connection.prepareStatement("UPDATE users SET CMD_NUM = " + cmdNum + " WHERE UUID = '" + uuid.toString() + "';");
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            File f = new File(Main.getPlugin(Main.class).getDataFolder() + "/data.yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(f);

            config.set("users." + uuid.toString() + ".cmd.rankNum", cmdNum);

            try {
                config.save(f);
            } catch(IOException e) {
                e.printStackTrace();
            }
        }

        users.put(uuid, this);
        usersName.put(name, this);
    }

    public void setDevRank(int devRank) {
        this.devRank = devRank;

        if (Main.databaseEnabled) {
            Connection connection = null;
            PreparedStatement statement = null;

            try {
                connection = Main.getDatabase().getConnection();
                statement = connection.prepareStatement("UPDATE users SET DEV_RANK = " + devRank + " WHERE UUID = '" + uuid.toString() + "';");
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            File f = new File(Main.getPlugin(Main.class).getDataFolder() + "/data.yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(f);

            config.set("users." + uuid.toString() + ".developer.rank", devRank);

            try {
                config.save(f);
            } catch(IOException e) {
                e.printStackTrace();
            }
        }

        users.put(uuid, this);
        usersName.put(name, this);
    }

    public void setDevTag(String devTag) {
        this.devTag = devTag;
        this.hasDevTag = true;

        if (Main.databaseEnabled) {
            Connection connection = null;
            PreparedStatement statement = null;

            try {
                connection = Main.getDatabase().getConnection();
                statement = connection.prepareStatement("UPDATE users SET HAS_DEV_TAG = true, DEV_TAG = '" + devTag + "' WHERE UUID = '" + uuid.toString() + "';");
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            File f = new File(Main.getPlugin(Main.class).getDataFolder() + "/data.yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(f);

            config.set("users." + uuid.toString() + ".developer.tag", devTag);
            config.set("users." + uuid.toString() + ".developer.hasTag", true);

            try {
                config.save(f);
            } catch(IOException e) {
                e.printStackTrace();
            }
        }

        users.put(uuid, this);
        usersName.put(name, this);
    }

    public void removeDevTag() {
        this.devTag = null;
        this.hasDevTag = false;

        if (Main.databaseEnabled) {
            Connection connection = null;
            PreparedStatement statement = null;

            try {
                connection = Main.getDatabase().getConnection();
                statement = connection.prepareStatement("UPDATE users SET HAS_DEV_TAG = false, DEV_TAG = null WHERE UUID = '" + uuid.toString() + "';");
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            File f = new File(Main.getPlugin(Main.class).getDataFolder() + "/data.yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(f);

            config.set("users." + uuid.toString() + ".developer.tag", "");
            config.set("users." + uuid.toString() + ".developer.hasTag", false);

            try {
                config.save(f);
            } catch(IOException e) {
                e.printStackTrace();
            }
        }

        users.put(uuid, this);
        usersName.put(name, this);
    }

    public void setDevKarma(int devKarma) {
        this.devKarma = devKarma;
        checkDev();

        if (Main.databaseEnabled) {
            Connection connection = null;
            PreparedStatement statement = null;

            try {
                connection = Main.getDatabase().getConnection();
                statement = connection.prepareStatement("UPDATE users SET DEV_KARMA = " + devKarma + " WHERE UUID = '" + uuid.toString() + "';");
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            File f = new File(Main.getPlugin(Main.class).getDataFolder() + "/data.yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(f);

            config.set("users." + uuid.toString() + ".developer.karma", devKarma);

            try {
                config.save(f);
            } catch(IOException e) {
                e.printStackTrace();
            }
        }

        users.put(uuid, this);
        usersName.put(name, this);
    }

    public void setDevNum(int devNum) {
        this.devNum = devNum;

        if (Main.databaseEnabled) {
            Connection connection = null;
            PreparedStatement statement = null;

            try {
                connection = Main.getDatabase().getConnection();
                statement = connection.prepareStatement("UPDATE users SET DEV_NUM = " + devNum + " WHERE UUID = '" + uuid.toString() + "';");
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            File f = new File(Main.getPlugin(Main.class).getDataFolder() + "/data.yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(f);

            config.set("users." + uuid.toString() + ".developer.rankNum", devNum);

            try {
                config.save(f);
            } catch(IOException e) {
                e.printStackTrace();
            }
        }

        users.put(uuid, this);
        usersName.put(name, this);
    }

    public void setGMRank(int gmRank) {
        this.gmRank = gmRank;

        if (Main.databaseEnabled) {
            Connection connection = null;
            PreparedStatement statement = null;

            try {
                connection = Main.getDatabase().getConnection();
                statement = connection.prepareStatement("UPDATE users SET GM_RANK = " + gmRank + " WHERE UUID = '" + uuid.toString() + "';");
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            File f = new File(Main.getPlugin(Main.class).getDataFolder() + "/data.yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(f);

            config.set("users." + uuid.toString() + ".gm.rank", gmRank);

            try {
                config.save(f);
            } catch(IOException e) {
                e.printStackTrace();
            }
        }

        users.put(uuid, this);
        usersName.put(name, this);
    }

    public void setGMTag(String gmTag) {
        this.gmTag = gmTag;
        this.hasGMTag = true;

        if (Main.databaseEnabled) {
            Connection connection = null;
            PreparedStatement statement = null;

            try {
                connection = Main.getDatabase().getConnection();
                statement = connection.prepareStatement("UPDATE users SET HAS_GM_TAG = true, GM_TAG = '" + gmTag + "' WHERE UUID = '" + uuid.toString() + "';");
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            File f = new File(Main.getPlugin(Main.class).getDataFolder() + "/data.yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(f);

            config.set("users." + uuid.toString() + ".gm.tag", gmTag);
            config.set("users." + uuid.toString() + ".gm.hasTag", true);

            try {
                config.save(f);
            } catch(IOException e) {
                e.printStackTrace();
            }
        }

        users.put(uuid, this);
        usersName.put(name, this);
    }

    public void removeGMTag() {
        this.gmTag = null;
        this.hasGMTag = false;

        if (Main.databaseEnabled) {
            Connection connection = null;
            PreparedStatement statement = null;

            try {
                connection = Main.getDatabase().getConnection();
                statement = connection.prepareStatement("UPDATE users SET HAS_GM_TAG = false, GM_TAG = null WHERE UUID = '" + uuid.toString() + "';");
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            File f = new File(Main.getPlugin(Main.class).getDataFolder() + "/data.yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(f);

            config.set("users." + uuid.toString() + ".gm.tag", "");
            config.set("users." + uuid.toString() + ".gm.hasTag", false);

            try {
                config.save(f);
            } catch(IOException e) {
                e.printStackTrace();
            }
        }

        users.put(uuid, this);
        usersName.put(name, this);
    }

    public void setGMKarma(int gmKarma) {
        this.gmKarma = gmKarma;
        checkGM();

        if (Main.databaseEnabled) {
            Connection connection = null;
            PreparedStatement statement = null;

            try {
                connection = Main.getDatabase().getConnection();
                statement = connection.prepareStatement("UPDATE users SET GM_KARMA = " + gmKarma + " WHERE UUID = '" + uuid.toString() + "';");
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            File f = new File(Main.getPlugin(Main.class).getDataFolder() + "/data.yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(f);

            config.set("users." + uuid.toString() + ".gm.karma", gmKarma);

            try {
                config.save(f);
            } catch(IOException e) {
                e.printStackTrace();
            }
        }

        users.put(uuid, this);
        usersName.put(name, this);
    }

    public void setGMNum(int gmNum) {
        this.gmNum = gmNum;

        if (Main.databaseEnabled) {
            Connection connection = null;
            PreparedStatement statement = null;

            try {
                connection = Main.getDatabase().getConnection();
                statement = connection.prepareStatement("UPDATE users SET GM_NUM = " + gmNum + " WHERE UUID = '" + uuid.toString() + "';");
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            File f = new File(Main.getPlugin(Main.class).getDataFolder() + "/data.yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(f);

            config.set("users." + uuid.toString() + ".gm.rankNum", gmNum);

            try {
                config.save(f);
            } catch(IOException e) {
                e.printStackTrace();
            }
        }

        users.put(uuid, this);
        usersName.put(name, this);
    }
    
    public boolean hasAdminTag() {
        return hasAdminTag;
    }
    
    public boolean hasBuildTag() {
        return hasBuildTag;
    }
    
    public boolean hasArtTag() {
        return hasArtTag;
    }
    
    public boolean hasCmdTag() {
        return hasCmdTag;
    }
    
    public boolean hasDevTag() {
        return hasDevTag;
    }

    public boolean hasGMTag() {
        return hasGMTag;
    }

    public int getAdminKarma() {
        return adminKarma;
    }

    public Player getPlayer() {
        return p;
    }

    public String getName() {
        return name;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void checkDev() {

        int karma = devKarma;
        SubRank r = SubRank.subranks.get(devRank);
        int num = devNum;

        if (r.canRankup()) {
            while (karma >= r.getRankup()) {
                if (r.canRankup()) {
                    r = SubRank.subranks.get(r.getRankupTo());
                    if (r.hasSubcategories()) { num = 1; }
                    else { num = 0; }
                    if (!r.canRankup()) {
                        break;
                    }
                }
            }
        }
        
        SubRank r2 = SubRank.subranks.get(devRank);

        if (r != r2) {
            if (r.hasSubcategories()) {
                if (r.getMaxNum() != 0) {
                    boolean toggle = false;
                    for (int i = r.getMaxNum(); i > 0; i--) {
                        if (karma >= r.getNum(i) && !toggle && r.getNum(i) != -1) {
                            devNum = i;
                            toggle = true;
                        }
                    }
                } else {
                    devNum = 0;
                }
            } else {
                devNum = 0;
            }

            if (devNum == 0) {
                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                        "&7" + name + " is now a " + r.getTag() + "Developer"));
            } else {
                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                        "&7" + name + " is now a " + r.getTag() + "Developer " + RomanNumeral.toRoman(devNum)));
            }

            setDevRank(r.getId());
            setDevNum(devNum);
        } else {
            if (r.hasSubcategories()) {
                if (r.getMaxNum() != 0) {
                    boolean toggle = false;
                    for (int i = r.getMaxNum(); i > 0; i--) {
                        if (karma >= r.getNum(i) && !toggle && r.getNum(i) != -1) {
                            devNum = i;
                            toggle = true;
                        }
                    }
                } else {
                    devNum = 0;
                }
            } else {
                devNum = 0;
            }

            if (num != devNum) {
                if (devNum == 0) {
                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                            "&7" + name + " is now a " + r2.getTag() + "Developer"));
                } else {
                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                            "&7" + name + " is now a " + r2.getTag() + "Developer " + RomanNumeral.toRoman(devNum)));
                }
            }

            setDevNum(devNum);
        }

        users.put(uuid, this);
        usersName.put(name, this);

    }

    public void checkArt() {

        int karma = artKarma;
        SubRank r = SubRank.subranks.get(artRank);
        int num = artNum;

        if (r.canRankup()) {
            while (karma >= r.getRankup()) {
                if (r.canRankup()) {
                    r = SubRank.subranks.get(r.getRankupTo());
                    if (r.hasSubcategories()) { num = 1; }
                    else { num = 0; }
                    if (!r.canRankup()) {
                        break;
                    }
                }
            }
        }

        SubRank r2 = SubRank.subranks.get(artRank);

        if (r != r2) {
            if (r.hasSubcategories()) {
                if (r.getMaxNum() != 0) {
                    boolean toggle = false;
                    for (int i = r.getMaxNum(); i > 0; i--) {
                        if (karma >= r.getNum(i) && !toggle && r.getNum(i) != -1) {
                            artNum = i;
                            toggle = true;
                        }
                    }
                } else {
                    artNum = 0;
                }
            } else {
                artNum = 0;
            }

            if (artNum == 0) {
                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                        "&7" + name + " is now a " + r.getTag() + "Artist"));
            } else {
                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                        "&7" + name + " is now a " + r.getTag() + "Artist " + RomanNumeral.toRoman(artNum)));
            }

            setArtRank(r.getId());
            setArtNum(artNum);
        } else {
            if (r.hasSubcategories()) {
                if (r.getMaxNum() != 0) {
                    boolean toggle = false;
                    for (int i = r.getMaxNum(); i > 0; i--) {
                        if (karma >= r.getNum(i) && !toggle && r.getNum(i) != -1) {
                            artNum = i;
                            toggle = true;
                        }
                    }
                } else {
                    artNum = 0;
                }
            } else {
                artNum = 0;
            }

            if (num != artNum) {
                if (artNum == 0) {
                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                            "&7" + name + " is now a " + r2.getTag() + "Artist"));
                } else {
                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                            "&7" + name + " is now a " + r2.getTag() + "Artist " + RomanNumeral.toRoman(artNum)));
                }

                setArtNum(artNum);
            }
        }

        users.put(uuid, this);
        usersName.put(name, this);

    }

    public void checkBuild() {

        int karma = buildKarma;
        SubRank r = SubRank.subranks.get(buildRank);
        int num = buildNum;

        if (r.canRankup()) {
            while (karma >= r.getRankup()) {
                if (r.canRankup()) {
                    r = SubRank.subranks.get(r.getRankupTo());
                    if (r.hasSubcategories()) { num = 1; }
                    else { num = 0; }
                    if (!r.canRankup()) {
                        break;
                    }
                }
            }
        }

        SubRank r2 = SubRank.subranks.get(buildRank);

        if (r != r2) {
            if (r.hasSubcategories()) {
                if (r.getMaxNum() != 0) {
                    boolean toggle = false;
                    for (int i = r.getMaxNum(); i > 0; i--) {
                        if (karma >= r.getNum(i) && !toggle && r.getNum(i) != -1) {
                            buildNum = i;
                            toggle = true;
                        }
                    }
                } else {
                    buildNum = 0;
                }
            } else {
                buildNum = 0;
            }

            if (buildNum == 0) {
                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                        "&7" + name + " is now a " + r.getTag() + "Builder"));
            } else {
                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                        "&7" + name + " is now a " + r.getTag() + "Builder " + RomanNumeral.toRoman(buildNum)));
            }

            setBuildRank(r.getId());
            setBuildNum(buildNum);
        } else {
            if (r.hasSubcategories()) {
                if (r.getMaxNum() != 0) {
                    boolean toggle = false;
                    for (int i = r.getMaxNum(); i > 0; i--) {
                        if (karma >= r.getNum(i) && !toggle && r.getNum(i) != -1) {
                            buildNum = i;
                            toggle = true;
                        }
                    }
                } else {
                    buildNum = 0;
                }
            } else {
                buildNum = 0;
            }

            if (num != buildNum) {
                if (buildNum == 0) {
                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                            "&7" + name + " is now a " + r2.getTag() + "Builder"));
                } else {
                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                            "&7" + name + " is now a " + r2.getTag() + "Builder " + RomanNumeral.toRoman(buildNum)));
                }
            }

            setBuildNum(buildNum);
        }

        users.put(uuid, this);
        usersName.put(name, this);

    }

    public void checkCmd() {

        int karma = cmdKarma;
        SubRank r = SubRank.subranks.get(cmdRank);
        int num = cmdNum;

        if (r.canRankup()) {
            while (karma >= r.getRankup()) {
                if (r.canRankup()) {
                    r = SubRank.subranks.get(r.getRankupTo());
                    if (r.hasSubcategories()) { num = 1; }
                    else { num = 0; }
                    if (!r.canRankup()) {
                        break;
                    }
                }
            }
        }

        SubRank r2 = SubRank.subranks.get(cmdRank);

        if (r != r2) {
            if (r.hasSubcategories()) {
                if (r.getMaxNum() != 0) {
                    boolean toggle = false;
                    for (int i = r.getMaxNum(); i > 0; i--) {
                        if (karma >= r.getNum(i) && !toggle && r.getNum(i) != -1) {
                            cmdNum = i;
                            toggle = true;
                        }
                    }
                } else {
                    cmdNum = 0;
                }
            } else {
                cmdNum = 0;
            }

            if (cmdNum == 0) {
                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                        "&7" + name + " is now a " + r.getTag() + "CMD"));
            } else {
                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                        "&7" + name + " is now a " + r.getTag() + "CMD " + RomanNumeral.toRoman(cmdNum)));
            }

            setCmdRank(r.getId());
            setCmdNum(cmdNum);
        } else {
            if (r.hasSubcategories()) {
                if (r.getMaxNum() != 0) {
                    boolean toggle = false;
                    for (int i = r.getMaxNum(); i > 0; i--) {
                        if (karma >= r.getNum(i) && !toggle && r.getNum(i) != -1) {
                            cmdNum = i;
                            toggle = true;
                        }
                    }
                } else {
                    cmdNum = 0;
                }
            } else {
                cmdNum = 0;
            }

            if (num != cmdNum) {
                if (cmdNum == 0) {
                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                            "&7" + name + " is now a " + r2.getTag() + "CMD"));
                } else {
                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                            "&7" + name + " is now a " + r2.getTag() + "CMD " + RomanNumeral.toRoman(cmdNum)));
                }
            }

            setCmdNum(cmdNum);
        }

        users.put(uuid, this);
        usersName.put(name, this);

    }

    public void checkGM() {

        int karma = gmKarma;
        SubRank r = SubRank.subranks.get(gmRank);
        int num = gmNum;

        if (r.canRankup()) {
            while (karma >= r.getRankup()) {
                if (r.canRankup()) {
                    r = SubRank.subranks.get(r.getRankupTo());
                    if (r.hasSubcategories()) { num = 1; }
                    else { num = 0; }
                    if (!r.canRankup()) {
                        break;
                    }
                }
            }
        }

        SubRank r2 = SubRank.subranks.get(gmRank);

        if (r != r2) {
            if (r.hasSubcategories()) {
                if (r.getMaxNum() != 0) {
                    boolean toggle = false;
                    for (int i = r.getMaxNum(); i > 0; i--) {
                        if (karma >= r.getNum(i) && !toggle && r.getNum(i) != -1) {
                            gmNum = i;
                            toggle = true;
                        }
                    }
                } else {
                    gmNum = 0;
                }
            } else {
                gmNum = 0;
            }

            if (gmNum == 0) {
                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                        "&7" + name + " is now a " + r.getTag() + "GM"));
            } else {
                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                        "&7" + name + " is now a " + r.getTag() + "GM " + RomanNumeral.toRoman(gmNum)));
            }

            setGMRank(r.getId());
            setGMNum(gmNum);
        } else {
            if (r.hasSubcategories()) {
                if (r.getMaxNum() != 0) {
                    boolean toggle = false;
                    for (int i = r.getMaxNum(); i > 0; i--) {
                        if (karma >= r.getNum(i) && !toggle && r.getNum(i) != -1) {
                            gmNum = i;
                            toggle = true;
                        }
                    }
                } else {
                    gmNum = 0;
                }
            } else {
                gmNum = 0;
            }

            if (num != gmNum) {
                if (gmNum == 0) {
                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                            "&7" + name + " is now a " + r2.getTag() + "GM"));
                } else {
                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                            "&7" + name + " is now a " + r2.getTag() + "GM " + RomanNumeral.toRoman(gmNum)));
                }
            }

            setGMNum(gmNum);
        }

        users.put(uuid, this);
        usersName.put(name, this);

    }
}
