package Commands;

import Bot.FireBot;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class GetServerAdmins {

    public static void getServerAdmins(MessageReceivedEvent event) {

        String reload = FireBot.tf2ServerInterface.getServerAdmins();

        event.getChannel().sendMessage(String.format("```%s```", reload)).queue();
    }
}
