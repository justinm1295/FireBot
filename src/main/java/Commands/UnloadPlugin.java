package Commands;

import Bot.FireBot;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Objects;

public class UnloadPlugin {

    public static void unloadPlugin(MessageReceivedEvent event) {
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
            FireBot.botLogger.logError("[UnloadPlugin.unloadPlugin] - Failed to unload plugin.");
        }

        String plugin = event.getMessage().getContentRaw().split("!unloadPlugin ")[1];

        String commandResult = FireBot.tf2ServerInterface.loadPlugin(plugin);

        event.getChannel().sendMessage(String.format("```%s```", commandResult)).queue();
    }
}
