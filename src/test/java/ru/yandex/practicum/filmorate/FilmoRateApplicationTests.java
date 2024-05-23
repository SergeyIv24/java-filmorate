package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmoRateApplicationTests {
    private final UserDbStorage userStorage;
    private final FilmDbStorage filmStorage;


    public void testCreateUser() {
        User addingUser = new User();
        addingUser.setLogin("TestLogin");
        addingUser.setName("TestName");
        addingUser.setEmail("Test@Test.ru");
        addingUser.setBirthday(LocalDate.now());
        User user = userStorage.addUser(addingUser);
        //assertThat(user).hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    public void testFindUserById() {
        testCreateUser();
        Optional<User> userOptional = userStorage.getUser(1L);
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

    public void testCreateFilm() {
        Film adingfilm = new Film();
        adingfilm.setName("TestFilm");
        adingfilm.setDescription("TestDescription");
        adingfilm.setDuration(Duration.ofMinutes(100));
        adingfilm.setReleaseDate(LocalDate.now());
        Mpa mpa = new Mpa();
        mpa.setId(2);
        adingfilm.setMpa(mpa);
        Film film = filmStorage.addFilm(adingfilm);
        assertThat(film).hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    public void testFindFilmById() {
        testCreateFilm();
        Optional<Film> filmOptional = filmStorage.getFilm(1L);
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

}
