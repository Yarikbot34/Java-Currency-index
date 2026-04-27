package main;
import java.net.URI;
import java.time.Instant;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class App {

    private static List<Currency> currencies;

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        currencies = requestCurrency();
        boolean processed = true;
        while (processed) {
            printCurrency();
            System.out.println("Введите 1 - Обновить данные | 2 - Редактировать список валют | 0 - Выйти");
            int userInput = sc.nextInt();
            switch (userInput) {
                case 0:
                    processed = false;
                    break;
                case 1:
                    requestCurrency();
                    break;
                case 2:
                    redactValueList();
                    break;
            }
        }
    }

    public static void printCurrency() {
        if (!currencies.isEmpty()) {
            for (Currency i : currencies) {
                System.out.println(i);
            }
        } else {
            System.out.println("Нет данных для отображния");
        }
    }

    public static void redactValueList(){
        boolean redactProcessed = true;
        Scanner sc = new Scanner(System.in);
        while (redactProcessed){
            for (String obj: Currency.getNames().keySet()){
                System.out.println("Тег: " + obj + "\tНазвание: " + Currency.getNames().get(obj));
            }
            System.out.println("Введите 1 - Для добавления/редактирования валюты | 2 - Для удаления валюты | 0 - Для выхода");
            int userChoice = sc.nextInt();
            switch (userChoice){
                case 1:
                    System.out.print("Трехзначный индекс валюты: ");
                    String key = sc.next().toUpperCase();
                    sc.nextLine();
                    if (key.length() != 3){
                        System.out.println("Неверная длина индекса");
                        break;
                    }
                    System.out.print("Отображаемое название: ");
                    String name = sc.nextLine();
                    Currency.addValue(key, name);
                    break;
                case 2:
                    System.out.print("Трехзначный индекс удаляемой валюты: ");
                    String delKey = sc.nextLine().toUpperCase();
                    Currency.delValue(delKey);
                    break;
                case 0:
                    redactProcessed = false;
                    break;
            }

        }
    }

    public static List<Currency> requestCurrency() {
        if (Currency.nextUpdateTime < Instant.now().getEpochSecond() || !Arrays.equals(Currency.values.toArray(new String[0]), Currency.lastValues)){
            List<String> values = new ArrayList<>(Currency.names.keySet());
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
                Currency.lastValues = values.toArray(new String[0]);
            } catch (Exception e) {
                System.out.println("Ошибка при получении ответа сервера: " + e);
            }
        }
        return currencies;
    }
}