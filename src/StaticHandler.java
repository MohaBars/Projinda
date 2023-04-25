import java.io.IOException;
import java.io.OutputStream;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.File;

/**
 * A static file handler for serving static files from a specified root directory
 * in an HTTP server using Java's built-in com.sun.net.httpserver.HttpHandler.
 * It serves static files such as HTML, CSS, JavaScript, images, etc.
 *
 * The root directory for static files is specified in the 'root' field, and it
 * defaults to the "web" directory in the current directory. If a requested file
 * exists in the root directory, it is served with a 200 OK response. Otherwise,
 * a 404 Not Found response is sent.
 */

public class StaticHandler implements HttpHandler {

    /**
     * Handles incoming HTTP requests by serving static files from the specified
     * root directory.
     *
     * @param t the HttpExchange object representing the incoming request
     * @throws IOException if there is an error reading the requested file or
     *                     sending the HTTP response
     */
    public void handle(HttpExchange t) throws IOException {
        String root = "web"; // specify the root directory for static files
        String path = t.getRequestURI().getPath();
        String file = root + path; // construct the file path
        File f = new File(file);
        
        if (f.exists() && f.isFile()) {
            // Serve the static file
            byte[] response = Files.readAllBytes(Paths.get(file));
            t.sendResponseHeaders(200, response.length);
            OutputStream os = t.getResponseBody();
            os.write(response);
            os.close();
        } else {
            // File not found, send 404 response
            String response = "File not found";
            t.sendResponseHeaders(404, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}