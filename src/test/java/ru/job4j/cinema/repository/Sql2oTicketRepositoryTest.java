package ru.job4j.cinema.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.Ticket;

import java.util.List;
import java.util.Properties;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class Sql2oTicketRepositoryTest {
    static Sql2oTicketRepository sql2oTicketRepository;

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

        sql2oTicketRepository = new Sql2oTicketRepository(sql2o);
    }

    @AfterEach
    void clearTickets() {
        var tickets = sql2oTicketRepository.findAll();
        for (var ticket : tickets) {
            sql2oTicketRepository.deleteById(ticket.getId());
        }
    }

    /**
     * Метод проверяет добавление билета в БД.
     */
    @Test
    void whenSave() {
        Ticket ticket = new Ticket(1, 1, 5, 7, 1);
        var expected = sql2oTicketRepository.save(ticket);
        assertThat(expected).isPresent();
        var savedTicket = sql2oTicketRepository.findById(ticket.getId());
        assertThat(savedTicket).usingRecursiveComparison().isEqualTo(expected);
    }

    /**
     * Метод проверяет отсутствие добавления билета в БД.
     */
    @Test
    void whenDoNotSaveThenNothingFound() {
        assertThat(sql2oTicketRepository.findAll()).isEqualTo(emptyList());
    }

    /**
     * Метод проверяет наличие всех билетов в БД, различных по сеансу, ряду или месту в зале.
     */
    @Test
    void whenTicketsDoNotMatchByRowNumberAndPlaceNumberAndSessionThenSave() {
        Ticket ticket1 = new Ticket(1, 1, 5, 7, 1);
        Ticket ticket2 = new Ticket(1, 1, 5, 8, 1);
        var expected1 = sql2oTicketRepository.save(ticket1);
        var expected2 = sql2oTicketRepository.save(ticket2);
        assertThat(expected1).isPresent();
        assertThat(expected2).isPresent();
        assertThat(sql2oTicketRepository.findAll()).usingRecursiveComparison().isEqualTo(List.of(ticket1, ticket2));
    }

    /**
     * Метод проверяет наличие только первого билета в БД, совпадающего по сеансу, ряду или месту в зале с другими билетами.
     */
    @Test
    void whenSaveOnlyTheFirstTicket() {
        Ticket ticket1 = new Ticket(1, 1, 5, 7, 1);
        Ticket ticket2 = new Ticket(1, 1, 5, 7, 2);
        var expected1 = sql2oTicketRepository.save(ticket1);
        var expected2 = sql2oTicketRepository.save(ticket2);
        assertThat(expected1).isPresent();
        assertThat(expected2).isEmpty();
    }
}