package ru.parfenov.dto;

import org.mapstruct.Mapper;
import ru.parfenov.homework_3.model.Order;

@Mapper
public interface OrderDTOMapper {
    Order toOrder(OrderDTO destination);
}