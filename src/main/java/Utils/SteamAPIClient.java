package Utils;

import Bot.FireBot;
import org.apache.http.client.utils.URIBuilder;
import org.json.JSONObject;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SteamAPIClient {

    private String apiKey;
    private final String SteamUserUrl = "http://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/";

    public SteamAPIClient() {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("config");
        try {
            this.apiKey = resourceBundle.getString("SteamAPIKey");
        } catch (Exception e) {
            e.printStackTrace();
            FireBot.botLogger.logError("[SteamAPIClient] - Cannot get SteamAPIKey.");
            System.exit(0);
        }
    }

    public ArrayList<String> getSteamInfo(String steamId64) {
        try {
            ArrayList<String> result = new ArrayList<>();
            URIBuilder uriBuilder = new URIBuilder(SteamUserUrl);
            uriBuilder.addParameter("key", apiKey);
            uriBuilder.addParameter("steamids", steamId64);
            HttpRequest request = HttpRequest.newBuilder().uri(uriBuilder.build()).build();
            HttpClient httpClient = HttpClient.newBuilder().build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject responseBody = new JSONObject(response.body());
            if (response.statusCode() != 200) {
                FireBot.botLogger.logError(String.format("[SteamAPIClient.getSteamInfo] - Failed to get Steam info, got %s from server.", response.statusCode()));
                return null;
            }
            responseBody = responseBody.getJSONObject("response").getJSONArray("players").getJSONObject(0);
            result.add(convertId(Long.parseLong(responseBody.getString("steamid"))));
            result.add(responseBody.getString("personaname"));
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            FireBot.botLogger.logError("[NewDonor.getSteamInfo] - Unable to contact Steam server.");
        }
        return null;
    }

    public static String convertId(long steamId64) {
        long base = 76561197960265728L;
        long mod = steamId64 % 2;
        long result = steamId64 - mod - base;

        return String.format("STEAM_0:%s:%s", mod, result / 2L);
    }
}
