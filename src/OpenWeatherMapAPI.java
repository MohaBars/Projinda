import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.net.ProtocolException;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is used to get the current weather information about a specified place. 
 * The resulting weather from each call with a city to OpenWeatherMapAPI will be stored in the class field "apiWeather"
 */
public class OpenWeatherMapAPI {
    private String apiKey = "8257206a615b0b55accb2cf9cce30db0";
    private String[] apiInformation = new String[2]; 

    public String[] weatherInformation(String city) {
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
                    // This will be the description of the weather
                    apiInformation[0] = matcher.group(2);
                    
                    //Extract local time of the city
                    long unixTime = Long.parseLong(response.toString().split("\"dt\":")[1].split(",")[0]);
                    //This will be the desciption of the time
                    apiInformation[1] = convertUnixToTime(unixTime, response.toString().split("\"timezone\":")[1].split(",")[0]);
                } 
            } else {
                //Modified to converts the error into a string
                apiInformation[0] = "Error";
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return apiInformation;
    }

    private String convertUnixToTime(long unixTime, String timeZone) {
        Date date = new Date(unixTime * 1000L); // Convert Unix time to Java Date object
        SimpleDateFormat sdf = new SimpleDateFormat("HH"); // Use "HH" format to get hour in 24-hour clock format
        sdf.setTimeZone(TimeZone.getTimeZone(timeZone)); // Set the time zone
        return sdf.format(date); // Return the hour part of the time as a string
    }

    // public static void main(String[] args) {
    //     OpenWeatherMapAPI weather = new OpenWeatherMapAPI();
    //     String[] arr = weather.weatherInformation("stockholm");
    //     System.out.println(arr[0]);
    //     System.out.println(arr[1]);

    // }
}