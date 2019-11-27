package Bot;

import EventManager.EventManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;
import java.util.ResourceBundle;

public class FireBot {

    public static JDA jda;
    public static String prefix = "!";

    public static void main(String[] args) throws LoginException, InterruptedException {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("config");
        String token = resourceBundle.getString("token");
        jda = new JDABuilder(token).addEventListeners(new EventManager()).build();
        jda.awaitReady();
        jda.getPresence().setActivity(Activity.watching("for reports. Prefix = \"!\""));
    }
}
