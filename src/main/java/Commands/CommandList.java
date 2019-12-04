package Commands;

import Bot.FireBot;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Objects;

public class CommandList {

    public static void sendCommandList(MessageReceivedEvent event) {
        event.getChannel().sendMessage(String.format("Please see %s for the full list of commands for this server.", Objects.requireNonNull(FireBot.jda.getTextChannelById(651617393323409419L)).getAsMention())).queue();
    }
}
