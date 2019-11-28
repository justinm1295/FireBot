package EventManager;

import Bot.FireBot;
import Commands.Info;
import Commands.MemberCount;
import Commands.ReportHelp;
import Commands.ServerReport;
import Commands.Staff;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.sql.SQLException;

public class EventManager extends ListenerAdapter {

    @Override
    public void onPrivateMessageReceived(@Nonnull PrivateMessageReceivedEvent event) {
        Message message = event.getMessage();

        if (message.getAuthor().isBot()) {
            return;
        }

        if (!message.getContentRaw().startsWith(FireBot.prefix)) {
            return;
        }

        String[] args = message.getContentRaw().split("\\s+");

        if (args[0].equals("!help")) {
            event.getChannel().sendTyping().complete();
            ReportHelp.sendReportHelp(event);
        }

        if (args[0].equals("!report")) {
            event.getChannel().sendTyping().complete();
            ServerReport.sendServerReport(event);
        }
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        Message message = event.getMessage();

        if (message.getAuthor().isBot()) {
            return;
        }

        if (!message.getContentRaw().startsWith(FireBot.prefix)) {
            return;
        }

        try {
            FireBot.databaseWriter.insertMessage(event);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String[] args = message.getContentRaw().split("\\s+");

        if (args[0].equals("!ping")) {
            MessageChannel channel = message.getChannel();
            long time = System.currentTimeMillis();
            channel.sendMessage("Pong!")
                    .queue(response -> response.editMessageFormat("Pong: %d ms", System.currentTimeMillis() - time).queue());
        }

        if (args[0].equals("!info")) {
            event.getChannel().sendTyping().complete();
            Info.sendInfo(event);
        }

        if (args[0].equals("!members")) {
            event.getChannel().sendTyping().complete();
            MemberCount.sendMemberCount(event);
        }

        if (args[0].equals("!staff")) {
            event.getChannel().sendTyping().complete();
            Staff.sendStaffInfo(event);
        }
    }
}
