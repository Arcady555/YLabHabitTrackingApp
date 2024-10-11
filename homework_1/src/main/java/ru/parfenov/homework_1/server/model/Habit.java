package ru.parfenov.homework_1.server.model;

import lombok.*;

import java.time.Period;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Habit {
    private long id;
    private User user;
    private boolean usefulness;
    private boolean active;
    private String name;
    private String description;
    private LocalDate dateOfCreate;
    private LocalDate  plannedFirstPerform;
    private LocalDate plannedPrevPerform;
    private LocalDate plannedNextPerform;
    private LocalDate lastRealPerform;
    private Period frequency;
    private int performsAmount;
}