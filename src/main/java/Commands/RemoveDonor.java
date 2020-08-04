package Commands;

import Bot.FireBot;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Objects;

public class RemoveDonor {

    public static void removeDonor(MessageReceivedEvent event) {
        Role headAdmin = null;
        for (Role role : event.getGuild().getRoles()) {
            if (role.getName().equals("Head Administrators")) {
                headAdmin = role;
            }
        }

        try {
            if (!Objects.requireNonNull(event.getMember()).getRoles().contains(headAdmin)) {
                event.getChannel().sendTyping().complete();
                event.getChannel().sendMessage("You do not have permission to do this.").queue();
                return;
            }
        } catch (NullPointerException npe) {
            npe.printStackTrace();
            FireBot.botLogger.logError("[RemoveDonor.removeDonor] - Failed to check member's roles for the Head Administrators role.");
        }

        String[] messageParts = event.getMessage().getContentRaw().split("!removeDonor ");

        if (messageParts.length != 2) {
            event.getChannel().sendTyping().complete();
            event.getChannel().sendMessage("Please provide the SteamID64 to delete.").queue();
            return;
        }

        try {
            FireBot.fpDatabaseClient.removeDonor( Long.parseLong(messageParts[1]));
        } catch (Exception e) {
            e.printStackTrace();
            FireBot.botLogger.logError("[RemoveDonor.removeDonor] - Failed to remove donor.");
            return;
        }

        event.getChannel().sendMessage("Removed donor.").queue();
    }
}
