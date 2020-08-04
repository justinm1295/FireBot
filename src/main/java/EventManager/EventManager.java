package EventManager;

import Bot.FireBot;
import Commands.*;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.guild.member.GuildMemberLeaveEvent;
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
        if (FireBot.reportMap.getReport(event.getMessageIdLong()) != 0L && event.getReactionEmote().getEmoji().equals("✅")) {
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

        if (FireBot.randomGenerator.nextInt(100) == 50) {
            event.getMessage().addReaction("🔥").queue();
        }

        if (FireBot.randomGenerator.nextInt(1000) == 500 && FireBot.FPEmote != null) {
            event.getMessage().addReaction(FireBot.FPEmote).queue();
        }

        try {
            FireBot.databaseClient.insertMessage(event);
        } catch (SQLException e) {
            e.printStackTrace();
            FireBot.botLogger.logError("[EventManager.onMessageReceived] - Failed to insert message into database.");
        }

        if (!message.getContentRaw().startsWith(FireBot.prefix)) {
            return;
        }

        String[] args = message.getContentRaw().split("\\s+");

        if (args[0].equals("!ping")) {
            Ping.sendPing(event);
        }

        if (args[0].equals("!info")) {
            Info.sendInfo(event);
        }

        if (args[0].equals("!members")) {
            MemberCount.sendMemberCount(event);
        }

        if (args[0].equals("!staff")) {
            Staff.sendStaff(event);
        }

        if (args[0].equals("!server")) {
            Server.sendServer(event);
        }

        if (args[0].equals("!commands")) {
            CommandList.sendCommandList(event);
        }

        if (args[0].equals("!newDonor")) {
            NewDonor.newDonor(event);
        }

        if (args[0].equals("!listDonors")) {
            ListDonors.listDonors(event);
        }

        if (args[0].equals("!restartServer")) {
            RestartServer.restartServer(event);
        }

        if (args[0].equals("!reloadAdmins")) {
            ReloadAdmins.reloadAdmins(event);
        }

        if (args[0].equals("!getServerAdmins")) {
            GetServerAdmins.getServerAdmins(event);
        }

        if (args[0].equals("!getPlayerList")) {
            GetPlayerList.getPlayerList(event);
        }

        if (args[0].equals("!makeAnnouncement")) {
            MakeAnnouncement.makeAnnouncement(event);
        }

        if (args[0].equals("!rcon")) {
            RCONCommand.executeRconCommand(event);
        }

        if (args[0].equals("!loadPlugin")) {
            LoadPlugin.loadPlugin(event);
        }

        if (args[0].equals("!unloadPlugin")) {
            UnloadPlugin.unloadPlugin(event);
        }

        if (args[0].equals("!pluginInfo")) {
            PluginInfo.pluginInfo(event);
        }

        if (args[0].equals("!reloadPlugin")) {
            ReloadPlugin.reloadPlugin(event);
        }

        if (args[0].equals("!removeDonor")) {
            RemoveDonor.removeDonor(event);
        }
    }

    @Override
    public void onGuildMemberLeave(@Nonnull GuildMemberLeaveEvent event) {
        FireBot.botLogger.logMessage(String.format("Member %s left the server.", event.getMember().getEffectiveName()));
    }
}
