package Utils;

import Bot.FireBot;
import com.mysql.cj.jdbc.MysqlDataSource;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class FPDatabaseWriter {

    private String dbUser;
    private String dbPassword;
    private String dbHost;
    private String dbDatabase;
    private int dbPort;

    public FPDatabaseWriter() {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("config");
        try {
            dbUser = resourceBundle.getString("FPuser");
            dbPassword = resourceBundle.getString("FPpassword");
            dbHost = resourceBundle.getString("FPhost");
            dbDatabase = resourceBundle.getString("FPdatabase");
            dbPort = Integer.parseInt(resourceBundle.getString("FPport"));
        } catch (Exception e) {
            e.printStackTrace();
            FireBot.botLogger.logError("[DatabaseWriter] - Cannot get database config values.");
            System.exit(0);
        }
    }

    public void insertDonor(String steamId32, String name, long expirationDate, String type) throws SQLException {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUser(dbUser);
        dataSource.setPassword(dbPassword);
        dataSource.setServerName(dbHost);
        dataSource.setDatabaseName(dbDatabase);
        dataSource.setPort(dbPort);

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement("INSERT INTO special_donors (steamid, name, expires, donation_type) VALUES (?, ?, ?, ?)");
            preparedStatement.setString(1, steamId32);
            preparedStatement.setString(2, name);
            preparedStatement.setLong(3, expirationDate);
            preparedStatement.setString(4, type);

            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            FireBot.botLogger.logError("[FPDatabaseWriter.insertDonor] - Failed to insert donor.");
        } finally {
            if (preparedStatement != null && !preparedStatement.isClosed()) {
                preparedStatement.close();
            }
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        }
    }
}
