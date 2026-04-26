import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import main.Currency;

void main() throws Exception {
    String[] values = new String[] {"USD", "EUR", "CNY"};
    List<Currency> currencies = reqestCurrency(values);
    printCurrency(currencies);



}
public void printCurrency(List<Currency> currencies){
    for (Currency i : currencies){
        System.out.println(i);
    }
}

public List<Currency> reqestCurrency(String[] values)throws Exception {
    HttpClient client = HttpClient.newHttpClient();
    ObjectMapper mapper = new ObjectMapper();
    List<Currency> currencies = new ArrayList<Currency>();

    HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://v6.exchangerate-api.com/v6/66957f647ee253c5c43f65fa/latest/RUB"))
            .timeout(java.time.Duration.ofSeconds(5))
            .GET()
            .build();

    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    JsonNode root = mapper.readTree(response.body());
    for (int i = 0; i < values.length; i++){
        Currency cur = new Currency();
        cur.createCurr(values[i], root.path("conversion_rates").path(values[i]).asDouble());
        currencies.add(cur);
    }
    return currencies;
}
