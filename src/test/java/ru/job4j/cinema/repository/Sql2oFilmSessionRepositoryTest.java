package ru.job4j.cinema.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.configuration.DatasourceConfiguration;

import java.util.List;
import java.util.Properties;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class Sql2oFilmSessionRepositoryTest {
    static Sql2oFilmSessionRepository sql2oFilmSessionRepository;

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

        sql2oFilmSessionRepository = new Sql2oFilmSessionRepository(sql2o);
    }

    /**
     * Метод проверяет наличие сеанса в расписании по ID в БД.
     */
    @Test
    void whenFindById() {
        var created = "2023-03-01T22:00:00";
        var expected = sql2oFilmSessionRepository.findById(5);
        assertThat(expected).isPresent();
        assertThat(expected.get().getFilmId()).isEqualTo(4);
        assertThat(expected.get().getHallsId()).isEqualTo(1);
        assertThat(expected.get().getStartTime()).isEqualTo(created);
        assertThat(expected.get().getPrice()).isEqualTo("200 руб.");
    }

    /**
     * Метод проверяет поиск всех сеансов в расписании в БД.
     */
    @Test
    void whenFindAll() {
        var expected = List.of(sql2oFilmSessionRepository.findById(1).get(),
                sql2oFilmSessionRepository.findById(2).get(),
                sql2oFilmSessionRepository.findById(3).get(),
                sql2oFilmSessionRepository.findById(4).get(),
                sql2oFilmSessionRepository.findById(5).get(),
                sql2oFilmSessionRepository.findById(6).get(),
                sql2oFilmSessionRepository.findById(7).get(),
                sql2oFilmSessionRepository.findById(8).get(),
                sql2oFilmSessionRepository.findById(9).get(),
                sql2oFilmSessionRepository.findById(10).get(),
                sql2oFilmSessionRepository.findById(11).get(),
                sql2oFilmSessionRepository.findById(12).get(),
                sql2oFilmSessionRepository.findById(13).get(),
                sql2oFilmSessionRepository.findById(14).get(),
                sql2oFilmSessionRepository.findById(15).get(),
                sql2oFilmSessionRepository.findById(16).get()
        );
        assertThat(sql2oFilmSessionRepository.findAll()).isEqualTo(expected);
    }
}