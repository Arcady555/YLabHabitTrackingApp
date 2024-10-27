package ru.parfenov;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Класс делает регулярный HTTP запрос.
 */
@Slf4j
@RequiredArgsConstructor
public class RegularRequest {
    private final CloseableHttpClient httpClient = HttpClients.createDefault();

    public void run() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                RegularRequest obj = new RegularRequest();

                try {
                    try {
                        obj.sendGet();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                } finally {
                    try {
                        obj.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }, 60 * 60 * 1000, 24 * 60 * 60 * 1000);
    }

    private void close() throws IOException {
        httpClient.close();
    }

    private void sendGet() throws Exception {
        HttpGet request = new HttpGet("http://localhost:8080/ht/remind-today-perform-via-email");

        request.addHeader("name", "admin@YLabHabitApp.com");
        request.addHeader("password", "123");

        try (CloseableHttpResponse response = httpClient.execute(request)) {

            System.out.println(response.getStatusLine().toString());

            HttpEntity entity = response.getEntity();
            Header headers = entity.getContentType();
            System.out.println(headers);

            if (entity != null) {
                String result = EntityUtils.toString(entity);
                System.out.println(result);
            }

        }
    }
}