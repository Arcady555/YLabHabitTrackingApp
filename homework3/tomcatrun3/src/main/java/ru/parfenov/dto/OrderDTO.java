package ru.parfenov.dto;

import lombok.Data;

/**
 * DTO для удобной подачи в json. Enum заменили на String
 */
@Data
public class OrderDTO {
    private int id;
    private int authorId;
    private int carId;
    private String type;
    private String status;
}