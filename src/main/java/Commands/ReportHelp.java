package Commands;

import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

public class ReportHelp {

    public static void sendReportHelp(PrivateMessageReceivedEvent event) {
        String helpMessage = "To send a report to the staff, please use the following command:" +
                "\n`!report | player name | reason`" +
                "\nExample: `!report | Sniper Noob | Spamming the mic.`";

        event.getChannel().sendMessage(helpMessage).queue();
    }
}
