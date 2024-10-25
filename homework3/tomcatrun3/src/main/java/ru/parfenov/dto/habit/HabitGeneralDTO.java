package ru.parfenov.dto.habit;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.parfenov.model.User;

@Data
@AllArgsConstructor
public class HabitGeneralDTO {
    private long id;
    private String user;
    private boolean useful;
    private boolean active;
    private int streaksAmount;
    private String name;
    private String description;
    private String dateOfCreate;
    private String plannedNextPerform;
    private String lastRealPerform;
    private int frequency;
    private int performsAmount;
    private String remind;
}