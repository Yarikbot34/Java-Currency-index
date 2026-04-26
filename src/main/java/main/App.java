import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import main.Currency;

void main() throws Exception {
    HttpClient client = HttpClient.newHttpClient();

    HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://v6.exchangerate-api.com/v6/66957f647ee253c5c43f65fa/latest/RUB"))
            .timeout(java.time.Duration.ofSeconds(5))
            .GET()
            .build();

    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    ObjectMapper mapper = new ObjectMapper();
    JsonNode root = mapper.readTree(response.body());
    List<Currency> currencies = new ArrayList<Currency>();
    String[] values = new String[] {"USD", "EUR"};
    for (int i = 0; i < values.length; i++){
        Currency cur = new Currency();
        cur.createCurr(values[i], values[i], root.path("conversion_rates").path(values[i]).asDouble());
        System.out.println(cur);
    }


}
