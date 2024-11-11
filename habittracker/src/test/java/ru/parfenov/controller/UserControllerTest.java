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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class UserControllerTest {
    static MockMvc staticMockMvc;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15");

    @Autowired
    MockMvc mockMvc;

    @BeforeAll
    static void setUp(@Autowired MockMvc mockMvc) throws Exception {
        String json1 = "{\"email\": \"user2@mail.ru\", \"password\": \"password2\", \"name\": \"user2\"}";
        String json2 = "{\"email\": \"user3@mail.ru\", \"password\": \"password3\", \"name\": \"user3\"}";
        String json3 = "{\"email\": \"user4@mail.ru\", \"password\": \"password4\", \"name\": \"user4\"}";
        mockMvc.perform(post("/sign-up").contentType(MediaType.APPLICATION_JSON).content(json1));
        mockMvc.perform(post("/sign-up").contentType(MediaType.APPLICATION_JSON).content(json2));
        mockMvc.perform(post("/sign-up").contentType(MediaType.APPLICATION_JSON).content(json3));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    @DisplayName("Проверка viewUser()")
    void viewUser() throws Exception {
        this.mockMvc.perform(get("/users/user/2"))
                .andExpectAll(
                        status().isOk(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        content().json("""
                                {"id": 2, "email": "user2@mail.ru", "name": "user2", "role": "CLIENT", "blocked": false}
                                """
                        )
                );
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    @DisplayName("Проверка findByParameters()")
    void findByParameters() throws Exception {
        this.mockMvc.perform(get("/users/find-by-parameters?role=CLIENT&name=user2&block=false"))
                .andExpectAll(
                        status().isOk(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        content().json("""
                                [{"id":2, "email": "user2@mail.ru", "name": "user2", "role": "CLIENT", "blocked": false}]
                                """
                        )
                );
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    @DisplayName("Проверка findAll()")
    void findAllUsers() throws Exception {
        this.mockMvc.perform(get("/users/all"))
                .andExpectAll(
                        status().isOk(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        content().json("""
                                [
                                {"id": 1, "email": "admin@YLabHabitApp.com", "name": "main admin", "role": "ADMIN", "blocked": false},
                                {"id": 2, "email": "user2@mail.ru", "name": "user2", "role": "CLIENT", "blocked": false},
                                {"id": 3, "email": "user3@mail.ru", "name": "user3", "role": "CLIENT", "blocked": false}
                                ]
                                """
                        )
                );
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    @DisplayName("Проверка delete()")
    void check_delete() throws Exception {
        this.mockMvc.perform(delete("/users/delete/4"))
                .andExpectAll(
                        status().isOk(),
                        content().string("User is deleted")
                );
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    @DisplayName("Проверка update()")
    void update() throws Exception {
        String json = "{\"id\": \"4\", \"password\": \"password45\", \"name\": \"user45\", \"role\": \"CLIENT\", \"blocked\": false}";
        this.mockMvc.perform(post("/users/update").contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpectAll(
                        status().isOk(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        content().json("""
                                {"id": 4, "email": "user4@mail.ru", "name": "user45", "role": "CLIENT", "blocked": false}
                                """
                        )
                );
    }
}