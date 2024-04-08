package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FilmsFilmorateApplicationTests {
    private static URI uri;
    private static HttpClient client;
    //Запуск сервера
    private static final ConfigurableApplicationContext app = SpringApplication.run(FilmorateApplication.class);

    @BeforeAll
    public static void createClient() { //Создание HTTP - клиента
        uri = URI.create("http://localhost:8080/films");
        client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    @AfterAll
    public static void stopServer() {
        app.close(); //Остановка сервера
    }

    @Test
    public void shouldReturn200WhenAddNewFilm() throws IOException, InterruptedException {
        String firstUser = "{\"name\": \"Inception\"," +
                "\"description\": \"Film is about dreams\"," +
                "\"releaseDate\": \"22.07.2010\"," +
                "\"duration\": \"148\"}"; //Тело запроса
        HttpRequest requestPost = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(firstUser))
                .setHeader("Accept", "application/json")
                .setHeader("Content-type", "application/json")
                .uri(uri)
                .build();
        HttpResponse.BodyHandler<String> handlerPost = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(requestPost, handlerPost);
        assertEquals(200, response.statusCode());
    }

    @Test
    public void shouldReturn500WhenNameIsEmpty() throws IOException, InterruptedException {
        String firstUser = "{\"name\": \"    \"," +
                "\"description\": \"Film is about dreams\"," +
                "\"releaseDate\": \"22.07.2010\"," +
                "\"duration\": \"148\"}";
        HttpRequest requestPost = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(firstUser))
                .setHeader("Accept", "application/json")
                .setHeader("Content-type", "application/json")
                .uri(uri)
                .build();
        HttpResponse.BodyHandler<String> handlerPost = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(requestPost, handlerPost);
        assertEquals(400, response.statusCode());
    }

    @Test
    public void shouldReturn500WhenNegativeDuration() throws IOException, InterruptedException {
        String firstUser = "{\"name\": \"Inception\"," +
                "\"description\": \"Film is about dreams\"," +
                "\"releaseDate\": \"22.07.2010\"," +
                "\"duration\": \"-5\"}";
        HttpRequest requestPost = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(firstUser))
                .setHeader("Accept", "application/json")
                .setHeader("Content-type", "application/json")
                .uri(uri)
                .build();
        HttpResponse.BodyHandler<String> handlerPost = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(requestPost, handlerPost);
        assertEquals(500, response.statusCode());
    }

    @Test
    public void shouldReturn500WhenNotCorrectData() throws IOException, InterruptedException {
        String firstUser = "{\"name\": \"Inception\"," +
                "\"description\": \"Film is about dreams\"," +
                "\"releaseDate\": \"22.07.1010\"," +
                "\"duration\": \"500\"}";
        HttpRequest requestPost = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(firstUser))
                .setHeader("Accept", "application/json")
                .setHeader("Content-type", "application/json")
                .uri(uri)
                .build();
        HttpResponse.BodyHandler<String> handlerPost = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(requestPost, handlerPost);
        assertEquals(500, response.statusCode());
    }


}
