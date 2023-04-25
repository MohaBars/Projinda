import java.io.IOException;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpServer;

/**
 * The main class that starts an HTTP server to handle incoming requests for
 * weather information and static files.
 *
 * The 'Main' class creates an HTTP server using the 'HttpServer' class from
 * the Java SE HTTP server package. It registers a 'WeatherHandler' object to
 * handle incoming requests for the '/weather' path, and a 'StaticHandler'
 * object to serve static files from the "web" directory for all other paths.
 * The server is started on the specified port number, and a default executor
 * is used for concurrent processing of incoming requests. The server can be
 * stopped by terminating the program.
 *
 * Usage:
 * 1. Run the 'main' method in the 'Main' class to start the HTTP server.
 * 2. The server will listen for incoming requests on the specified port
 *    number, and requests for '/weather' path will be handled by the
 *    'WeatherHandler' object, while requests for other paths will be handled
 *    by the 'StaticHandler' object.
 * 3. Terminate the program to stop the server.
 */

public class Main {
    public static void main(String[] args) throws IOException {
        int port = 8080;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/weather", new WeatherHandler());
        // Add a new context to serve static files from the "web" directory
        server.createContext("/", new StaticHandler());

        // Create a default executor
        server.setExecutor(null);
        server.start();
        System.out.println("Server started on port " + port);
    }
}