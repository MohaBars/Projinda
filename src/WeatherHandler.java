import java.io.IOException;
import java.io.OutputStream;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * A weather handler for handling HTTP requests to retrieve weather information
 * for a specified city using OpenWeatherMap API, and sending the response in
 * plain text format. The respons in then used with Spotifys API to search for a 
 * playlist of the name of the OpenWeatherMap API's desription of the weather.
 *
 * The 'handle' method retrieves the query parameters from the incoming HTTP
 * request URL, specifically the 'query' parameter which represents the city
 * name to retrieve weather information for. It then calls the 'weatherInformation'
 * method of the 'OpenWeatherMapAPI' class to get the weather information as a
 * plain text response. The response is sent back to the client as the HTTP
 * response body with a 200 OK status code, or a 404 Not Found response if the
 * city name is not found in the query parameters.
 */

public class WeatherHandler implements HttpHandler {

    /**
     * Handles incoming HTTP requests for weather information by retrieving
     * the city name from the query parameters, calling the 'weatherInformation'
     * method of the 'OpenWeatherMapAPI' class to get the weather information,
     * and sending the response to the client.
     *
     * @param t the HttpExchange object representing the incoming request
     * @throws IOException if there is an error sending the HTTP response
     */
    public void handle(HttpExchange t) throws IOException {
        // Get the query parameters from the URL
        String query = t.getRequestURI().getQuery();
        String city = "";
        if (query != null) {
            String[] queryParams = query.split("&");
            for (String param : queryParams) {
                String[] keyValue = param.split("=");
                if (keyValue.length == 2 && keyValue[0].equals("query")) { // Update to "query" instead of "city"
                    city = keyValue[1];
                    break;
                }
            }
        }

        // Gets the weather from input location
        OpenWeatherMapAPI openWeatherMapAPI = new OpenWeatherMapAPI();
        String[] result = openWeatherMapAPI.weatherInformation(city);
        String weather = result[0];

        // Uses the output weather desciption to search for a playlist of that name
        SpotifyAPI spotify = new SpotifyAPI();
        String[] links = spotify.searchPlaylists(weather);
        String link = links[0];
        String coverLink = links[1];

        // Addresses the the fact that the time seemed to be off by 2 hours. Might be because we have not payed for the API and use
        // a free version that might not be updated each minute.
        int timeInt = Integer.parseInt(result[1]); // incorrect time by 2 hours.
        timeInt += 2; //Time in hours as an int between 0-23
        String time = Integer.toString(timeInt);

        String results2BeSent = weather + "," + time + "," + link + "," + coverLink;

        t.getResponseHeaders().set("Content-Type", "text/plain");
        t.sendResponseHeaders(200, results2BeSent.getBytes().length);
        OutputStream os = t.getResponseBody();
        os.write(results2BeSent.getBytes());
        os.close();
    }
}

