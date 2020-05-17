package Commands;

import Bot.FireBot;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Objects;

public class RestartServer {

    public static void restartServer(MessageReceivedEvent event) {

        Role headAdmins = null;
        Role seniorAdmins = null;
        for (Role role : event.getGuild().getRoles()) {
            if (role.getName().equals("Head Administrators")) {
                headAdmins = role;
            }

            if (role.getName().equals("Senior Administrators")) {
                seniorAdmins = role;
            }
        }

        try {
            if (!Objects.requireNonNull(event.getMember()).getRoles().contains(headAdmins) && !Objects.requireNonNull(event.getMember()).getRoles().contains(seniorAdmins)) {
                event.getChannel().sendTyping().complete();
                event.getChannel().sendMessage("You do not have permission to do this.").queue();
                return;
            }
        } catch (NullPointerException npe) {
            npe.printStackTrace();
            FireBot.botLogger.logError("[RestartServer.restartServer] - Failed to restart server.");
        }

        String restart = FireBot.tf2ServerInterface.restartServer();

        event.getChannel().sendMessage(String.format("```%s```", restart)).queue();
    }
}
