package Commands;

import Bot.FireBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import org.apache.commons.codec.digest.DigestUtils;

import java.awt.Color;
import java.util.Objects;

public class Report {

    public static void sendReport(PrivateMessageReceivedEvent event) {
        Message message = event.getMessage();

        String reportFormatMessage = "Invalid format, please use the format below:" +
                "\n`!report | player name | reason`" +
                "\nExample: `!report | Sniper Noob | Spamming the mic.`";
        String[] reportArgs = message.getContentRaw().split("\\|");

        if (reportArgs.length != 3) {
            message.getChannel().sendMessage(reportFormatMessage).queue();
            return;
        }

        String hash = DigestUtils.shaHex(String.valueOf(System.currentTimeMillis())).substring(0, 8);

        EmbedBuilder serverReport = new EmbedBuilder();

        serverReport.setTitle(String.format("Server Report [%s]", hash));
        serverReport.setDescription("Received by FireBot");
        serverReport.setColor(Color.RED);

        serverReport.addField("Reporter", event.getAuthor().getAsMention(), false);
        serverReport.addField("Reported User", reportArgs[1], false);
        serverReport.addField("Report Reason", reportArgs[2], false);

        try {
            // Notify staff.
            Objects.requireNonNull(Objects.requireNonNull(FireBot.jda.getGuildById(149707514521321473L)).getTextChannelById(647655709969874955L)).sendMessage(
                    Objects.requireNonNull(Objects.requireNonNull(FireBot.jda.getGuildById(149707514521321473L)).getRoleById(648340471344529408L)).getAsMention() + "New report received.").queue();
            // Send embed.
            Objects.requireNonNull(Objects.requireNonNull(FireBot.jda.getGuildById(149707514521321473L)).getTextChannelById(647655709969874955L)).sendMessage(serverReport.build()).queue(
                    response -> FireBot.serverReports.put(response.getIdLong(), event.getAuthor().getIdLong()));
            // Notify sender.
            event.getChannel().sendMessage(serverReport.build()).queue();
            event.getChannel().sendMessage(String.format("Report [%s] received and sent to the staff. You will receive a notification when a staff member claims your report.", hash)).queue();

        } catch (NullPointerException npe) {
            npe.printStackTrace();
            FireBot.botLogger.logError(String.format("[ServerReport.sendServerReport] - Failed to generate report sent by %s.", event.getAuthor().getAsMention()));
            event.getChannel().sendMessage("Error sending report. Please contact Sniper Noob to report this issue.").queue();
        }
        serverReport.clear();
    }

    public static void claimReport(GuildMessageReactionAddEvent event) {
        long reporterId = FireBot.serverReports.get(event.getMessageIdLong());
        User reporter = FireBot.jda.getUserById(reporterId);
        Message reportMessage = event.getChannel().getHistory().getMessageById(event.getMessageIdLong());
        String hash = null;

        try {
            hash = Objects.requireNonNull(Objects.requireNonNull(reportMessage).getEmbeds().get(0).getTitle()).substring(14, 23);
        } catch (NullPointerException npe) {
            npe.printStackTrace();
            FireBot.botLogger.logError("[Report.claimReport] - Unable to get hash from report.");
            return;
        }

        try {
            PrivateChannel privateChannel = Objects.requireNonNull(reporter).openPrivateChannel().complete();
            privateChannel.sendMessage(String.format("Report %s has been claimed by %s. They will contact you directly if more information is required.", hash, event.getMember().getAsMention())).queue();
            event.getChannel().sendMessage(String.format("Message %s has been claimed by %s.", hash, event.getMember().getAsMention())).queue();
        } catch (NullPointerException npe) {
            npe.printStackTrace();
            FireBot.botLogger.logError(String.format("[Report.claimReport] - Reporter of report %s no longer visible to bot.", hash));
        }


    }
}
