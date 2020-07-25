package Commands;

import Bot.FireBot;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Objects;

public class ReloadAccess {

    public static void reloadAccess(MessageReceivedEvent event) {

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
            FireBot.botLogger.logError("[ReloadAccess.reloadAccess] - Failed to reload access cache.");
        }

        String reload = FireBot.tf2ServerInterface.reloadAccess();

        event.getChannel().sendMessage(String.format("```%s```", reload)).queue();
    }
}
