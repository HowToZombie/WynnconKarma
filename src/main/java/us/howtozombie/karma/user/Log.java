package us.howtozombie.karma.user;

import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import us.howtozombie.Main;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Log {

    public static HashMap<Integer, String> getLogs(String s) {

        HashMap<Integer, String> results = new HashMap<>();

        if (Main.databaseEnabled) {
            Connection connection = null;
            PreparedStatement statement = null;
            ResultSet result = null;
            ResultSet result1 = null;

            try {

                connection = Main.getDatabase().getConnection();
                statement = connection.prepareStatement("SELECT * FROM users WHERE NAME = '" + s + "';");
                result = statement.executeQuery();

                if (!result.next()) {
                    return null;
                }

                String uuid = result.getString("UUID");

                statement = connection.prepareStatement("SELECT * FROM karmalogs WHERE RECIEVER = '" + uuid + "';");
                result = statement.executeQuery();

                while (result.next()) {

                    int day1 = result.getInt("DAY");
                    String day;
                    if (day1 < 10) {
                        day = "0" + day1;
                    } else {
                        day = day1 + "";
                    }

                    int month1 = result.getInt("MONTH");
                    String month;
                    if (month1 < 10) {
                        month = "0" + month1;
                    } else {
                        month = month1 + "";
                    }

                    String year = Integer.toString(result.getInt("YEAR"));

                    String uuidG = result.getString("GIVER");
                    String nameG = "";

                    if (result.getString("GIVER").equalsIgnoreCase("Console")) {

                        nameG = "CONSOLE";

                    } else {

                        statement = connection.prepareStatement("SELECT * FROM users WHERE UUID = '" + result.getString("GIVER") + "';");
                        result1 = statement.executeQuery();

                        while (result1.next()) {
                            nameG = result1.getString("NAME");
                        }

                    }

                    int karma = result.getInt("KARMA");
                    String karmaType = result.getString("KARMA_TYPE");
                    int id = result.getInt("ID");

                    boolean hasReason = result.getBoolean("HAS_REASON");
                    String reason = result.getString("REASON");

                    String in;

                    if (hasReason) {
                        in = "&7" + month + "/" + day + "/" + year + ": &f" + karma + " " + karmaType + " Karma gifted by " + nameG + " for " + reason;
                    } else {
                        in = "&7" + month + "/" + day + "/" + year + ": &f" + karma + " " + karmaType + " Karma gifted by " + nameG;
                    }

                    results.put(id, in);

                }

                return results;

            } catch (SQLException e) {
                e.printStackTrace();
            }

            return null;

        } else {

            String uuid = User.getUser(s).getUuid().toString();

            File f = new File(Main.getPlugin(Main.class).getDataFolder() + "/data.yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(f);



            if (config.contains("users." + uuid + ".logs")) {

                String path = "users." + uuid + ".logs";

                for (int i = 0; i < config.getConfigurationSection(path).getKeys(false).size(); i++) {

                    String subPath = path + "." + i;

                    String dateString = config.getString(subPath + ".date");
                    String [] date = dateString.split("/");
                    String year = date[0];
                    String month = date[1];
                    String day = date[2];

                    String type = config.getString(subPath + ".type");
                    String giverID = config.getString(subPath + ".giver");
                    String giver;
                    if (giverID.equalsIgnoreCase("console")) {
                        giver = "CONSOLE";
                    } else {
                        User u = User.getUser(UUID.fromString(giverID));
                        giver = u.getName();
                    }
                    String amount = config.getString(subPath + ".amount");
                    boolean hasReason = config.getBoolean(subPath + ".hasReason");
                    String reason = config.getString(subPath + ".reason");

                    String out;

                    if (hasReason) {
                        out = "&7" + month + "/" + day + "/" + year + ": &f" + amount + " " + type + " Karma gifted by " + giver + " for " + reason;
                    } else {
                        out = "&7" + month + "/" + day + "/" + year + ": &f" + amount + " " + type + " Karma gifted by " + giver;
                    }

                    results.put(i, out);

                }

                return results;
            }

            return null;
        }

    }

    public static void addToLog(String s, UUID u, int k, boolean h, String r, String t) {

        if (Main.databaseEnabled) {
            Connection connection = null;
            PreparedStatement statement = null;
            ResultSet result = null;

            try {

                connection = Main.getDatabase().getConnection();
                statement = connection.prepareStatement("SELECT * FROM users WHERE NAME = '" + s + "';");
                result = statement.executeQuery();

                if (!result.next()) {
                    return;
                }

                String uuid = result.getString("UUID");

                DateFormat format = new SimpleDateFormat("yyyy/MM/dd");
                Date date = new Date();
                String date1 = format.format(date);
                String[] date2 = date1.split("/");
                String day = date2[2];
                String month = date2[1];
                String year = date2[0];

                statement = connection.prepareStatement("INSERT INTO karmalogs (KARMA, KARMA_TYPE, RECIEVER, GIVER, DAY, MONTH, YEAR, HAS_REASON, REASON)" +
                        " VALUES (" + k + ", '" + t + "', '" + uuid + "', '" + u + "', " + day + ", " + month + ", " + year +
                        ", " + h + ", '" + r + "');");
                statement.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }

        } else {

            String uuid = User.getUser(s).getUuid().toString();

            DateFormat format = new SimpleDateFormat("yyyy/MM/dd");
            Date date = new Date();
            String date1 = format.format(date);

            File f = new File(Main.getPlugin(Main.class).getDataFolder() + "/data.yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(f);

            int num;
            if (!config.contains("users." + uuid + ".logs")) {
                num = 0;
            } else {
                ConfigurationSection sec = config.getConfigurationSection("users." + uuid + ".logs");
                List<String> keys = new ArrayList<>(sec.getKeys(false));
                Collections.sort(keys);
                Collections.reverse(keys);
                num = Integer.valueOf(keys.get(0)) + 1;
            }

            String path = "users." + uuid + ".logs." + num;

            config.set(path + ".date", date1);
            config.set(path + ".type", t);
            config.set(path + ".giver", u.toString());
            config.set(path + ".amount", k);
            config.set(path + ".hasReason", h);
            config.set(path + ".reason", r);

            try {
                config.save(f);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    public static void addToLog(String s, int k, boolean h, String r, String t) {

        if (Main.databaseEnabled) {
            Connection connection = null;
            PreparedStatement statement = null;
            ResultSet result = null;

            try {

                connection = Main.getDatabase().getConnection();
                statement = connection.prepareStatement("SELECT * FROM users WHERE NAME = '" + s + "';");
                result = statement.executeQuery();

                if (!result.next()) {
                    return;
                }

                String uuid = result.getString("UUID");

                DateFormat format = new SimpleDateFormat("yyyy/MM/dd");
                Date date = new Date();
                String date1 = format.format(date);
                String[] date2 = date1.split("/");
                String day = date2[2];
                String month = date2[1];
                String year = date2[0];

                statement = connection.prepareStatement("INSERT INTO karmalogs (KARMA, KARMA_TYPE, RECIEVER, GIVER, DAY, MONTH, YEAR, HAS_REASON, REASON)" +
                        " VALUES (" + k + ", '" + t + "', '" + uuid + "', 'CONSOLE', " + day + ", " + month + ", " + year +
                        ", " + h + ", '" + r + "');");
                statement.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }

        } else {
            String uuid = User.getUser(s).getUuid().toString();

            DateFormat format = new SimpleDateFormat("yyyy/MM/dd");
            Date date = new Date();
            String date1 = format.format(date);

            File f = new File(Main.getPlugin(Main.class).getDataFolder() + "/data.yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(f);

            int num;
            if (!config.contains("users." + uuid + ".logs")) {
                num = 0;
            } else {
                ConfigurationSection sec = config.getConfigurationSection("users." + uuid + ".logs");
                List<String> keys = new ArrayList<>(sec.getKeys(false));
                Collections.sort(keys);
                Collections.reverse(keys);
                num = Integer.valueOf(keys.get(0)) + 1;
            }

            String path = "users." + uuid + ".logs." + num;

            config.set(path + ".date", date1);
            config.set(path + ".type", t);
            config.set(path + ".giver", "CONSOLE");
            config.set(path + ".amount", k);
            config.set(path + ".hasReason", h);
            config.set(path + ".reason", r);

            try {
                config.save(f);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
