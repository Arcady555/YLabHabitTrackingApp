package ru.parfenov.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class HabitControllerTest {
    static MockMvc staticMockMvc;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15");

    @Autowired
    MockMvc mockMvc;

    @BeforeAll
    static void setUp(@Autowired MockMvc mockMvc) throws Exception {
        String json1 = "{\"email\": \"user2@mail.ru\", \"password\": \"password2\", \"name\": \"user2\"}";
        mockMvc.perform(post("/sign-up").contentType(MediaType.APPLICATION_JSON).content(json1));
    }

    @Test
    @WithMockUser(roles = {"CLIENT"}, username = "user2@mail.ru")
    @DisplayName("Прогон всех методов на примере действий одного юзера")
    void runByOneUser() throws Exception {
        String json = """
                {
                    "usefulness" : "true",
                    "name" : "Плавать",
                    "description" : "Плавать каждый день",
                    "firstPerform" :
                """
                + "\"" + LocalDate.now() + "\"" +
                """
                            ,
                            "frequency" : 1
                        }
                        """;
        String requestStatistic = "/habits/statistic?dateFrom=" + LocalDate.now() + "&dateTo=" + LocalDate.now();

        this.mockMvc.perform(post("/habits/create").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpectAll(
                        status().isOk(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        content().json("""
                                {
                                    "id": 1,
                                    "user": "user2@mail.ru",
                                    "useful": true,
                                    "active": true,
                                    "streaksAmount": 1,
                                    "name": "Плавать",
                                    "description": "Плавать каждый день",
                                    "dateOfCreate":
                                """ +
                                LocalDate.now() +
                                """
                                            ,
                                            "plannedNextPerform":
                                        """ +
                                LocalDate.now() +
                                """
                                            ,
                                            "lastRealPerform": null,
                                            "frequency": 1,
                                            "performsAmount": 0,
                                            "remind": ""
                                        }
                                        """
                        )
                );

        this.mockMvc.perform(get(requestStatistic))
                .andExpectAll(
                        status().isOk(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        content().json("""
                                [
                                    {
                                    "id":1,
                                    "useful":"true",
                                    "name":"Плавать",
                                    "description":"Плавать каждый день",
                                    "dateOfCreate":
                                """ +
                                LocalDate.now() +
                                """
                                            ,
                                            "plannedNextPerform":
                                        """ +
                                LocalDate.now() +
                                """
                                            ,
                                            "lastRealPerform":null,
                                            "frequency":1,
                                            "statistic":"Not correct date!"
                                            }
                                        ]
                                        """
                        )
                );

        this.mockMvc.perform(get("/habits/perform/1"))
                .andExpectAll(
                        status().isOk(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        content().json("""
                                {
                                    "id": 1,
                                    "user": "user2@mail.ru",
                                    "useful": true,
                                    "active": true,
                                    "streaksAmount": 1,
                                    "name": "Плавать",
                                    "description": "Плавать каждый день",
                                    "dateOfCreate":
                                """ +
                                LocalDate.now() +
                                """
                                            ,
                                            "plannedNextPerform":
                                        """ +
                                LocalDate.now().plusDays(1) +
                                """
                                            ,
                                            "lastRealPerform":
                                        """ +
                                LocalDate.now() +
                                """
                                            ,
                                            "frequency": 1,
                                            "performsAmount": 1,
                                            "remind": ""
                                        }
                                        """
                        )
                );

        this.mockMvc.perform(get("/habits/your-all-list"))
                .andExpectAll(
                        status().isOk(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        content().json("""
                                [
                                    {
                                    "id": 1,
                                    "user": "user2@mail.ru",
                                    "useful": true,
                                    "active": true,
                                    "streaksAmount": 1,
                                    "name": "Плавать",
                                    "description": "Плавать каждый день",
                                    "dateOfCreate":
                                """ +
                                LocalDate.now() +
                                """
                                            ,
                                            "plannedNextPerform":
                                        """ +
                                LocalDate.now().plusDays(1) +
                                """
                                            ,
                                            "lastRealPerform":
                                        """ +
                                LocalDate.now() +
                                """
                                            ,
                                            "frequency": 1,
                                            "performsAmount": 1,
                                            "remind": "Perform the habit in 1 days"
                                            }
                                        ]
                                        """
                        )
                );
    }
}