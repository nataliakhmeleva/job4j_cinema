package ru.job4j.cinema.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.User;

import java.util.Properties;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class Sql2oUserRepositoryTest {
    static Sql2oUserRepository sql2oUserRepository;

    @BeforeAll
    static void initRepositories() throws Exception {
        var properties = new Properties();
        try (var inputStream = Sql2oUserRepository.class.getClassLoader().getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }
        var url = properties.getProperty("datasource.url");
        var username = properties.getProperty("datasource.username");
        var password = properties.getProperty("datasource.password");

        var configuration = new DatasourceConfiguration();
        var datasource = configuration.connectionPool(url, username, password);
        var sql2o = configuration.databaseClient(datasource);

        sql2oUserRepository = new Sql2oUserRepository(sql2o);
    }

    @AfterEach
    void clearUsers() {
        var users = sql2oUserRepository.findAll();
        for (var user : users) {
            sql2oUserRepository.deleteById(user.getId());
        }
    }

    /**
     * Метод проверяет добавление пользователя в БД.
     */
    @Test
    void whenSave() {
        User user = new User(1, "Ivan", "1@ya.ru", "meow123");
        var expected = sql2oUserRepository.save(user);
        assertThat(expected).isPresent();
        var savedUser = sql2oUserRepository.findById(user.getId());
        assertThat(savedUser).usingRecursiveComparison().isEqualTo(expected);
    }

    /**
     * Метод проверяет отсутствие добавления пользователя в БД.
     */
    @Test
    void whenDoNotSaveThenNothingFound() {
        assertThat(sql2oUserRepository.findAll()).isEqualTo(emptyList());
    }

    /**
     * Метод проверяет наличие пользователя путем поиска по e-mail и паролю.
     */
    @Test
    void whenFindByEmailAndPassword() {
        User user = new User(1, "Ivan", "1@ya.ru", "meow123");
        var expected = sql2oUserRepository.save(user);
        assertThat(expected).isPresent();
        var savedUser = sql2oUserRepository.findByEmailAndPassword(user.getEmail(), user.getPassword());
        assertThat(savedUser).usingRecursiveComparison().isEqualTo(expected);
    }

    /**
     * Метод проверяет добавление в БД только первого пользователя по одному e-mail.
     */
    @Test
    void whenSaveOnlyTheFirstUniqueEmail() {
        User user = new User(1, "Ivan", "1@ya.ru", "meow123");
        User anotherUser = new User(2, "Stepan", "1@ya.ru", "321go");
        var expected = sql2oUserRepository.save(user);
        assertThat(expected).isPresent();
        assertThat(sql2oUserRepository.save(anotherUser)).isEmpty();
    }

}