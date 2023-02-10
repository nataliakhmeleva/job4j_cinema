package ru.job4j.cinema.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.configuration.DatasourceConfiguration;

import java.util.List;
import java.util.Properties;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class Sql2oHallRepositoryTest {
    static Sql2oHallRepository sql2oHallRepository;

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

        sql2oHallRepository = new Sql2oHallRepository(sql2o);
    }

    /**
     * Метод проверяет наличие зала по ID в БД.
     */
    @Test
    void whenFindById() {
        var expected = sql2oHallRepository.findById(1);
        assertThat(expected).isPresent();
        assertThat(expected.get().getName()).isEqualTo("Красный зал");
        assertThat(expected.get().getRowCount()).isEqualTo(12);
        assertThat(expected.get().getPlaceCount()).isEqualTo(16);
        assertThat(expected.get().getDescription()).isEqualTo("Большой, комфортный зал на 190 человек.");
    }

    /**
     * Метод проверяет поиск всех залов в кинотеатре в БД.
     */
    @Test
    void whenFindAll() {
        var expected = List.of(sql2oHallRepository.findById(1).get(),
                sql2oHallRepository.findById(2).get(),
                sql2oHallRepository.findById(3).get());
        assertThat(sql2oHallRepository.findAll()).isEqualTo(expected);

    }
}