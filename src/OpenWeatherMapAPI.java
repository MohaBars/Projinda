import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.ProtocolException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is used to get the current weather information about a specified place. 
 * The resulting weather from each call with a city to OpenWeatherMapAPI will be stored in the class field "apiWeather"
 */
public class OpenWeatherMapAPI {
    private String apiKey = "8257206a615b0b55accb2cf9cce30db0";
    private String apiWeather; 

    public String weatherInformation(String city) {
        
        //This is the standard call method for openweather
        String url = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey;

        try {
            //Set up an http connection
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            //HTTP_OK is set to 200 which means it was a successful request for some reason
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                //Extract weather information using regular expressions
                Pattern pattern = Pattern.compile("\"main\":\"(.+?)\",\"description\":\"(.+?)\"");
                Matcher matcher = pattern.matcher(response.toString());
                if (matcher.find()) {
                    apiWeather = matcher.group(2); // Will be the description of the weather
                } 
            } else {
                System.out.println("Error: " + responseCode);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return apiWeather;
    }
}