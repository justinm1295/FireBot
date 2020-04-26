package Utils;

import Bot.FireBot;
import com.github.koraktor.steamcondenser.steam.servers.SourceServer;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.Color;
import java.util.HashMap;
import java.util.ResourceBundle;

public class TF2ServerInterface {

    private String ip;
    private int port;
    private String authKey;

    public TF2ServerInterface() {
        this.ip = "66.55.70.58";
        this.port = 27015;
    }

    public EmbedBuilder getServerInfo() {
        EmbedBuilder serverInfo = new EmbedBuilder();

        serverInfo.setTitle("TF2 Server Information");
        serverInfo.setDescription("Retrieved by FireBot");
        serverInfo.setColor(Color.RED);

        try {
            SourceServer server = new SourceServer(ip, port);
            server.initialize();
            HashMap<String, Object> map = server.getServerInfo();
            serverInfo.addField("Server Name", map.get("serverName").toString(), false);
            serverInfo.addField("IP", String.format("%s:%d", ip, port), false);
            serverInfo.addField("Player Count", String.format("%s/32", map.get("numberOfPlayers").toString()), false);
            serverInfo.addField("Map", map.get("mapName").toString(), false);
            server.disconnect();
            return serverInfo;
        } catch (Exception e) {
            e.printStackTrace();
            FireBot.botLogger.logError("[TF2ServerInterface.sendServerInfo] - Error communicating with TF2 server.");
        }

        serverInfo.clear();

        return null;
    }

    public String reloadAdmins() {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("config");

        try {
            authKey = resourceBundle.getString("RCONAuthKey");
        } catch (Exception e) {
            e.printStackTrace();
            FireBot.botLogger.logError("[TF2ServerInterface] - Cannot get RCONAuthKey.");
            return null;
        }

        try {
            SourceServer server = new SourceServer(ip, port);
            server.rconAuth(authKey);
            server.initialize();
            String result = server.rconExec("sm_reloadadmins");
            server.disconnect();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            FireBot.botLogger.logError("[TF2ServerInterface.reloadAdmins] - Error reloading admins.");
        }

        return null;
    }
}
