package us.howtozombie.karma.user;

import us.howtozombie.Main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private static Connection source = null;

    public Database() {
        load();
    }

    public void load(){
        try {
            Class.forName("com.mysql.jdbc.Driver");

            source = DriverManager
                    .getConnection("jdbc:mysql://" + Main.host+ ":" + Main.port + "/" + Main.database, Main.username, Main.password);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public Connection getConnection() {
        close();
        load();
        return source;
    }

    public void close() {
        try {
            if (source != null && !source.isClosed()) {
                source.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
