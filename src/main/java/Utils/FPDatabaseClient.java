package Utils;

import Bot.FireBot;
import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class FPDatabaseClient {

    private String dbUser;
    private String dbPassword;
    private String dbHost;
    private String dbDatabase;
    private int dbPort;

    private final DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");

    public FPDatabaseClient() {
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

    public String getDonorList() throws SQLException {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUser(dbUser);
        dataSource.setPassword(dbPassword);
        dataSource.setServerName(dbHost);
        dataSource.setDatabaseName(dbDatabase);

        StringBuilder result = new StringBuilder("```Name\tSteamID32\tExpiration Date\tType```");

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement("SELECT name, steamid, expires, donation_type FROM special_donors");
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.beforeFirst();
            result.append("\n```");
            while (resultSet.next()) {
                Date date = new Date(resultSet.getLong("expires") * 1000);
                String name = resultSet.getString("name");
                String steamId = resultSet.getString("steamid");
                String expirationDate = dateFormat.format(date);
                String donationType = resultSet.getString("donation_type");
                result.append(String.format("\n%s\t%s\t%s\t%s", name, steamId, expirationDate, donationType));
            }
            result.append("```");
            return result.toString();

        } catch (Exception e) {
            e.printStackTrace();
            FireBot.botLogger.logError("[FPDatabaseWriter.getDonorList] - Failed to get donor list.");
            return "Error while retrieving donors.";
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
