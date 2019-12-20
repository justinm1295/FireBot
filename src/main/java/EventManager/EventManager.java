package EventManager;

import Bot.FireBot;
import Commands.CommandList;
import Commands.Info;
import Commands.MemberCount;
import Commands.Ping;
import Commands.ReportHelp;
import Commands.Report;
import Commands.Server;
import Commands.Staff;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.sql.SQLException;

public class EventManager extends ListenerAdapter {


    @Override
    public void onGuildMessageReactionAdd(@Nonnull GuildMessageReactionAddEvent event) {
        System.out.println(event.getReactionEmote().getEmoji());
        System.out.println(FireBot.serverReports.get(event.getMessageIdLong()));
        if (FireBot.serverReports.get(event.getMessageIdLong()) != null && event.getReactionEmote().getEmoji().equals("✅")) {
            System.out.println(String.format("Triggering Claim. Message %s", event.getMessageIdLong()));
            event.getChannel().sendTyping().complete();
            Report.claimReport(event);
        }
    }

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
            Report.sendReport(event);
        }
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        Message message = event.getMessage();

        if (message.isFromType(ChannelType.PRIVATE)) {
            return;
        }

        if (message.getAuthor().isBot()) {
            return;
        }

        try {
            System.out.println(String.format("Storing message: %s", event.getMessage().getContentRaw()));
            FireBot.databaseWriter.insertMessage(event);
        } catch (SQLException e) {
            e.printStackTrace();
            FireBot.botLogger.logError("[EventManager.onMessageReceived] - Failed to insert message into database.");
        }

        if (!message.getContentRaw().startsWith(FireBot.prefix)) {
            return;
        }

        String[] args = message.getContentRaw().split("\\s+");

        if (args[0].equals("!ping")) {
            event.getChannel().sendTyping().complete();
            Ping.sendPing(event);
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
            Staff.sendStaff(event);
        }

        if (args[0].equals("!server")) {
            event.getChannel().sendTyping().complete();
            Server.sendServer(event);
        }

        if (args[0].equals("!commands")) {
            event.getChannel().sendTyping().complete();
            CommandList.sendCommandList(event);
        }
    }
}
