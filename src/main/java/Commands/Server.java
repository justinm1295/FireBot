package Commands;

import Bot.FireBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;


public class Server {

    public static void sendServer(MessageReceivedEvent event) {
        EmbedBuilder serverEmbed = FireBot.tf2ServerInterface.getServerInfo();

        if (serverEmbed == null) {
            event.getChannel().sendTyping().complete();
            event.getChannel().sendMessage("Unable to retrieve server information").queue();
            return;
        }

        event.getChannel().sendTyping().complete();
        event.getChannel().sendMessage("Retrieving server information...").queue(message ->
                message.editMessage(serverEmbed.build()).queue());
    }
}
