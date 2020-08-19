package Bot;

import EventManager.EventManager;
import Utils.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Emote;

import javax.security.auth.login.LoginException;
import java.util.Objects;
import java.util.Random;
import java.util.ResourceBundle;

public class FireBot {

    public static JDA jda;
    public static String prefix = "!";
    public static DatabaseClient databaseClient;
    public static FPDatabaseClient fpDatabaseClient;
    public static BotLogger botLogger;
    public static TF2ServerInterface tf2ServerInterface;
    public static ReportMap reportMap;
    public static SteamAPIClient steamAPIClient;
    public static MessageCache messageCache;
    public static Random randomGenerator;
    public static Emote FPEmote;

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

        reportMap = new ReportMap();
        botLogger = new BotLogger();
        databaseClient = new DatabaseClient();
        fpDatabaseClient = new FPDatabaseClient();
        tf2ServerInterface = new TF2ServerInterface();
        steamAPIClient = new SteamAPIClient();
        messageCache = new MessageCache(4096);
        randomGenerator = new Random();
        getFPEmote();

        botLogger.logMessage("[FireBot.main] - New bot session started.");
    }

    private static void getFPEmote() {
        try {
            FPEmote = Objects.requireNonNull(FireBot.jda.getGuildsByName("FirePowered Gaming", false).get(0).getEmotesByName("FP", false).get(0));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Couldn't find FP emote.");
        }
    }
}
