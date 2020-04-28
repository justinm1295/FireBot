package Commands;

import Bot.FireBot;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Objects;

public class ListDonors {

    public static void listDonors(MessageReceivedEvent event) {
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
            FireBot.botLogger.logError("[ListDonors.listDonors] - Failed to check member's roles for the FirePowered Staff role.");
        }

        try {
            String donors = FireBot.fpDatabaseClient.getDonorList();
            event.getChannel().sendTyping().complete();
            event.getChannel().sendMessage(donors).queue();
        } catch (Exception e) {
            e.printStackTrace();
            FireBot.botLogger.logError("[ListDonors.listDonors] - Unable to retrieve donor list.");
            event.getChannel().sendTyping().complete();
            event.getChannel().sendMessage("Unable to retrieve donor list.").queue();
        }
    }
}
