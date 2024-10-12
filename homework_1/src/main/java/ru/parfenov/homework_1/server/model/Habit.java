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
    private boolean useful;
    private boolean active;
    private int streaksAmount;
    private String name;
    private String description;
    private LocalDate dateOfCreate;
    private LocalDate  plannedFirstPerform;
    private LocalDate plannedPrevPerform;
    private LocalDate plannedNextPerform;
    private LocalDate lastRealPerform;
    private Period frequency;
    private int performsAmount;

    @Override
    public String toString() {
        return "Habit{" +
                "id=" + id +
                ", user=" + user.getEmail() +
                ", usefulness=" + useful +
                ", active=" + active +
                ", streaksAmount=" + streaksAmount +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", dateOfCreate=" + dateOfCreate +
                ", plannedFirstPerform=" + plannedFirstPerform +
                ", plannedPrevPerform=" + plannedPrevPerform +
                ", plannedNextPerform=" + plannedNextPerform +
                ", lastRealPerform=" + lastRealPerform +
                ", frequency=" + frequency +
                ", performsAmount=" + performsAmount +
                '}';
    }
}