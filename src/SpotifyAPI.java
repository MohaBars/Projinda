import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class SpotifyAPI {
    private String clientId = "4528c15345444a0e867580331f178e08";
    private String clientSecret = "e7fa7fd60a4b4624883614601b2f1fdc";
    private String accessToken;

    public SpotifyAPI() {
        accessToken = getAccessToken();
    }

    private String getAccessToken() {
        try {
            URL url = new URL("https://accounts.spotify.com/api/token");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Authorization", "Basic " + Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes(StandardCharsets.UTF_8)));
            connection.setDoOutput(true);
            OutputStream os = connection.getOutputStream();
            os.write("grant_type=client_credentials".getBytes(StandardCharsets.UTF_8));
            os.flush();
            os.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            connection.disconnect();

            JSONObject jsonObject = new JSONObject(content.toString());
            return jsonObject.getString("access_token");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String[] searchPlaylists(String apiWeather) {
        String query = apiWeather + " playlist";
        String[] playlistUrls = null;

        try {
            URL url = new URL("https://api.spotify.com/v1/search?q=" + query + "&type=playlist&limit=10");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + accessToken);

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            connection.disconnect();

            JSONObject jsonObject = new JSONObject(content.toString());
            JSONArray playlists = jsonObject.getJSONObject("playlists").getJSONArray("items");
            int playlistCount = playlists.length();
            playlistUrls = new String[playlistCount * 2];

            for (int i = 0; i < playlistCount; i++) {
                JSONObject playlist = playlists.getJSONObject(i);
                String playlistUrl = playlist.getJSONObject("external_urls").getString("spotify");
                String artworkUrl = playlist.getJSONObject("images").getJSONArray("url").getString(0);
                playlistUrls[i * 2] = playlistUrl;
                playlistUrls[i * 2 + 1] = artworkUrl;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return playlistUrls;
    }
}

