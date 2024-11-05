package ru.parfenov;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

/**
 * Класс делает регулярный HTTP запрос.
 */
@Slf4j
@PropertySource("classpath:app.properties")
@Component
public class RegularRequest {
    /**
     * Имя и пароль админа приложения
     */
    private @Value("${username}") String username;
    private @Value("${password}") String password;
    private @Value("${uri}") String uri;

    @Autowired
    public RegularRequest() {
    }

    @Scheduled(fixedRate = 24 * 60 * 60 * 1000)
    public void run() throws Exception {
       sendGet(username, password, uri);
    }

    private void sendGet(String username, String password, String uri) throws Exception {
        String encodedCredentials = Base64.getEncoder().encodeToString((username + ":" + password).getBytes());

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(uri))
                .GET()
                .header("Authorization", "Basic " + encodedCredentials)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String responseBody = response.body();
        int responseStatusCode = response.statusCode();

        log.info("httpGetRequest: {}", responseBody);
        log.info("httpGetRequest status code: {}", responseStatusCode);
    }
}