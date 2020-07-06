package Utils;

import Bot.FireBot;
import com.github.koraktor.steamcondenser.steam.SteamPlayer;
import com.github.koraktor.steamcondenser.steam.servers.SourceServer;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.Color;
import java.util.HashMap;
import java.util.ResourceBundle;

public class TF2ServerInterface {

    private final String ip = "66.55.70.58";
    private final int port = 27015;
    private String authKey;

    public TF2ServerInterface() {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("config");

        try {
            authKey = resourceBundle.getString("RCONAuthKey");
        } catch (Exception e) {
            e.printStackTrace();
            FireBot.botLogger.logError("[TF2ServerInterface] - Cannot get RCONAuthKey.");
            System.exit(0);
        }
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

        try {
            SourceServer server = new SourceServer(ip, port);
            server.rconAuth(authKey);
            String result = server.rconExec("sm_reloadadmins");
            server.disconnect();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            FireBot.botLogger.logError("[TF2ServerInterface.reloadAdmins] - Error reloading admins.");
        }

        return null;
    }

    public String restartServer() {
        try {
            SourceServer server = new SourceServer(ip, port);
            server.rconAuth(authKey);
            String result = server.rconExec("_restart");
            server.disconnect();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            FireBot.botLogger.logError("[TF2ServerInterface.restartServer] - Error restarting server.");
        }

        return null;
    }

    public String getServerAdmins() {
        try {
            SourceServer server = new SourceServer(ip, port);
            server.rconAuth(authKey);
            String result = server.rconExec("sm_admins");
            server.disconnect();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            FireBot.botLogger.logError("[TF2ServerInterface.getServerAdmins] - Error getting server admins.");
        }

        return null;
    }

    public String makeAnnouncement(String message) {
        try {
            SourceServer server = new SourceServer(ip, port);
            server.rconAuth(authKey);
            String result = server.rconExec(String.format("sm_csay %s", message));
            server.disconnect();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            FireBot.botLogger.logError("[TF2ServerInterface.makeAnnouncement] - Error making announcement.");
        }

        return null;
    }

    public String getPlayerList() {
        try {
            SourceServer server = new SourceServer(ip, port);
            server.rconAuth(authKey);
            HashMap<String, SteamPlayer> result = server.getPlayers();
            server.disconnect();
            StringBuilder stringBuilder = new StringBuilder();
            for (String key : result.keySet()) {
                SteamPlayer player = result.get(key);
                stringBuilder.append(String.format("\n%s\t%s", player.getName(), player.getSteamId()));
            }
            return stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            FireBot.botLogger.logError("[TF2ServerInterface.getPlayerList] - Error getting players.");
        }

        return null;
    }

    public String rconCommand(String command) {
        try {
            SourceServer server = new SourceServer(ip, port);
            server.rconAuth(authKey);
            String result = server.rconExec(command);
            server.disconnect();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            FireBot.botLogger.logError("[TF2ServerInterface.rconCommand] - Error executing rcon command.");
        }

        return null;
    }
}
