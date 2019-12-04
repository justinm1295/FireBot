package Commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandList {

    public static void sendCommandList(MessageReceivedEvent event) {
        event.getChannel().sendMessage("Please see #bot-commands for the full list of commands for this server.").queue();
    }
}
