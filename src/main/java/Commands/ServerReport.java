package Commands;

import Bot.FireBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

import java.awt.Color;
import java.util.Objects;

public class ServerReport {

    public static void sendServerReport(PrivateMessageReceivedEvent event) {
        Message message = event.getMessage();

        String reportFormatMessage = "Invalid format, please use the format below:" +
                "\n`!report | player name | reason`" +
                "\nExample: `!report | Sniper Noob | Spamming the mic.`";
        String[] reportArgs = message.getContentRaw().split("\\|");

        if (reportArgs.length != 3) {
            message.getChannel().sendMessage(reportFormatMessage).queue();
            return;
        }

        EmbedBuilder serverReport = new EmbedBuilder();

        serverReport.setTitle("Server Report ");
        serverReport.setDescription("Received by FireBot");
        serverReport.setColor(Color.RED);

        serverReport.addField("Reporter", event.getAuthor().getAsMention(), false);
        serverReport.addField("Reported User", reportArgs[1], false);
        serverReport.addField("Report Reason", reportArgs[2], false);

        try {
            Objects.requireNonNull(Objects.requireNonNull(FireBot.jda.getGuildById(149707514521321473L)).getTextChannelById(647655709969874955L)).sendMessage(
                    Objects.requireNonNull(Objects.requireNonNull(FireBot.jda.getGuildById(149707514521321473L)).getRoleById(648340471344529408L)).getAsMention() + " New report received.\n" + serverReport.build()).queue();
            event.getChannel().sendMessage("Report received and sent to the staff. A member of staff may reach out to you directly if necessary.").queue();

        } catch (NullPointerException npe) {
            npe.printStackTrace();
            event.getChannel().sendMessage("Error sending report. Please contact Sniper Noob to report this issue.").queue();
        }
        serverReport.clear();
    }
}
