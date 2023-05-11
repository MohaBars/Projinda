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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The SpotifyAPI class provides functionality to interact with the Spotify API,
 * including obtaining an access token and performing playlist searches.
 */
public class SpotifyAPI {
    private String clientId = "4528c15345444a0e867580331f178e08";
    private String clientSecret = "e7fa7fd60a4b4624883614601b2f1fdc";
    private String accessToken;

    /**
     * Constructor for the SpotifyAPI class.
     * Initializes the access token by calling the getAccessToken() method.
     */
    public SpotifyAPI() {
        accessToken = getAccessToken();
    }

    /**
     * Retrieves an access token from the Spotify API using the client ID and client secret.
     *
     * @return The access token as a string.
     */
    private String getAccessToken() {
        try {
            // Create the URL object for the Spotify token endpoint
            URL url = new URL("https://accounts.spotify.com/api/token");
            // Open a connection to the URL
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // Set the request method to POST
            connection.setRequestMethod("POST");
            // Set the request properties
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Authorization", "Basic " + Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes(StandardCharsets.UTF_8)));
            // Enable output for writing the request body
            connection.setDoOutput(true);
            OutputStream os = connection.getOutputStream();
            // Write the grant_type parameter to the request body
            os.write("grant_type=client_credentials".getBytes(StandardCharsets.UTF_8));
            os.flush();
            os.close();

            // Read the response from the connection
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

    /**
     * Searches for a playlist related to the provided API weather parameter.
     *
     * @param apiWeather The weather parameter used to search for a playlist.
     * @return An array containing playlist data: [0] playlist URL, [1] artwork URL.
     */
    public String[] searchPlaylist(String apiWeather) {
        // Prepare the query parameter for the Spotify API search
        String query = apiWeather.replace(" ", "%20") + "%20playlist";
        String[] playlistData = new String[2];

        try {
            // Create the URL object for the Spotify API search endpoint
            URL url = new URL("https://api.spotify.com/v1/search?q=" + query + "&type=playlist&limit=1");
            // Open a connection to the URL
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // Set the request method to GET
            connection.setRequestMethod("GET");
            // Set the request properties
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + accessToken);
            // Read the response from the connection
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            connection.disconnect();

            // Convert the response to a JSON string
            String jsonString = content.toString();

            // Extract the playlist URL using a regular expression
            Pattern playlistPattern = Pattern.compile("\"spotify\"\\s*:\\s*\"(https://open\\.spotify\\.com/playlist/[^\"]+)\"");
            Matcher playlistMatcher = playlistPattern.matcher(jsonString);

            if (playlistMatcher.find()) {
                // Store the playlist URL in the playlistData array
                playlistData[0] = playlistMatcher.group(1);
            }
            // Extract the artwork URL using a regular expression
            Pattern artworkPattern = Pattern.compile("\"images\"\\s*:\\s*\\[\\s*\\{[^\\}]*\"url\"\\s*:\\s*\"([^\"]+)\"");
            Matcher artworkMatcher = artworkPattern.matcher(jsonString);

            if (artworkMatcher.find()) {
                // Store the artwork URL in the playlistData array
                playlistData[1] = artworkMatcher.group(1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return playlistData;
    }
}