package ru.parfenov.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.parfenov.dto.habit.HabitCreateDTO;
import ru.parfenov.dto.habit.HabitGeneralDTO;
import ru.parfenov.dto.habit.HabitStatisticDTO;
import ru.parfenov.dto.habit.HabitUpdateDTO;
import ru.parfenov.service.HabitService;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/habits")
@RequiredArgsConstructor
public class HabitController {
    private final HabitService habitService;

    /**
     * Страница, где пользователь создаёт свою привычку с занесением в базу данных
     * Метод обработает HTTP запрос Post.
     *
     * @param request  HTTP запрос
     * @param habitDTO сущность Habit, обвёрнутая в DTO для подачи в виде Json
     * @return ответ сервера
     */
    @PostMapping("/create")
    public ResponseEntity<HabitGeneralDTO> create(HttpServletRequest request, @RequestBody HabitCreateDTO habitDTO) {
        Optional<HabitGeneralDTO> habitGeneralDTO = habitService.create(request, habitDTO);
        if (habitGeneralDTO.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return habitGeneralDTO
                    .map(habitGeneralDTOGet -> new ResponseEntity<>(habitGeneralDTOGet, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
        }
    }

    /**
     * Обработка запроса на вывод привычек юзера и статистики по ним за кокой-либо период
     *
     * @param request  HTTP запрос
     * @param dateFrom начальная дата периода для вывода статистики
     * @param dateTo   конечная дата периода для вывода статистики
     * @return ответ сервера
     */
    @GetMapping("/statistic")
    public ResponseEntity<List<HabitStatisticDTO>> statistic(
            HttpServletRequest request,
            @RequestParam(required = false) String dateFrom,
            @RequestParam(required = false) String dateTo
    ) {
        List<HabitStatisticDTO> result = habitService.statisticForUser(request, dateFrom, dateTo);
        if (result.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }

    /**
     * Обработка запроса на выполнение юзером своей привычки
     *
     * @param request HTTP запрос
     * @param habitId ID привычки
     * @return ответ сервера
     */
    @PostMapping("/perform/{habitId}")
    public ResponseEntity<HabitGeneralDTO> perform(HttpServletRequest request, @PathVariable long habitId) {
        Optional<HabitGeneralDTO> result = habitService.perform(request, habitId);
        return result.map(
                habitGeneralDTO -> new ResponseEntity<>(habitGeneralDTO, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE)
                );
    }

    /**
     * Обработка запроса на обновление юзером своей привычки
     *
     * @param request  HTTP запрос
     * @param habitDTO сущность Habit, обвёрнутая в DTO для подачи в виде Json
     * @return ответ сервера
     */
    @PostMapping("/update")
    public ResponseEntity<HabitGeneralDTO> update(HttpServletRequest request, @RequestBody HabitUpdateDTO habitDTO) {
        Optional<HabitGeneralDTO> result = habitService.updateByUser(request, habitDTO);
        return result.map(
                habitGeneralDTO -> new ResponseEntity<>(habitGeneralDTO, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE)
                );
    }

    /**
     * Обработка запроса на вывод привычек юзера по заданным параметрам
     *
     * @param request      HTTP запрос
     *                     Параметры ниже могут быть заданы пустой строкой, если не участвуют в отборе:
     * @param usefulness   полезность
     * @param active       активность
     * @param name         название привычки
     * @param description  часть строки в описании привычки
     * @param dateOfCreate дата создания
     * @param frequency    частота выполнения в днях
     * @return ответ сервера
     */
    @GetMapping("/your-list-by-param")
    public ResponseEntity<List<HabitGeneralDTO>> findByParam(
            HttpServletRequest request,
            @RequestParam(required = false) String usefulness,
            @RequestParam(required = false) String active,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String dateOfCreate,
            @RequestParam(required = false) int frequency
    ) {
        List<HabitGeneralDTO> result =
                habitService.findByParameters(request, usefulness, active, name, description, dateOfCreate, frequency);
        if (result.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }

    /**
     * Обработка запроса на вывод всех привычек юзера
     *
     * @param request HTTP запрос
     * @return ответ сервера
     */
    @GetMapping("/your-all-list")
    public ResponseEntity<List<HabitGeneralDTO>> findYourAll(HttpServletRequest request) {
        List<HabitGeneralDTO> result = habitService.findByUser(request);
        if (result.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }

    /**
     * Обработка запроса на вывод всех привычек юзера к выполнению сегодня
     *
     * @param request HTTP запрос
     * @return ответ сервера
     */
    @GetMapping("/your-today-list")
    public ResponseEntity<List<HabitGeneralDTO>> findTodayList(HttpServletRequest request) {
        List<HabitGeneralDTO> result = habitService.todayPerforms(request);
        if (result.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }
}