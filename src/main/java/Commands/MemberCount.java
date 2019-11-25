package Commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class MemberCount {

    public static void sendMemberCount(MessageReceivedEvent event) {
        event.getChannel().sendMessage(String.format("There are %s members in this Discord server.", String.valueOf(event.getGuild().getMembers().size()))).queue();
    }
}
