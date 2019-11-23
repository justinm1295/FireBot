import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;
import java.util.ResourceBundle;

public class Main {
    public static void main(String[] args) throws LoginException {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("config");
        String token = resourceBundle.getString("token");

        JDABuilder jdaBuilder = new JDABuilder(AccountType.BOT);
        jdaBuilder.setToken(token);
        jdaBuilder.build();
    }
}
