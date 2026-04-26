import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

void main() throws Exception {
    HttpClient client = HttpClient.newHttpClient();

    HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://v6.exchangerate-api.com/v6/66957f647ee253c5c43f65fa/latest/RUB"))
            .timeout(java.time.Duration.ofSeconds(5))
            .GET()
            .build();

    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    System.out.println("Status: " + response.statusCode());
    System.out.println("Body:   " + response.body());
}
