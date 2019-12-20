package Bot;

import EventManager.EventManager;
import Utils.BotLogger;
import Utils.DatabaseWriter;
import Utils.TF2ServerInterface;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;
import java.util.HashMap;
import java.util.ResourceBundle;

public class FireBot {

    public static JDA jda;
    public static String prefix = "!";
    public static DatabaseWriter databaseWriter;
    public static BotLogger botLogger;
    public static TF2ServerInterface tf2ServerInterface;
    public static HashMap<Long, Long> serverReports = new HashMap<>();

    public static void main(String[] args) throws LoginException, InterruptedException {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("config");
        String token = null;
        try {
            token = resourceBundle.getString("token");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Cannot get Discord token.");
            System.exit(0);
        }

        jda = new JDABuilder(token).addEventListeners(new EventManager()).build();
        jda.awaitReady();
        jda.getPresence().setActivity(Activity.watching("for reports."));

        botLogger = new BotLogger();
        databaseWriter = new DatabaseWriter();
        tf2ServerInterface = new TF2ServerInterface();

        botLogger.logMessage("[FireBot.main] - New bot session started.");
    }
}
