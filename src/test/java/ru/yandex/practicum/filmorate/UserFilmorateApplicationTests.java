package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class UserFilmorateApplicationTests {
    private static URI uri;
    private static HttpClient client;
    //Запуск сервера
    private static final ConfigurableApplicationContext app = SpringApplication.run(FilmorateApplication.class);

    @BeforeAll
    public static void createClient() { //Создание HTTP - клиента
        uri = URI.create("http://localhost:8080/users");
        client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    @AfterAll
    public static void stopServer() {
        app.close(); //Остановка сервера
    }

    @Test
    public void shouldReturn200WhenAddNewUser() throws IOException, InterruptedException {
        String firstUser = "{\"login\": \"Sergey\"," +
                "\"name\": \"SergeyIv\"," +
                "\"email\": \"SomeEmail@yandex.ru\"," +
                "\"birthday\": \"28.12.1998\"}";
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
    public void shouldReturn500WhenUserWithoutEmail() throws IOException, InterruptedException {
        String secondUser = "{\"login\": \"Sergey\"," +
                "\"name\": \"SergeyIv\"," +
                "\"birthday\": \"28.12.1998\"}";
        HttpRequest requestPost = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(secondUser))
                .setHeader("Accept", "application/json")
                .setHeader("Content-type", "application/json")
                .uri(uri)
                .build();
        HttpResponse.BodyHandler<String> handlerPost = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(requestPost, handlerPost);
        assertEquals(400, response.statusCode());
    }

    @Test
    public void shouldReturn500IfItIsWithoutLogin() throws IOException, InterruptedException {
        String thirdUser = "{\"login\": \"     \"," +
                "\"name\": \"SergeyIv\"," +
                "\"email\": \"SomeEmail@yandex.ru\"," +
                "\"birthday\": \"28.12.1998\"}";
        HttpRequest requestPost = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(thirdUser))
                .setHeader("Accept", "application/json")
                .setHeader("Content-type", "application/json")
                .uri(uri)
                .build();
        HttpResponse.BodyHandler<String> handlerPost = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(requestPost, handlerPost);
        assertEquals(400, response.statusCode());
    }

    @Test
    public void shouldReturn500WhenBirthdayInFuture() throws IOException, InterruptedException {
        String thirdUser = "{\"login\": \"Sergey\"," +
                "\"name\": \"SergeyIv\"," +
                "\"email\": \"SomeEmail@yandex.ru\"," +
                "\"birthday\": \"28.12.2998\"}";
        HttpRequest requestPost = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(thirdUser))
                .setHeader("Accept", "application/json")
                .setHeader("Content-type", "application/json")
                .uri(uri)
                .build();
        HttpResponse.BodyHandler<String> handlerPost = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(requestPost, handlerPost);
        assertEquals(500, response.statusCode());
    }


}
