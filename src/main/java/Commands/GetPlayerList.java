package Commands;

import Bot.FireBot;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Objects;

public class GetPlayerList {

    public static void getPlayerList(MessageReceivedEvent event) {
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
            FireBot.botLogger.logError("[GetPlayerList.getPlayerList] - Failed to get player list.");
        }

        String playerList = FireBot.tf2ServerInterface.getPlayerList();

        event.getChannel().sendMessage(String.format("```Name\tSteamId```\n```%s```", playerList)).queue();
    }
}
