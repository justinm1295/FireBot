package Utils;

import Bot.FireBot;

import java.util.Objects;

public class BotLogger {

    private long botLogChannel;

    public BotLogger() {
        try {
            this.botLogChannel = FireBot.jda.getTextChannelsByName("bot-log", false).get(0).getIdLong();
        } catch (Exception e) {
            e.printStackTrace();
            botLogChannel = 0L;
        }
    }

    public void logError(String errorMessage) {
        try {
            Objects.requireNonNull(FireBot.jda.getTextChannelById(botLogChannel)).sendMessage(errorMessage).queue();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Couldn't find logging channel.");
        }
    }
}
