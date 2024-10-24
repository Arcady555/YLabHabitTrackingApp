package ru.parfenov.dto;

import lombok.Data;

/**
 * DTO для удобной подачи в json. Enum заменили на String
 */
@Data
public class CarDTO {
    private int id;
    private int ownerId;
    private String brand;
    private String model;
    private int yearOfProd;
    private int price;
    private String condition;
}