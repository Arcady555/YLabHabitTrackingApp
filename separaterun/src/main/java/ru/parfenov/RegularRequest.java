package ru.parfenov;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Класс делает регулярный HTTP запрос.
 */
@Slf4j

@Component
public class RegularRequest {

    @Autowired
    public RegularRequest() {
    }
    
    public void run() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                RegularRequest obj = new RegularRequest();
                try {
                    obj.sendGet();

                } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
            }
        }, 60 * 60 * 1000, 24 * 60 * 60 * 1000);
    }

    private void sendGet() throws Exception {
        /**
         * Сюда нужно вписать имя и пароль админа приложения
         */
        String username = "user";
        String password = "pass";
        String encodedCredentials = Base64.getEncoder().encodeToString((username + ":" + password).getBytes());

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://localhost:8080/ht/remind-today-perform-via-email"))
                .GET()
                .header("Authorization", "Basic " + encodedCredentials)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String responseBody = response.body();
        int responseStatusCode = response.statusCode();

        System.out.println("httpGetRequest: " + responseBody);
        System.out.println("httpGetRequest status code: " + responseStatusCode);
    }
}