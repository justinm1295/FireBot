package Commands;

import Bot.FireBot;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Objects;

public class MakeAnnouncement {

    public static void makeAnnouncement(MessageReceivedEvent event) {
        Role firePoweredStaff = null;
        for (Role role : event.getGuild().getRoles()) {
            if (role.getName().equals("FirePowered Staff")) {
                firePoweredStaff = role;
            }
        }

        try {
            if (!Objects.requireNonNull(event.getMember()).getRoles().contains(firePoweredStaff)) {
                event.getChannel().sendTyping().complete();
                event.getChannel().sendMessage("You do not have permission to do this.").queue();
                return;
            }
        } catch (NullPointerException npe) {
            npe.printStackTrace();
            FireBot.botLogger.logError("[MakeAnnouncement.makeAnnouncement] - Failed to make announcement.");
        }

        String message = event.getMessage().getContentRaw().split("!makeAnnouncement ")[1];

        String announcement = FireBot.tf2ServerInterface.makeAnnouncement(message);

        event.getChannel().sendMessage(String.format("```%s```", announcement)).queue();
    }
}
