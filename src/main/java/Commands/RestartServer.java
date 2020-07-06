package Commands;

import Bot.FireBot;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class RestartServer {

    public static void restartServer(MessageReceivedEvent event) {

        Role headAdmins = null;
        Role seniorAdmins = null;
        for (Role role : event.getGuild().getRoles()) {
            if (role.getName().equals("Head Administrators")) {
                headAdmins = role;
            }

            if (role.getName().equals("Senior Administrators")) {
                seniorAdmins = role;
            }
        }

        try {
            if (!Objects.requireNonNull(event.getMember()).getRoles().contains(headAdmins) && !Objects.requireNonNull(event.getMember()).getRoles().contains(seniorAdmins)) {
                event.getChannel().sendTyping().complete();
                event.getChannel().sendMessage("You do not have permission to do this.").queue();
                return;
            }
        } catch (NullPointerException npe) {
            npe.printStackTrace();
            FireBot.botLogger.logError("[RestartServer.restartServer] - Failed to restart server.");
        }

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                FireBot.tf2ServerInterface.restartServer();
            }
        };

        Timer timer = new Timer();
        timer.schedule(timerTask, 30000);
        event.getChannel().sendMessage("Server will restart in 30 seconds. Making server announcement.").queue();
        FireBot.tf2ServerInterface.makeAnnouncement("The server will restart in 30 seconds. The restart process will take approximately 1 minute.");
    }
}
