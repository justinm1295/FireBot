package Utils;

import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent;

import java.util.LinkedHashMap;
import java.util.Map;

public class MessageCache extends LinkedHashMap<Long, String> {

    private final int size;

    public MessageCache(int size) {
        super(size);
        this.size = size;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<Long, String> eldest) {
        return size() > this.size;
    }

    public String getUpdatedMessage(GuildMessageUpdateEvent event) {
        if (!containsKey(event.getMessageIdLong())) {
            return null;
        } else {
            return String.format("Message updated: %s\nOld content: ```%s```\nNew content: ```%s```", event.getMessageIdLong(), get(event.getMessageIdLong()), event.getMessage().getContentRaw());
        }
    }

    public String getDeletedMessage(GuildMessageDeleteEvent event) {
        if (!containsKey(event.getMessageIdLong())) {
            return null;
        } else {
            return String.format("Message deleted: %s\nContent: ```%s```", event.getMessageIdLong(), get(event.getMessageIdLong()));
        }
    }
}
