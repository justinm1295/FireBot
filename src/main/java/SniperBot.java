import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;
import java.util.ResourceBundle;

public class SniperBot {
    public static void main(String[] args) throws LoginException, InterruptedException {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("config");
        String token = resourceBundle.getString("token");
        JDA jda = new JDABuilder(token).addEventListeners(new EventManager()).build();
        jda.awaitReady();
    }
}
