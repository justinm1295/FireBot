package Utils;

import Bot.FireBot;
import com.mysql.cj.jdbc.MysqlDataSource;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.ResourceBundle;

public class DatabaseClient {

    private String dbUser;
    private String dbPassword;
    private String dbHost;
    private String dbDatabase;

    public DatabaseClient() {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("config");
        try {
            dbUser = resourceBundle.getString("user");
            dbPassword = resourceBundle.getString("password");
            dbHost = resourceBundle.getString("host");
            dbDatabase = resourceBundle.getString("database");
        } catch (Exception e) {
            e.printStackTrace();
            FireBot.botLogger.logError("[DatabaseWriter] - Cannot get database config values.");
            System.exit(0);
        }
    }

    public void insertMessage(MessageReceivedEvent event) throws SQLException {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUser(dbUser);
        dataSource.setPassword(dbPassword);
        dataSource.setServerName(dbHost);
        dataSource.setDatabaseName(dbDatabase);

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement("INSERT INTO Messages (date, userName, userID, userDiscriminator, content) VALUES (?, ?, ?, ?, ?)");
            preparedStatement.setTimestamp(1, new Timestamp(new Date().getTime()));
            preparedStatement.setString(2, event.getAuthor().getName());
            preparedStatement.setString(3, event.getAuthor().getId());
            preparedStatement.setString(4, event.getAuthor().getDiscriminator());
            preparedStatement.setString(5, event.getMessage().getContentRaw());

            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            FireBot.botLogger.logError("[DatabaseWriter.insertMessage] - Failed to insert message.");
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
