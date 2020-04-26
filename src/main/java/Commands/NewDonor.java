package Commands;

import Bot.FireBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.Color;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Objects;

public class NewDonor {

    public static void newDonor(MessageReceivedEvent event) {
        String formatMessage = "Please adhere to the correct donor syntax:" +
                "\n```!newDonor | SteamId64 | Length(weeks), 0 for perma | Type```";

        Role headAdmin = null;
        for (Role role : event.getGuild().getRoles()) {
            if (role.getName().equals("Head Administrators")) {
                headAdmin = role;
            }
        }

        try {
            if (!Objects.requireNonNull(event.getMember()).getRoles().contains(headAdmin)) {
                event.getChannel().sendTyping().complete();
                event.getChannel().sendMessage("You do not have permission to do this.").queue();
                return;
            }
        } catch (NullPointerException npe) {
            npe.printStackTrace();
            FireBot.botLogger.logError("[NewDonor.newDonor] - Failed to check member's roles for the Head Administrators role.");
        }

        // steamid, name, expires, donation_type
        ArrayList<String> donorAttributes;

        String[] messageParts = event.getMessage().getContentRaw().split(" \\| ");

        if (messageParts.length != 4) {
            event.getChannel().sendTyping().complete();
            event.getChannel().sendMessage(formatMessage).queue();
            return;
        }

        long expirationDate;

        if (Integer.parseInt(messageParts[2]) == 0) {
            expirationDate = 1949720400L;
        } else if (Integer.parseInt(messageParts[2]) < 0) {
            event.getChannel().sendTyping().complete();
            event.getChannel().sendMessage(formatMessage).queue();
            return;
        } else {
            expirationDate = Instant.now().plus(Integer.parseInt(messageParts[2]) * 7, ChronoUnit.DAYS).getEpochSecond();
        }

        try {
            donorAttributes = FireBot.steamAPIClient.getSteamInfo(messageParts[1]);
        } catch (Exception e) {
            e.printStackTrace();
            FireBot.botLogger.logError("[NewDonor.newDonor] - Failed to get steamInfo.");
            return;
        }

        try {
            FireBot.fpDatabaseWriter.insertDonor(donorAttributes.get(0), donorAttributes.get(1), expirationDate, messageParts[3]);
        } catch (Exception e) {
            e.printStackTrace();
            FireBot.botLogger.logError("[NewDonor.newDonor] - Failed to insert new donor.");
            return;
        }

        EmbedBuilder donor = new EmbedBuilder();
        donor.setTitle("New Donor Added");
        donor.setColor(Color.GREEN);

        donor.addField("SteamID32", donorAttributes.get(0), false);
        donor.addField("Name", donorAttributes.get(1), false);
        donor.addField("Expiration Date", Long.toString(expirationDate), false);
        donor.addField("Donation Type", messageParts[3], false);
        donor.addField("Added By", Objects.requireNonNull(event.getMember()).getEffectiveName(), false);

        String reload = FireBot.tf2ServerInterface.reloadAdmins();

        System.out.println(reload);

        event.getChannel().sendTyping().complete();
        event.getChannel().sendMessage(donor.build()).queue();
    }
}
