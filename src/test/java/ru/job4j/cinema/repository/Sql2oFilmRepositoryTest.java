package ru.job4j.cinema.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.configuration.DatasourceConfiguration;

import java.util.List;
import java.util.Properties;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class Sql2oFilmRepositoryTest {
    static Sql2oFilmRepository sql2oUserRepository;

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

        sql2oUserRepository = new Sql2oFilmRepository(sql2o);
    }

    /**
     * Метод проверяет наличие фильма по ID в БД.
     */
    @Test
    void whenFindById() {
        var expected = sql2oUserRepository.findById(1);
        assertThat(expected).isPresent();
        assertThat(expected.get().getName()).isEqualTo("Аватар: Путь воды");
        assertThat(expected.get().getYear()).isEqualTo(2022);
        assertThat(expected.get().getGenreId()).isEqualTo(1);
        assertThat(expected.get().getMinimalAge()).isEqualTo(12);
        assertThat(expected.get().getDuration()).isEqualTo(192);
        assertThat(expected.get().getFileId()).isEqualTo(1);
    }

    /**
     * Метод проверяет поиск всех фильмов в БД.
     */
    @Test
    void whenFindAll() {
        var expected = List.of(sql2oUserRepository.findById(1).get(),
                sql2oUserRepository.findById(2).get(),
                sql2oUserRepository.findById(3).get(),
                sql2oUserRepository.findById(4).get(),
                sql2oUserRepository.findById(5).get());
        assertThat(sql2oUserRepository.findAll()).isEqualTo(expected);

    }
}