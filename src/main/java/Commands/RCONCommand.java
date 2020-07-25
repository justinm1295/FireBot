package Commands;

import Bot.FireBot;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Objects;

public class RCONCommand {

    public static void executeRconCommand(MessageReceivedEvent event) {
        Role headAdministrators = null;
        for (Role role : event.getGuild().getRoles()) {
            if (role.getName().equals("Head Administrators")) {
                headAdministrators = role;
            }
        }

        try {
            if (!Objects.requireNonNull(event.getMember()).getRoles().contains(headAdministrators)) {
                event.getChannel().sendTyping().complete();
                event.getChannel().sendMessage("You do not have permission to do this.").queue();
                return;
            }
        } catch (NullPointerException npe) {
            npe.printStackTrace();
            FireBot.botLogger.logError("[RCONCommand.executeRconCommand] - Failed to execute rcon command.");
        }

        String command = event.getMessage().getContentRaw().split("!rcon ")[1];

        String commandResult = FireBot.tf2ServerInterface.rconCommand(command);

        for (int i = 0; i < commandResult.length(); i += 1900) {
            event.getChannel().sendMessage(String.format("```%s```", commandResult.substring(i, commandResult.length() - i <= 1900? commandResult.length() : i + 1900))).queue();
        }
    }
}
