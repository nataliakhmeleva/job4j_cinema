package ru.job4j.cinema.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.configuration.DatasourceConfiguration;

import java.util.Properties;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class Sql2oGenreRepositoryTest {
    static Sql2oGenreRepository sql2oGenreRepository;

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

        sql2oGenreRepository = new Sql2oGenreRepository(sql2o);
    }

    /**
     * Метод проверяет наличие жанра по ID в БД.
     */
    @Test
    void whenFindById() {
        var expected = sql2oGenreRepository.findById(2);
        assertThat(expected).isPresent();
        assertThat(expected.get().getName()).isEqualTo("Мультфильм");
    }
}