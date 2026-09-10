import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.InetAddress;

public class Main {

    public static void main(String[] args) throws IOException {
        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));
        String instanceName = System.getenv().getOrDefault("INSTANCE_NAME", "api-instance");

        String dbHost = System.getenv().getOrDefault("DB_HOST", "db");
        String dbPort = System.getenv().getOrDefault("DB_PORT", "5432");
        String dbName = System.getenv().getOrDefault("DB_NAME", "labdb");

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext("/", exchange -> {
            String hostname;
            try {
                hostname = InetAddress.getLocalHost().getHostName();
            } catch (Exception e) {
                hostname = "unknown";
            }

            String body = "{"
                    + "\"message\": \"Hello from Java API\","
                    + "\"instance\": \"" + instanceName + "\","
                    + "\"container_hostname\": \"" + hostname + "\","
                    + "\"db_target\": \"" + dbHost + ":" + dbPort + "/" + dbName + "\""
                    + "}";

            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, body.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(body.getBytes());
            os.close();
        });

        server.createContext("/health", exchange -> {
            String body = "{\"status\": \"ok\"}";
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, body.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(body.getBytes());
            os.close();
        });

        server.setExecutor(null);
        System.out.println("Instance " + instanceName + " listening on port " + port);
        server.start();
    }
}
