package main;
import java.net.URI;
import java.time.Instant;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class App {

    private static List<Currency> currencies;

    static void main() throws Exception {
        Scanner sc = new Scanner(System.in);
        String[] values = new String[]{"USD", "EUR", "CNY"};
        currencies = reqestCurrency(values);
        boolean processed = true;
        while (processed) {
            printCurrency();
            System.out.println("Введите 1 - Обновить данные | 0 - Выйти");
            int userInput = sc.nextInt();
            switch (userInput) {
                case 0:
                    processed = false;
                    break;
                case 1:
                    reqestCurrency(values);
                    System.out.print("\033[H\033[J");
                    break;
            }
        }
    }

    public static void printCurrency() {
        for (Currency i : currencies) {
            System.out.println(i);
        }
    }

    public static List<Currency> reqestCurrency(String[] values) {
        if (Currency.nextUpdateTime < Instant.now().getEpochSecond()){
            HttpClient client = HttpClient.newHttpClient();
            ObjectMapper mapper = new ObjectMapper();
            currencies = new ArrayList<>();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://v6.exchangerate-api.com/v6/66957f647ee253c5c43f65fa/latest/RUB"))
                    .timeout(java.time.Duration.ofSeconds(5))
                    .GET()
                    .build();
            try{
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                JsonNode root = mapper.readTree(response.body());
                for (String val : values) {
                    Currency cur = new Currency();
                    cur.createCurr(val, root.path("conversion_rates").path(val).asDouble());
                    currencies.add(cur);
                }
                Currency.nextUpdateTime = root.path("time_next_update_unix").asInt();
            } catch (Exception e) {
                System.out.println("Ошибка при получении ответа сервера: " + e);
            }
        }
        return currencies;
    }
}